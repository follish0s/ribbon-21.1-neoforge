package com.example.ribbon.render;

import com.example.ribbon.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 方块实体遮挡剔除管理器。
 * 主线程 raycast + 结果缓存，参考 EntityCullingManager 的设计。
 */
public final class BlockEntityCullingManager {

    private static final Map<Long, CachedResult> CACHE = new ConcurrentHashMap<>();
    private static final int BATCH_SIZE = 8;
    private static final long CACHE_TTL_MS = 200;

    private BlockEntityCullingManager() {}

    public static void submitBatch(Iterable<BlockEntity> blockEntities, Vec3 cameraPos, Level level) {
        if (!Config.isBlockEntityCullingEnabled()) {
            CACHE.clear();
            return;
        }

        long now = System.currentTimeMillis();
        int range = Config.getBlockEntityCullingRange();
        double maxDistSq = (double) range * range;
        int processed = 0;

        for (BlockEntity be : blockEntities) {
            if (processed >= BATCH_SIZE) break;
            if (be == null || be.isRemoved()) continue;

            long key = be.getBlockPos().asLong();
            CachedResult cached = CACHE.get(key);
            if (cached != null && now - cached.timestamp < CACHE_TTL_MS) continue;

            Vec3 bePos = Vec3.atCenterOf(be.getBlockPos());
            if (cameraPos.distanceToSqr(bePos) > maxDistSq) {
                CACHE.put(key, new CachedResult(true, now));
                continue;
            }

            boolean visible = traceVisibility(level, cameraPos, bePos);
            CACHE.put(key, new CachedResult(visible, now));
            processed++;
        }
    }

    public static boolean isVisible(BlockEntity be) {
        if (!Config.isBlockEntityCullingEnabled()) return true;
        CachedResult cached = CACHE.get(be.getBlockPos().asLong());
        if (cached == null) return true;
        return cached.visible;
    }

    private static boolean traceVisibility(Level level, Vec3 cameraPos, Vec3 targetPos) {
        try {
            BlockHitResult hit = level.clip(new ClipContext(
                    cameraPos, targetPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    CollisionContext.empty()
            ));
            return hit.getType() == HitResult.Type.MISS;
        } catch (Exception e) {
            return true;
        }
    }

    public static void onBlockEntityRemoved(BlockPos pos) {
        CACHE.remove(pos.asLong());
    }

    public static void clear() {
        CACHE.clear();
    }

    private record CachedResult(boolean visible, long timestamp) {}
}