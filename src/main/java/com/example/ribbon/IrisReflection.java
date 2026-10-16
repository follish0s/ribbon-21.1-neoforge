package com.example.ribbon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Optional;

/**
 * 用反射访问 Iris，避免编译期依赖 Iris。
 * 如果 Iris 未安装或 API 变化，所有方法静默返回 null / false。
 */
public final class IrisReflection {

    private static final Logger LOGGER = LoggerFactory.getLogger("Ribbon/IrisReflection");

    private static boolean initialized = false;
    private static boolean irisPresent = false;

    private static Method getIrisConfigMethod;
    private static Method getShaderpacksDirectoryMethod;
    private static Method setShadersEnabledStaticMethod;
    private static Method getShaderPackNameMethod;
    private static Method setShadersEnabledConfigMethod;
    private static Method saveConfigMethod;
    private static Method areShadersEnabledMethod;

    private IrisReflection() {}

    private static synchronized void init() {
        if (initialized) return;
        initialized = true;

        try {
            Class<?> irisClass = Class.forName("net.irisshaders.iris.Iris");
            irisPresent = true;

            try {
                getIrisConfigMethod = irisClass.getMethod("getIrisConfig");
            } catch (NoSuchMethodException ignored) {}

            try {
                getShaderpacksDirectoryMethod = irisClass.getMethod("getShaderpacksDirectory");
            } catch (NoSuchMethodException ignored) {}

            try {
                setShadersEnabledStaticMethod = irisClass.getMethod("setShadersEnabled", boolean.class);
            } catch (NoSuchMethodException ignored) {}

            if (getIrisConfigMethod != null) {
                Class<?> configClass = getIrisConfigMethod.getReturnType();

                try {
                    getShaderPackNameMethod = configClass.getMethod("getShaderPackName");
                } catch (NoSuchMethodException ignored) {}

                try {
                    setShadersEnabledConfigMethod = configClass.getMethod("setShadersEnabled", boolean.class);
                } catch (NoSuchMethodException ignored) {}

                try {
                    saveConfigMethod = configClass.getMethod("save");
                } catch (NoSuchMethodException ignored) {}

                // 尝试多种方法名，Iris 不同版本可能不同
                for (String name : new String[]{"areShadersEnabled", "getShadersEnabled", "isShadersEnabled"}) {
                    try {
                        areShadersEnabledMethod = configClass.getMethod(name);
                        LOGGER.info("[Ribbon] 找到 Iris 光影启用状态方法: {}", name);
                        break;
                    } catch (NoSuchMethodException ignored) {}
                }
            }

            LOGGER.info("[Ribbon] Iris 反射初始化完成");
        } catch (ClassNotFoundException e) {
            LOGGER.info("[Ribbon] Iris 未安装，光影兼容性检测已禁用");
        }
    }

    public static boolean isIrisPresent() {
        init();
        return irisPresent;
    }

    /**
     * 获取当前选中的光影包路径。
     * 无法获取时返回 null。
     */
    public static Path getCurrentShaderpackPath() {
        init();
        if (!irisPresent || getIrisConfigMethod == null) return null;

        try {
            Object config = getIrisConfigMethod.invoke(null);
            if (config == null) return null;

            String name = null;
            if (getShaderPackNameMethod != null) {
                Object result = getShaderPackNameMethod.invoke(config);
                if (result instanceof Optional<?> opt) {
                    if (opt.isPresent()) {
                        name = String.valueOf(opt.get());
                    }
                } else if (result instanceof String s) {
                    name = s;
                }
            }

            if (name == null || name.isEmpty()) return null;

            if (getShaderpacksDirectoryMethod != null) {
                Object dirObj = getShaderpacksDirectoryMethod.invoke(null);
                if (dirObj instanceof Path dir) {
                    return dir.resolve(name);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("[Ribbon] 获取当前光影包路径失败", e);
        }
        return null;
    }

    /**
     * 查询 Iris 当前是否启用光影。
     * Iris 未安装或 API 不可用时返回 false。
     */
    public static boolean areShadersEnabled() {
        init();
        if (!irisPresent || getIrisConfigMethod == null || areShadersEnabledMethod == null) {
            return false;
        }
        try {
            Object config = getIrisConfigMethod.invoke(null);
            if (config == null) return false;
            Object result = areShadersEnabledMethod.invoke(config);
            if (result instanceof Boolean b) return b;
        } catch (Exception e) {
            LOGGER.debug("[Ribbon] 获取光影启用状态失败", e);
        }
        return false;
    }

    /**
     * 尝试禁用 Iris 光影。
     * 成功返回 true，所有方案失败返回 false。
     */
    public static boolean disableShaders() {
        init();
        if (!irisPresent) return false;

        // 方案 1：Iris.setShadersEnabled(false)
        if (setShadersEnabledStaticMethod != null) {
            try {
                setShadersEnabledStaticMethod.invoke(null, false);
                LOGGER.info("[Ribbon] 已通过 Iris.setShadersEnabled(false) 禁用光影");
                return true;
            } catch (Exception e) {
                LOGGER.debug("[Ribbon] Iris.setShadersEnabled 调用失败", e);
            }
        }

        // 方案 2：IrisConfig.setShadersEnabled(false) + save()
        if (getIrisConfigMethod != null && setShadersEnabledConfigMethod != null) {
            try {
                Object config = getIrisConfigMethod.invoke(null);
                setShadersEnabledConfigMethod.invoke(config, false);
                if (saveConfigMethod != null) {
                    saveConfigMethod.invoke(config);
                }
                LOGGER.info("[Ribbon] 已通过 IrisConfig 禁用光影");
                return true;
            } catch (Exception e) {
                LOGGER.debug("[Ribbon] IrisConfig 禁用光影失败", e);
            }
        }

        LOGGER.warn("[Ribbon] 无法禁用光影，所有反射方案均失败");
        return false;
    }
}