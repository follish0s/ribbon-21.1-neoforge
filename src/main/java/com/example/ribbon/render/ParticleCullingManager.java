package com.example.ribbon.render;

import com.example.ribbon.Config;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;

/**
 * 粒子剔除管理器。
 * 只做距离剔除，不做 raycast（粒子生命周期短，raycast 不划算）。
 */
public final class ParticleCullingManager {

    private static long culled = 0;

    private ParticleCullingManager() {}

    public static boolean shouldCull(Particle particle, Camera camera) {
        if (!Config.isParticleCullingEnabled()) return false;

        double maxDist = Config.getParticleCullingRange();
        double distSq = particle.getPos().distanceToSqr(camera.getPosition());

        if (distSq > maxDist * maxDist) {
            culled++;
            return true;
        }
        return false;
    }

    public static long getCulledCount() {
        return culled;
    }
}