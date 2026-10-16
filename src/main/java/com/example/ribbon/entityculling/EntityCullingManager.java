package com.example.ribbon.entityculling;

import com.example.ribbon.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体遮挡剔除管理器。
 *
 * 安全设计原则：
 * 1. 绝不修改视锥体剔除的结果。只在"视锥体判定为可见"之后追加判断。
 * 2. 所有 raycast 在主线程执行（避免异步访问区块导致崩溃）。
 * 3. 分帧限流：每 tick 最多处理 N 个实体。
 * 4. 保守放行：未检测过、距离过远、异常，一律认为可见。
 * 5. 结果缓存若干 tick，避免每帧都重新计算。
 */
public final class EntityCullingManager {

    private static final Logger LOGGER = LoggerFactory.getLogger("Ribbon/EntityCulling");

    /** 结果缓存：实体ID → 可见性判定（true = 可见，false = 被遮挡） */
    private static final Map<Integer, CachedResult> CACHE = new ConcurrentHashMap<>();

    /** 每 tick 最多处理的实体数 */
    private static final int BATCH_SIZE = 8;

    /** 结果缓存有效期（毫秒） */
    private static final long CACHE_TTL_MS = 200;

    /** 距离超过这个值不做遮挡检测（直接可见） */
    private static final double MAX_CHECK_DISTANCE_SQ = 96 * 96;

    private EntityCullingManager() {}

    public static void init() {
        LOGGER.info("[Ribbon] 实体剔除已初始化（每 tick 最多检测 {} 个实体）", BATCH_SIZE);
    }

    public static void shutdown() {
        CACHE.clear();
    }

    /**
     * 由 tick handler 调用，提交一批实体做遮挡检测。
     * 只处理缓存过期或未检测的实体。
     */
    public static void submitBatch(Iterable<Entity> entities, Vec3 cameraPos, Level level) {
        if (!Config.isEntityCullingEnabled()) {
            CACHE.clear();
            return;
        }

        long now = System.currentTimeMillis();
        int range = Config.getEntityCullingRange();
        double maxDistSq = (double) range * range;

        int processed = 0;
        for (Entity entity : entities) {
            if (processed >= BATCH_SIZE) break;
            if (entity == null) continue;

            CachedResult cached = CACHE.get(entity.getId());
            if (cached != null && now - cached.timestamp < CACHE_TTL_MS) {
                continue; // 缓存未过期，跳过
            }

            // 距离太远直接标记可见
            if (cameraPos.distanceToSqr(entity.position()) > maxDistSq) {
                CACHE.put(entity.getId(), new CachedResult(true, now));
                continue;
            }

            // 主线程 raycast
            boolean visible = traceVisibility(level, cameraPos, entity);
            CACHE.put(entity.getId(), new CachedResult(visible, now));
            processed++;
        }
    }

    /**
     * 查询实体是否可见。
     * 注意：此方法只在 Mixin 的 shouldRender 已经判定"可见"之后调用。
     * 返回 false 意味着"追加剔除"，绝不改变视锥体的原始判定。
     */
    public static boolean isVisible(Entity entity) {
        if (!Config.isEntityCullingEnabled()) return true;

        CachedResult cached = CACHE.get(entity.getId());
        // 未检测过 → 保守放行
        if (cached == null) return true;
        return cached.visible;
    }

    /**
     * 主线程 raycast：从相机到实体眼睛位置，检查是否被方块阻挡。
     * 异常时返回 true（保守放行）。
     */
    private static boolean traceVisibility(Level level, Vec3 cameraPos, Entity entity) {
        try {
            Vec3 targetPos = entity.getEyePosition();

            BlockHitResult hit = level.clip(new ClipContext(
                    cameraPos, targetPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    net.minecraft.world.phys.shapes.CollisionContext.empty()
            ));

            // 如果 raycast 撞到方块（未到达实体位置），判定被遮挡
            return hit.getType() == HitResult.Type.MISS;
        } catch (Exception e) {
            // 任何异常 → 保守认为可见
            return true;
        }
    }

    /** 实体被卸载时清除缓存 */
    public static void onEntityRemoved(int entityId) {
        CACHE.remove(entityId);
    }

    public static void clear() {
        CACHE.clear();
    }

    public static int getCacheSize() {
        return CACHE.size();
    }

    private record CachedResult(boolean visible, long timestamp) {}
}