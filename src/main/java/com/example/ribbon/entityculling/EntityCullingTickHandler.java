package com.example.ribbon.entityculling;

import com.example.ribbon.Config;
import com.example.ribbon.ShadowRendererDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/**
 * 每 tick 提交一批实体做遮挡检测。
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = "ribbon")
public final class EntityCullingTickHandler {

    private EntityCullingTickHandler() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (!Config.isEntityCullingEnabled()) return;

        // 影子渲染阶段不提交任务
        if (ShadowRendererDetector.isShadowPassActive()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        // F3 调试屏打开时不剔除，方便调试
        if (mc.getDebugOverlay() != null && mc.getDebugOverlay().showDebugScreen()) {
            return;
        }

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        EntityCullingManager.submitBatch(mc.level.entitiesForRendering(), cameraPos, mc.level);
    }
}