package com.example.ribbon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = "ribbon", value = Dist.CLIENT)
public class DamageNumberHandler {

    private static final List<DamageNumber> DAMAGE_NUMBERS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    private static final int COLOR_NORMAL = 0xFFFFFF;
    private static final int COLOR_FIRE = 0xFF4500;
    private static final int COLOR_FREEZE = 0x00FFFF;
    private static final int COLOR_LIGHTNING = 0xFFFF00;
    private static final int COLOR_MAGIC = 0x800080;

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        if (!Config.isDamageNumbersEnabled()) return;
        if (!(event.getSource().getEntity() instanceof Player)) return;

        float damage = event.getNewDamage();
        if (damage <= 0) return;

        int color = determineColor(event.getSource());

        double x = event.getEntity().getX();
        double y = event.getEntity().getY() + event.getEntity().getBbHeight() + 0.3;
        double z = event.getEntity().getZ();

        DAMAGE_NUMBERS.add(new DamageNumber(damage, color, x, y, z, System.currentTimeMillis()));
    }

    private static int determineColor(DamageSource source) {
        if (source.is(DamageTypeTags.IS_FIRE)) return COLOR_FIRE;
        String msgId = source.getMsgId();
        if (msgId.equals("snowball") || msgId.equals("freeze")) return COLOR_FREEZE;
        if (msgId.equals("lightningBolt")) return COLOR_LIGHTNING;
        if (msgId.equals("magic") || msgId.equals("indirectMagic") || msgId.equals("thrown"))
            return COLOR_MAGIC;
        return COLOR_NORMAL;
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (!Config.isDamageNumbersEnabled()) return;

        GuiGraphics gui = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        long currentTime = System.currentTimeMillis();
        DAMAGE_NUMBERS.removeIf(dn -> dn.isExpired(currentTime));

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        for (DamageNumber dn : DAMAGE_NUMBERS) {
            dn.render(gui, mc, currentTime, screenWidth, screenHeight);
        }
    }

    private static class DamageNumber {
        float damage;
        int color;
        double worldX, worldY, worldZ;
        long spawnTime;
        double offsetX;
        double offsetZ;

        DamageNumber(float damage, int color, double worldX, double worldY, double worldZ, long spawnTime) {
            this.damage = damage;
            this.color = color;
            this.worldX = worldX;
            this.worldY = worldY;
            this.worldZ = worldZ;
            this.spawnTime = spawnTime;
            this.offsetX = (RANDOM.nextDouble() - 0.5) * 0.6;
            this.offsetZ = (RANDOM.nextDouble() - 0.5) * 0.6;
        }

        boolean isExpired(long currentTime) {
            return currentTime - spawnTime > 1500;
        }

        void render(GuiGraphics gui, Minecraft mc, long currentTime, int screenWidth, int screenHeight) {
            float t = (currentTime - spawnTime) / 1000.0f;

            double initialVy = 1.5;
            double gravity = 4.0;
            double yOffset = initialVy * t - 0.5 * gravity * t * t;

            double currentX = worldX + offsetX;
            double currentY = worldY + yOffset;
            double currentZ = worldZ + offsetZ;

            var camera = mc.gameRenderer.getMainCamera();
            Vec3 camPos = camera.getPosition();
            float yawRad = (float) Math.toRadians(camera.getYRot());
            float pitchRad = (float) Math.toRadians(camera.getXRot());

            double fx = -Math.sin(yawRad) * Math.cos(pitchRad);
            double fy = -Math.sin(pitchRad);
            double fz =  Math.cos(yawRad) * Math.cos(pitchRad);

            double rx = Math.cos(yawRad);
            double rz = Math.sin(yawRad);

            double ux = fy * rz;
            double uy = fz * rx - fx * rz;
            double uz = -fy * rx;

            double dx = currentX - camPos.x;
            double dy = currentY - camPos.y;
            double dz = currentZ - camPos.z;

            double forward = dx * fx + dy * fy + dz * fz;
            double rightC  = dx * rx + dz * rz;
            double upC     = dx * ux + dy * uy + dz * uz;

            if (forward < 0.5) return;

            double fovRad = Math.toRadians(mc.options.fov().get() / 2.0);
            double projScale = (screenHeight / 2.0) / Math.tan(fovRad);

            int screenX = (int) (screenWidth / 2.0 + (rightC / forward) * projScale);
            int screenY = (int) (screenHeight / 2.0 - (upC / forward) * projScale);

            float progress = t / 1.5f;
            float alpha = 1.0f - progress;
            if (alpha <= 0) return;

            int renderColor = color;
            renderColor = (renderColor & 0x00FFFFFF) | ((int) (alpha * 255) << 24);

            String text = (damage == (int) damage)
                    ? String.valueOf((int) damage)
                    : String.format("%.1f", damage);

            float textScale = (float) Config.getDamageNumberScale();
            if (textScale < 0.5f) textScale = 1.0f;
            if (textScale > 5.0f) textScale = 5.0f;

            var pose = gui.pose();
            pose.pushPose();
            pose.translate(screenX, screenY, 0);
            pose.scale(textScale, textScale, 1.0f);
            gui.drawString(mc.font, text, 0, 0, renderColor, true);
            pose.popPose();
        }
    }
}