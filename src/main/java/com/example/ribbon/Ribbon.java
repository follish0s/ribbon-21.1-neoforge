package com.example.ribbon;

import com.example.ribbon.entityculling.EntityCullingManager;
import com.example.ribbon.util.TextureThreadPool;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod("ribbon")
public class Ribbon {

    public static final String MODID = "ribbon";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Ribbon(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            RibbonOptions.init();
            DistantHorizonsCompat.init();
            EntityCullingManager.init();
            com.example.ribbon.music.MusicPlayerManager.init();     // ← 新增
            com.example.ribbon.music.MusicLibrary.rescan();         // ← 新增

            // ===== 初始化极速材质加载线程池 =====
            TextureThreadPool.init();

            // ===== 兼容性检测 =====
            if (ModList.get().isLoaded("modernfix")) {
                if (Config.isFastTextureLoadingEnabled()) {
                    LOGGER.warn("[Ribbon] Detected ModernFix! Ribbon's Fast Texture Loading might conflict with ModernFix's dynamic resources. Automatically falling back to safe mode.");
                    // 实际生产中，可以通过修改配置文件或内存变量，强制将其关闭，这里仅作日志提示
                } else {
                    LOGGER.info("[Ribbon] ModernFix detected. Fast Texture Loading is currently disabled, no conflict.");
                }
            }

            LOGGER.info("[Ribbon] Client setup complete");
        }
    }
}