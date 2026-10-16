package com.example.ribbon.music;

import com.example.ribbon.LangUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.io.File;
import java.util.List;

/**
 * 独立音乐播放器界面。
 * 纯黑背景 + 白色字体 + 四角点缀。
 */
public class MusicPlayerScreen extends Screen {

    private static final int ROW_HEIGHT = 18;
    private static final int LIST_TOP = 64;
    private static final int LIST_BOTTOM_MARGIN = 56;
    private static final int SIDE_PADDING = 24;

    private int scrollOffset = 0;

    public MusicPlayerScreen() {
        super(Component.literal(LangUtil.tr("music.screen.title")));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // ===== 纯黑背景 =====
        g.fill(0, 0, this.width, this.height, 0xFF000000);

        // ===== 点缀 1：四角直角标记 =====
        drawCorner(g, 8, 8, 1, 1);
        drawCorner(g, this.width - 8, 8, -1, 1);
        drawCorner(g, 8, this.height - 8, 1, -1);
        drawCorner(g, this.width - 8, this.height - 8, -1, -1);

        // ===== 点缀 2：标题下方装饰线 =====
        int decoW = 80;
        g.fill((this.width - decoW) / 2, 38, (this.width + decoW) / 2, 39, 0x66FFFFFF);

        // ===== 点缀 3：底部细线 =====
        g.fill(SIDE_PADDING, this.height - 40,
                this.width - SIDE_PADDING, this.height - 39, 0x33FFFFFF);

        // ===== 标题（白色，居中，带阴影） =====
        String title = LangUtil.tr("music.screen.title");
        int tw = this.font.width(title);
        g.drawString(this.font, title, (this.width - tw) / 2, 22, 0xFFFFFFFF, true);

        // ===== 曲目列表 =====
        List<File> tracks = MusicLibrary.getTracks();
        if (tracks.isEmpty()) {
            String empty = LangUtil.tr("music.no_tracks");
            int ew = this.font.width(empty);
            g.drawString(this.font, empty, (this.width - ew) / 2,
                    this.height / 2, 0xFFAAAAAA, true);
            drawBottomHint(g);
            return;
        }

        int listBottom = this.height - LIST_BOTTOM_MARGIN;
        int maxRows = (listBottom - LIST_TOP) / ROW_HEIGHT;
        int currentIndex = MusicPlayerManager.getCurrentIndex();
        Mp3Player player = MusicPlayerManager.getPlayer();

        int maxScroll = Math.max(0, tracks.size() - maxRows);
        scrollOffset = Mth.clamp(scrollOffset, 0, maxScroll);

        int left = SIDE_PADDING;
        int right = this.width - SIDE_PADDING;

        for (int i = 0; i < maxRows; i++) {
            int idx = i + scrollOffset;
            if (idx >= tracks.size()) break;

            int y = LIST_TOP + i * ROW_HEIGHT;
            boolean hovered = mouseX >= left && mouseX <= right
                    && mouseY >= y && mouseY < y + ROW_HEIGHT;

            String prefix;
            int color;
            if (idx == currentIndex) {
                if (player.isPlaying()) {
                    prefix = ">> ";
                    color = 0xFF55FFAA;
                } else if (player.isPaused()) {
                    prefix = "|| ";
                    color = 0xFFFFAA55;
                } else {
                    prefix = "   ";
                    color = 0xFF55FFAA;
                }
            } else {
                prefix = "   ";
                color = hovered ? 0xFFFFFFFF : 0xFFBFBFBF;
            }

            // hover 高亮
            if (hovered) {
                g.fill(left, y, right, y + ROW_HEIGHT, 0x22FFFFFF);
            }

            String name = tracks.get(idx).getName().replaceAll("(?i)\\.mp3$", "");
            String line = String.format("%02d. %s%s", idx + 1, prefix, name);
            g.drawString(this.font, line, left + 4, y + 5, color, false);
        }

        // ===== 滚动指示 =====
        if (tracks.size() > maxRows) {
            String counter = (scrollOffset + 1) + "-"
                    + Math.min(tracks.size(), scrollOffset + maxRows)
                    + " / " + tracks.size();
            int cw = this.font.width(counter);
            g.drawString(this.font, counter, this.width - cw - SIDE_PADDING,
                    this.height - 30, 0xFF888888, false);
        }

        drawBottomHint(g);
    }

    private void drawBottomHint(GuiGraphics g) {
        String hint = LangUtil.tr("music.screen.hint");
        int hw = this.font.width(hint);
        g.drawString(this.font, hint, (this.width - hw) / 2,
                this.height - 28, 0xFF888888, true);
    }

    private void drawCorner(GuiGraphics g, int x, int y, int dx, int dy) {
        int len = 14;
        int color = 0x66FFFFFF;
        g.fill(x, y, x + dx * len, y + dy, color);
        g.fill(x, y, x + dx, y + dy * len, color);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != 0) return super.mouseClicked(mx, my, button);

        List<File> tracks = MusicLibrary.getTracks();
        if (tracks.isEmpty()) return super.mouseClicked(mx, my, button);

        int listBottom = this.height - LIST_BOTTOM_MARGIN;
        int maxRows = (listBottom - LIST_TOP) / ROW_HEIGHT;

        int left = SIDE_PADDING;
        int right = this.width - SIDE_PADDING;

        if (mx >= left && mx <= right
                && my >= LIST_TOP && my < LIST_TOP + maxRows * ROW_HEIGHT) {
            int i = (int) ((my - LIST_TOP) / ROW_HEIGHT);
            int idx = i + scrollOffset;
            if (idx >= 0 && idx < tracks.size()) {
                MusicPlayerManager.playIndex(idx);
                return true;
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
        scrollOffset -= (int) Math.signum(scrollY);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // ESC 关闭——Screen 默认处理
        // 其他按键放行给 tick handler
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}