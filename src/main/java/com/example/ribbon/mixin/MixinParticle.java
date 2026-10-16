package com.example.ribbon.mixin;

import com.example.ribbon.render.ParticleCullingManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public abstract class MixinParticle {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void ribbon$cullParticle(VertexConsumer buffer, Camera camera, float partialTicks,
                                     CallbackInfo ci) {
        Particle self = (Particle) (Object) this;
        if (ParticleCullingManager.shouldCull(self, camera)) {
            ci.cancel();
        }
    }
}