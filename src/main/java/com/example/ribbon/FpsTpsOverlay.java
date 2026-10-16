package com.example.ribbon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = "ribbon", value = Dist.CLIENT)
public class FpsTpsOverlay {

    private static long lastServerTick = 0L;
    private static double smoothedTps = 20.0;

    // 1. 事件改为 ServerTickEvent.Post
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        long now = System.nanoTime();
        if (lastServerTick != 0L) {
            double tps = 1_000_000_000.0 / (now - lastServerTick);
            smoothedTps = smoothedTps * 0.7 + tps * 0.3;
        }
        lastServerTick = now;
    }

    // 2. 渲染事件改为 RenderGuiEvent.Post
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.level == null) return;
        if (!Config.SHOW_OVERLAY.get()) return;

        GuiGraphics g = event.getGuiGraphics();
        int fps = mc.getFps();
        String text = String.format("FPS: %d | TPS: %.1f", fps, Math.min(smoothedTps, 20.0));

        int tw = mc.font.width(text);
        int th = mc.font.lineHeight;
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();
        float s = Config.OVERLAY_SCALE.get().floatValue();
        int m = 4;

        float tx, ty;
        switch (Config.OVERLAY_CORNER.get()) {
            case TOP_RIGHT    -> { tx = sw - m - tw * s; ty = m; }
            case BOTTOM_LEFT  -> { tx = m;             ty = sh - m - th * s; }
            case BOTTOM_RIGHT -> { tx = sw - m - tw * s; ty = sh - m - th * s; }
            default           -> { tx = m;             ty = m; }
        }

        var pose = g.pose();
        pose.pushPose();
        pose.translate(tx, ty, 0.0f);
        pose.scale(s, s, 1.0f);
        g.drawString(mc.font, text, 0, 0, 0xFFFFFF);
        pose.popPose();
    }
}