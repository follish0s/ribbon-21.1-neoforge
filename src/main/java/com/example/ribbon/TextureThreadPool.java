/*
 * Copyright (C) 2026 follish0s
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package com.example.ribbon.util;

import com.example.ribbon.Config;
import com.example.ribbon.LangUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextureThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger("Ribbon/TextureLoader");
    private static ExecutorService executor;

    public static void init() {
        if (executor != null) executor.shutdown();
        int threads = Config.getTextureLoadingThreads();
        executor = Executors.newFixedThreadPool(threads, r -> {
            Thread t = new Thread(r, "Ribbon-Texture-Worker");
            t.setDaemon(true);
            return t;
        });
        if (Config.isTextureDebugLogEnabled()) {
            LOGGER.info(LangUtil.tr("log.texture.pool_init", threads));
        }
    }

    public static ExecutorService get() {
        if (executor == null) init();
        return executor;
    }

    /**
     * 检测 JVM 堆内存压力。如果使用率 > 85%，返回 true。
     * Javadoc Note: Thread-safe. Can be called from any thread.
     */
    public static boolean isMemoryPressureHigh() {
        Runtime rt = Runtime.getRuntime();
        long max = rt.maxMemory();
        long used = rt.totalMemory() - rt.freeMemory();
        return used > (max * 0.85);
    }
}