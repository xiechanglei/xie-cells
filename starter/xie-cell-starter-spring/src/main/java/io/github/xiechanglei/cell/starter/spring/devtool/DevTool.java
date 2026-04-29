package io.github.xiechanglei.cell.starter.spring.devtool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 判断当前的运行环境
 */
@Component
public class DevTool {

    private static String env;

    @Value("${spring.profiles.active}")
    private void setEnv(String profile) {
        env = profile;
    }

    public static boolean isDev() {
        return "dev".equals(env) || "development".equals(env);
    }

    public static boolean isProd() {
        return "prod".equals(env);
    }

    public static boolean isTest() {
        return "test".equals(env);
    }
}
