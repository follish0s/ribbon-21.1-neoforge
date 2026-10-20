package com.example.ribbon;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Ribbon.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHOW_OVERLAY;
    public static final ModConfigSpec.EnumValue<OverlayCorner> OVERLAY_CORNER;
    public static final ModConfigSpec.DoubleValue OVERLAY_SCALE;

    public static final ModConfigSpec.BooleanValue RAINBOW_BLOCK_OUTLINE;
    public static final ModConfigSpec.EnumValue<OutlineColorMode> OUTLINE_COLOR_MODE;

    public static final ModConfigSpec.BooleanValue RAINBOW_CROSSHAIR;
    public static final ModConfigSpec.BooleanValue HOTBAR_HIGHLIGHT;
    public static final ModConfigSpec.BooleanValue DAMAGE_NUMBERS;
    public static final ModConfigSpec.DoubleValue DAMAGE_NUMBER_SCALE;
    public static final ModConfigSpec.IntValue DAMAGE_NUMBER_LIFETIME;
    public static final ModConfigSpec.IntValue DAMAGE_NUMBER_MERGE_WINDOW;

    public static final ModConfigSpec.BooleanValue BIOME_TITLE;
    public static final ModConfigSpec.IntValue BIOME_TITLE_DURATION;

    public static final ModConfigSpec.EnumValue<ClickEffectType> CLICK_EFFECT_TYPE;

    public static final ModConfigSpec.BooleanValue RAINBOW_HEALTH;
    public static final ModConfigSpec.EnumValue<HealthColorMode> HEALTH_COLOR_MODE;
    public static final ModConfigSpec.ConfigValue<String> HEALTH_CUSTOM_COLOR;
    public static final ModConfigSpec.DoubleValue HEALTH_RAINBOW_SPEED;
    public static final ModConfigSpec.BooleanValue HEALTH_LOW_SHAKE;
    public static final ModConfigSpec.IntValue HEALTH_LOW_SHAKE_THRESHOLD;

    public static final ModConfigSpec.BooleanValue ZOOM_ENABLED;
    public static final ModConfigSpec.DoubleValue ZOOM_FOV_MULTIPLIER;
    public static final ModConfigSpec.BooleanValue ZOOM_SMOOTH;

    public static final ModConfigSpec.EnumValue<Language> LANGUAGE;

    public static final ModConfigSpec.BooleanValue SHADER_COMPATIBILITY_WARNING;
    public static final ModConfigSpec.BooleanValue SHOW_ADVANCED_DEBUG;

    public static final ModConfigSpec.BooleanValue HIT_PARTICLES;
    public static final ModConfigSpec.IntValue HIT_PARTICLE_INTENSITY;

    public static final ModConfigSpec.BooleanValue CLICK_EFFECT_COVER_UI;
    public static final ModConfigSpec.BooleanValue CLICK_EFFECT_UI_ONLY_ON_WIDGETS;
    public static final ModConfigSpec.BooleanValue CLICK_EFFECT_MUTE_VANILLA;

    public static final ModConfigSpec.BooleanValue ENTITY_CULLING_ENABLED;
    public static final ModConfigSpec.IntValue ENTITY_CULLING_RANGE;
    public static final ModConfigSpec.BooleanValue PARTICLE_CULLING_ENABLED;
    public static final ModConfigSpec.IntValue PARTICLE_CULLING_RANGE;
    public static final ModConfigSpec.BooleanValue BLOCK_ENTITY_CULLING_ENABLED;
    public static final ModConfigSpec.IntValue BLOCK_ENTITY_CULLING_RANGE;
    public static final ModConfigSpec.BooleanValue ITEM_ENTITY_CULLING_ENABLED;
    public static final ModConfigSpec.IntValue ITEM_ENTITY_MAX_DISTANCE;

    public static final ModConfigSpec.BooleanValue LUNAR_HUD_ENABLED;
    public static final ModConfigSpec.EnumValue<OverlayCorner> LUNAR_HUD_CORNER;
    public static final ModConfigSpec.DoubleValue LUNAR_HUD_SCALE;
    public static final ModConfigSpec.BooleanValue LUNAR_SHOW_TERM;
    public static final ModConfigSpec.BooleanValue LUNAR_SHOW_FESTIVAL;
    public static final ModConfigSpec.BooleanValue LUNAR_FESTIVAL_TITLE;
    public static final ModConfigSpec.BooleanValue LUNAR_FESTIVAL_EFFECT;
    public static final ModConfigSpec.EnumValue<HolidayScope> LUNAR_HOLIDAY_SCOPE;
    public static final ModConfigSpec.BooleanValue LUNAR_FESTIVAL_BADGE;
    public static final ModConfigSpec.EnumValue<OverlayCorner> LUNAR_FESTIVAL_BADGE_CORNER;
    public static final ModConfigSpec.BooleanValue LUNAR_FESTIVAL_COUNTDOWN;

    public static final ModConfigSpec.BooleanValue LIGHT_LEVEL_HUD_ENABLED;
    public static final ModConfigSpec.EnumValue<OverlayCorner> LIGHT_LEVEL_HUD_CORNER;
    public static final ModConfigSpec.DoubleValue LIGHT_LEVEL_HUD_SCALE;

    public static final ModConfigSpec.BooleanValue DYNAMIC_LIGHT_ENABLED;
    public static final ModConfigSpec.BooleanValue DYNAMIC_LIGHT_AUTO_DISABLE_WITH_SHADERS;
    public static final ModConfigSpec.IntValue DYNAMIC_LIGHT_RADIUS;
    public static final ModConfigSpec.IntValue DYNAMIC_LIGHT_INTENSITY;
    public static final ModConfigSpec.DoubleValue DYNAMIC_LIGHT_OFFSET_X;
    public static final ModConfigSpec.DoubleValue DYNAMIC_LIGHT_OFFSET_Y;
    public static final ModConfigSpec.DoubleValue DYNAMIC_LIGHT_OFFSET_Z;
    public static final ModConfigSpec.IntValue DYNAMIC_LIGHT_UPDATE_INTERVAL;
    public static final ModConfigSpec.BooleanValue DYNAMIC_LIGHT_SMOOTH_FADE;
    public static final ModConfigSpec.DoubleValue DYNAMIC_LIGHT_SMOOTH_SPEED;
    public static final ModConfigSpec.BooleanValue DYNAMIC_LIGHT_PLAYER_LIGHT;
    public static final ModConfigSpec.BooleanValue DYNAMIC_LIGHT_ITEM_LIGHT;
    public static final ModConfigSpec.IntValue DYNAMIC_LIGHT_ITEM_MIN_LIGHT;

    public static final ModConfigSpec.BooleanValue MUSIC_PLAYER_ENABLED;
    public static final ModConfigSpec.BooleanValue MUSIC_HUD_ENABLED;
    public static final ModConfigSpec.EnumValue<OverlayCorner> MUSIC_HUD_CORNER;

    public static final ModConfigSpec SPEC;

    static {
        BUILDER.push("fps_tps_overlay");
        SHOW_OVERLAY = BUILDER.comment("Show the FPS/TPS overlay on screen").define("showOverlay", true);
        OVERLAY_CORNER = BUILDER.comment("Which corner to draw the overlay in").defineEnum("overlayCorner", OverlayCorner.TOP_LEFT);
        OVERLAY_SCALE = BUILDER.comment("Text size multiplier").defineInRange("overlayScale", 1.0, 0.5, 3.0);
        BUILDER.pop();

        BUILDER.push("music_player");
        MUSIC_PLAYER_ENABLED = BUILDER.comment("启用 MP3 音乐播放器", "音乐文件放在 .minecraft/ribbon_music/ 目录下").define("enabled", true);
        MUSIC_HUD_ENABLED = BUILDER.comment("在 HUD 上显示当前播放的音乐").define("showHud", true);
        MUSIC_HUD_CORNER = BUILDER.comment("音乐 HUD 显示位置").defineEnum("hudCorner", OverlayCorner.BOTTOM_RIGHT);
        BUILDER.pop();

        BUILDER.push("render_settings");
        RAINBOW_BLOCK_OUTLINE = BUILDER.comment("Enable custom block outline").define("rainbowBlockOutline", true);
        OUTLINE_COLOR_MODE = BUILDER.comment("The color mode of the block outline").defineEnum("outlineColorMode", OutlineColorMode.RAINBOW);
        BUILDER.pop();

        BUILDER.push("gui_settings");
        RAINBOW_CROSSHAIR = BUILDER.comment("Enable rainbow crosshair").define("rainbowCrosshair", false);
        HOTBAR_HIGHLIGHT = BUILDER.comment("Highlight the selected hotbar slot").define("hotbarHighlight", false);
        DAMAGE_NUMBERS = BUILDER.comment("Show floating damage numbers").define("damageNumbers", false);
        DAMAGE_NUMBER_SCALE = BUILDER.comment("Damage number size multiplier").defineInRange("damageNumberScale", 2.0, 0.5, 5.0);
        DAMAGE_NUMBER_LIFETIME = BUILDER.comment("How long damage numbers stay on screen (ms)").defineInRange("damageNumberLifetime", 1500, 500, 5000);
        DAMAGE_NUMBER_MERGE_WINDOW = BUILDER.comment("Merge hits within this time window (ms)").defineInRange("damageNumberMergeWindow", 1000, 100, 3000);
        BIOME_TITLE = BUILDER.comment("Show an RPG-style title when entering a new biome").define("biomeTitle", true);
        BIOME_TITLE_DURATION = BUILDER.comment("How long the biome title stays on screen (ms)").defineInRange("biomeTitleDuration", 3000, 1000, 10000);
        CLICK_EFFECT_TYPE = BUILDER.comment("Which click effect to show").defineEnum("clickEffectType", ClickEffectType.RIPPLE);
        BUILDER.pop();

        BUILDER.push("rainbow_health");
        RAINBOW_HEALTH = BUILDER.comment("Render the player health bar in custom colors").define("enabled", false);
        HEALTH_COLOR_MODE = BUILDER.comment("Color mode: RAINBOW or CUSTOM").defineEnum("colorMode", HealthColorMode.RAINBOW);
        HEALTH_CUSTOM_COLOR = BUILDER.comment("Hex color for CUSTOM mode, e.g. #FF5555").define("customColor", "#FF5555");
        HEALTH_RAINBOW_SPEED = BUILDER.comment("Rainbow animation speed multiplier").defineInRange("rainbowSpeed", 1.0, 0.1, 5.0);
        HEALTH_LOW_SHAKE = BUILDER.comment("Shake the health bar when health is low").define("lowShake", true);
        HEALTH_LOW_SHAKE_THRESHOLD = BUILDER.comment("Health percentage below which shaking starts").defineInRange("lowShakeThreshold", 30, 1, 99);
        BUILDER.pop();

        BUILDER.push("zoom");
        ZOOM_ENABLED = BUILDER.comment("Enable OptiFine-style zoom").define("enabled", true);
        ZOOM_FOV_MULTIPLIER = BUILDER.comment("FOV multiplier while zooming").defineInRange("fovMultiplier", 0.25, 0.1, 0.9);
        ZOOM_SMOOTH = BUILDER.comment("Smoothly transition in/out of zoom").define("smooth", true);
        BUILDER.pop();

        BUILDER.push("misc");
        LANGUAGE = BUILDER.comment("Language used by the Ribbon GUI").defineEnum("language", Language.ENGLISH);
        SHADER_COMPATIBILITY_WARNING = BUILDER.comment("检测到不兼容光影时是否拦截并提示", "支持检测 Canvas 和 Vulkan 格式的光影包").define("shaderCompatibilityWarning", true);
        SHOW_ADVANCED_DEBUG = BUILDER.comment("Show advanced system and mod info in the F3 debug screen").define("showAdvancedDebug", true);
        BUILDER.pop();

        BUILDER.push("effects");
        HIT_PARTICLES = BUILDER.comment("Show extra particles when you hit an entity").define("hitParticles", true);
        HIT_PARTICLE_INTENSITY = BUILDER.comment("Multiplier for the number of hit particles").defineInRange("hitParticleIntensity", 3, 1, 10);
        CLICK_EFFECT_COVER_UI = BUILDER.comment("GUI 内点击时也触发 Ribbon 特效").define("coverUi", true);
        CLICK_EFFECT_UI_ONLY_ON_WIDGETS = BUILDER.comment("仅在点击到按钮/控件时才触发特效").define("uiOnlyOnWidgets", false);
        CLICK_EFFECT_MUTE_VANILLA = BUILDER.comment("覆盖时取消原版按钮点击音效").define("muteVanilla", false);
        BUILDER.pop();

        BUILDER.push("performance");
        ENTITY_CULLING_ENABLED = BUILDER.comment("启用异步路径追踪实体剔除", "被方块遮挡的实体将跳过渲染").define("entityCullingEnabled", true);
        ENTITY_CULLING_RANGE = BUILDER.comment("实体剔除的最大检测距离（格）").defineInRange("entityCullingRange", 64, 16, 128);
        PARTICLE_CULLING_ENABLED = BUILDER.comment("剔除远处的粒子").define("particleCullingEnabled", true);
        PARTICLE_CULLING_RANGE = BUILDER.comment("粒子的最大渲染距离（格）").defineInRange("particleCullingRange", 32, 8, 64);
        BLOCK_ENTITY_CULLING_ENABLED = BUILDER.comment("剔除被方块遮挡的方块实体（箱子、熔炉等）").define("blockEntityCullingEnabled", true);
        BLOCK_ENTITY_CULLING_RANGE = BUILDER.comment("方块实体遮挡检测最大距离（格）").defineInRange("blockEntityCullingRange", 64, 16, 128);
        ITEM_ENTITY_CULLING_ENABLED = BUILDER.comment("剔除远处的掉落物").define("itemEntityCullingEnabled", true);
        ITEM_ENTITY_MAX_DISTANCE = BUILDER.comment("掉落物的最大渲染距离（格）").defineInRange("itemEntityMaxDistance", 64, 16, 128);
        BUILDER.pop();

        BUILDER.push("lunar");
        LUNAR_HUD_ENABLED = BUILDER.comment("在 HUD 上显示农历日期").define("lunarHudEnabled", true);
        LUNAR_HUD_CORNER = BUILDER.comment("农历 HUD 显示位置").defineEnum("lunarHudCorner", OverlayCorner.BOTTOM_LEFT);
        LUNAR_HUD_SCALE = BUILDER.comment("农历 HUD 文字缩放").defineInRange("lunarHudScale", 1.0, 0.5, 3.0);
        LUNAR_SHOW_TERM = BUILDER.comment("显示 24 节气").define("showSolarTerm", true);
        LUNAR_SHOW_FESTIVAL = BUILDER.comment("显示传统节日").define("showFestival", true);
        LUNAR_FESTIVAL_TITLE = BUILDER.comment("节日当天进入世界时弹出标题").define("festivalTitle", true);
        LUNAR_FESTIVAL_EFFECT = BUILDER.comment("重大节日当天在玩家周围生成节日粒子").define("festivalEffect", true);
        LUNAR_HOLIDAY_SCOPE = BUILDER.comment("节日显示范围", "CHINA_ONLY = 仅中国节日", "INTERNATIONAL = 中国 + 国际节日", "ALL = 所有国家节日").defineEnum("holidayScope", HolidayScope.INTERNATIONAL);
        LUNAR_FESTIVAL_BADGE = BUILDER.comment("节日当天在屏幕角落显示物品图标徽章").define("festivalBadge", true);
        LUNAR_FESTIVAL_BADGE_CORNER = BUILDER.comment("节日徽章显示位置").defineEnum("festivalBadgeCorner", OverlayCorner.TOP_RIGHT);
        LUNAR_FESTIVAL_COUNTDOWN = BUILDER.comment("HUD 显示距离下一个节日还有几天").define("festivalCountdown", false);
        BUILDER.pop();

        BUILDER.push("hud_extras");
        LIGHT_LEVEL_HUD_ENABLED = BUILDER.comment("显示光照等级 HUD").define("lightLevelHudEnabled", false);
        LIGHT_LEVEL_HUD_CORNER = BUILDER.comment("光照等级 HUD 位置").defineEnum("lightLevelHudCorner", OverlayCorner.TOP_LEFT);
        LIGHT_LEVEL_HUD_SCALE = BUILDER.comment("光照等级 HUD 缩放").defineInRange("lightLevelHudScale", 1.0, 0.5, 3.0);
        BUILDER.pop();

        BUILDER.push("dynamic_light");
        DYNAMIC_LIGHT_ENABLED = BUILDER.comment("启用动态光源").define("enabled", true);
        DYNAMIC_LIGHT_AUTO_DISABLE_WITH_SHADERS = BUILDER.comment("开启光影时自动禁用动态光源").define("autoDisableWithShaders", true);
        DYNAMIC_LIGHT_RADIUS = BUILDER.comment("光源半径（格）").defineInRange("radius", 12, 4, 24);
        DYNAMIC_LIGHT_INTENSITY = BUILDER.comment("最大亮度等级").defineInRange("intensity", 14, 1, 15);
        DYNAMIC_LIGHT_OFFSET_X = BUILDER.comment("X 偏移").defineInRange("offsetX", 0.0, -4.0, 4.0);
        DYNAMIC_LIGHT_OFFSET_Y = BUILDER.comment("Y 偏移").defineInRange("offsetY", 1.0, -4.0, 4.0);
        DYNAMIC_LIGHT_OFFSET_Z = BUILDER.comment("Z 偏移").defineInRange("offsetZ", 0.0, -4.0, 4.0);
        DYNAMIC_LIGHT_UPDATE_INTERVAL = BUILDER.comment("更新频率（tick）").defineInRange("updateInterval", 2, 1, 20);
        DYNAMIC_LIGHT_SMOOTH_FADE = BUILDER.comment("平滑过渡").define("smoothFade", true);
        DYNAMIC_LIGHT_SMOOTH_SPEED = BUILDER.comment("平滑速度").defineInRange("smoothSpeed", 0.25, 0.05, 1.0);
        DYNAMIC_LIGHT_PLAYER_LIGHT = BUILDER.comment("玩家自身发光").define("playerLight", false);
        DYNAMIC_LIGHT_ITEM_LIGHT = BUILDER.comment("手持物品发光").define("itemLight", true);
        DYNAMIC_LIGHT_ITEM_MIN_LIGHT = BUILDER.comment("最低物品发光阈值").defineInRange("itemMinLight", 8, 1, 15);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {}

    public static boolean isShaderCompatibilityWarningEnabled() { try { return SHADER_COMPATIBILITY_WARNING.get(); } catch (IllegalStateException e) { return true; } }
    public static boolean isShowAdvancedDebug() { try { return SHOW_ADVANCED_DEBUG.get(); } catch (IllegalStateException e) { return true; } }
    public static boolean isHitParticlesEnabled() { try { return HIT_PARTICLES.get(); } catch (IllegalStateException e) { return true; } }
    public static boolean isClickEffectCoverUi() { try { return CLICK_EFFECT_COVER_UI.get(); } catch (IllegalStateException e) { return true; } }
    public static boolean isClickEffectUiOnlyOnWidgets() { try { return CLICK_EFFECT_UI_ONLY_ON_WIDGETS.get(); } catch (IllegalStateException e) { return false; } }
    public static boolean isClickEffectMuteVanilla() { try { return CLICK_EFFECT_MUTE_VANILLA.get(); } catch (IllegalStateException e) { return false; } }
    public static boolean isEntityCullingEnabled() { try { return ENTITY_CULLING_ENABLED.get(); } catch (IllegalStateException e) { return true; } }
    public static int getEntityCullingRange() { try { return ENTITY_CULLING_RANGE.get(); } catch (IllegalStateException e) { return 64; } }
    public static boolean isParticleCullingEnabled() { try { return PARTICLE_CULLING_ENABLED.get(); } catch (IllegalStateException e) { return true; } }
    public static int getParticleCullingRange() { try { return PARTICLE_CULLING_RANGE.get(); } catch (IllegalStateException e) { return 32; } }
    public static boolean isBlockEntityCullingEnabled() { try { return BLOCK_ENTITY_CULLING_ENABLED.get(); } catch (IllegalStateException e) { return true; } }
    public static int getBlockEntityCullingRange() { try { return BLOCK_ENTITY_CULLING_RANGE.get(); } catch (IllegalStateException e) { return 64; } }
    public static boolean isItemEntityCullingEnabled() { try { return ITEM_ENTITY_CULLING_ENABLED.get(); } catch (IllegalStateException e) { return true; } }
    public static int getItemEntityMaxDistance() { try { return ITEM_ENTITY_MAX_DISTANCE.get(); } catch (IllegalStateException e) { return 64; } }
    public static boolean isLunarHudEnabled() { try { return LUNAR_HUD_ENABLED.get(); } catch (IllegalStateException e) { return true; } }
    public static OverlayCorner getLunarHudCorner() { try { return LUNAR_HUD_CORNER.get(); } catch (IllegalStateException e) { return OverlayCorner.BOTTOM_LEFT; } }
    public static double getLunarHudScale() { try { return LUNAR_HUD_SCALE.get(); } catch (IllegalStateException e) { return 1.0; } }
    public static boolean isShowSolarTerm() { try { return LUNAR_SHOW_TERM.get(); } catch (IllegalStateException e) { return true; } }
    public static boolean isShowFestival() { try { return LUNAR_SHOW_FESTIVAL.get(); } catch (IllegalStateException e) { return true; } }
    public static boolean isFestivalTitleEnabled() { try { return LUNAR_FESTIVAL_TITLE.get(); } catch (IllegalStateException e) { return true; } }
    public static boolean isFestivalEffectEnabled() { try { return LUNAR_FESTIVAL_EFFECT.get(); } catch (IllegalStateException e) { return true; } }
    public static HolidayScope getHolidayScope() { try { return LUNAR_HOLIDAY_SCOPE.get(); } catch (IllegalStateException e) { return HolidayScope.INTERNATIONAL; } }
    public static boolean isFestivalBadgeEnabled() { try { return LUNAR_FESTIVAL_BADGE.get(); } catch (IllegalStateException e) { return true; } }
    public static OverlayCorner getFestivalBadgeCorner() { try { return LUNAR_FESTIVAL_BADGE_CORNER.get(); } catch (IllegalStateException e) { return OverlayCorner.TOP_RIGHT; } }
    public static boolean isFestivalCountdownEnabled() { try { return LUNAR_FESTIVAL_COUNTDOWN.get(); } catch (IllegalStateException e) { return false; } }
    public static boolean isLightLevelHudEnabled() { try { return LIGHT_LEVEL_HUD_ENABLED.get(); } catch (IllegalStateException e) { return false; } }
    public static OverlayCorner getLightLevelHudCorner() { try { return LIGHT_LEVEL_HUD_CORNER.get(); } catch (IllegalStateException e) { return OverlayCorner.TOP_LEFT; } }
    public static double getLightLevelHudScale() { try { return LIGHT_LEVEL_HUD_SCALE.get(); } catch (IllegalStateException e) { return 1.0; } }
    public static boolean isDynamicLightEnabled() { try { return DYNAMIC_LIGHT_ENABLED.get(); } catch (IllegalStateException e) { return true; } }
    public static boolean isDynamicLightAutoDisableWithShaders() { try { return DYNAMIC_LIGHT_AUTO_DISABLE_WITH_SHADERS.get(); } catch (IllegalStateException e) { return true; } }
    public static int getDynamicLightRadius() { try { return DYNAMIC_LIGHT_RADIUS.get(); } catch (IllegalStateException e) { return 12; } }
    public static int getDynamicLightIntensity() { try { return DYNAMIC_LIGHT_INTENSITY.get(); } catch (IllegalStateException e) { return 14; } }
    public static double getDynamicLightOffsetX() { try { return DYNAMIC_LIGHT_OFFSET_X.get(); } catch (IllegalStateException e) { return 0.0; } }
    public static double getDynamicLightOffsetY() { try { return DYNAMIC_LIGHT_OFFSET_Y.get(); } catch (IllegalStateException e) { return 1.0; } }
    public static double getDynamicLightOffsetZ() { try { return DYNAMIC_LIGHT_OFFSET_Z.get(); } catch (IllegalStateException e) { return 0.0; } }
    public static int getDynamicLightUpdateInterval() { try { return DYNAMIC_LIGHT_UPDATE_INTERVAL.get(); } catch (IllegalStateException e) { return 2; } }
    public static boolean isDynamicLightSmoothFade() { try { return DYNAMIC_LIGHT_SMOOTH_FADE.get(); } catch (IllegalStateException e) { return true; } }
    public static double getDynamicLightSmoothSpeed() { try { return DYNAMIC_LIGHT_SMOOTH_SPEED.get(); } catch (IllegalStateException e) { return 0.25; } }
    public static boolean isDynamicLightPlayerLight() { try { return DYNAMIC_LIGHT_PLAYER_LIGHT.get(); } catch (IllegalStateException e) { return false; } }
    public static boolean isDynamicLightItemLight() { try { return DYNAMIC_LIGHT_ITEM_LIGHT.get(); } catch (IllegalStateException e) { return true; } }
    public static int getDynamicLightItemMinLight() { try { return DYNAMIC_LIGHT_ITEM_MIN_LIGHT.get(); } catch (IllegalStateException e) { return 8; } }
    public static boolean isDamageNumbersEnabled() { try { return DAMAGE_NUMBERS.get(); } catch (IllegalStateException e) { return false; } }
    public static double getDamageNumberScale() { try { return DAMAGE_NUMBER_SCALE.get(); } catch (IllegalStateException e) { return 2.0; } }
    public static int getDamageNumberLifetime() { try { return DAMAGE_NUMBER_LIFETIME.get(); } catch (IllegalStateException e) { return 1500; } }
    public static int getDamageNumberMergeWindow() { try { return DAMAGE_NUMBER_MERGE_WINDOW.get(); } catch (IllegalStateException e) { return 1000; } }
    public static boolean isMusicPlayerEnabled() { try { return MUSIC_PLAYER_ENABLED.get(); } catch (IllegalStateException e) { return true; } }
    public static boolean isMusicHudEnabled() { try { return MUSIC_HUD_ENABLED.get(); } catch (IllegalStateException e) { return true; } }
    public static OverlayCorner getMusicHudCorner() { try { return MUSIC_HUD_CORNER.get(); } catch (IllegalStateException e) { return OverlayCorner.BOTTOM_RIGHT; } }
    public static int getHitParticleIntensity() { try { return HIT_PARTICLE_INTENSITY.get(); } catch (IllegalStateException e) { return 3; } }

    public enum OverlayCorner { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }
    public enum OutlineColorMode { RAINBOW, BLUE_PURPLE, GREEN_PURPLE, BLUE_GREEN, PURPLE_YELLOW, PURPLE_GREEN_YELLOW }
    public enum ClickEffectType { NONE, RIPPLE, RAIN, SAKURA, CARD, RAT, OX, TIGER, RABBIT, DRAGON, SNAKE, HORSE, GOAT, MONKEY, ROOSTER, DOG, PIG, SPRING, SUMMER, AUTUMN, WINTER }
    public enum HealthColorMode { RAINBOW, CUSTOM }
    public enum Language { ENGLISH, CHINESE }
    public enum HolidayScope { CHINA_ONLY, INTERNATIONAL, ALL }
}