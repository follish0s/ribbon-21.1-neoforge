package com.example.ribbon;

import com.example.ribbon.entityculling.EntityCullingManager;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
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
            LOGGER.info("[Ribbon] Client setup complete");
        }
    }
}