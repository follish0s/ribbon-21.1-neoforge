package com.example.ribbon.mixin;

import com.example.ribbon.Config;
import com.example.ribbon.ShadowRendererDetector;
import com.example.ribbon.entityculling.EntityCullingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.renderer.entity.EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    private void ribbon$occlusionCulling(
            Entity entity,
            Frustum frustum,
            double camX, double camY, double camZ,
            CallbackInfoReturnable<Boolean> cir) {

        // 视锥体结果优先，绝不覆盖
        Boolean frustumResult = cir.getReturnValue();
        if (frustumResult == null || !frustumResult) return;

        // 影子渲染阶段跳过所有剔除
        if (ShadowRendererDetector.isShadowPassActive()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && entity == mc.player) return;

        // ===== 掉落物距离剔除 =====
        if (Config.isItemEntityCullingEnabled() && entity instanceof ItemEntity) {
            double maxDist = Config.getItemEntityMaxDistance();
            Vec3 cameraPos = new Vec3(camX, camY, camZ);
            if (cameraPos.distanceToSqr(entity.position()) > maxDist * maxDist) {
                cir.setReturnValue(false);
                return;
            }
        }

        // ===== 通用实体遮挡剔除 =====
        if (!Config.isEntityCullingEnabled()) return;
        if (!EntityCullingManager.isVisible(entity)) {
            cir.setReturnValue(false);
        }
    }
}