package com.dogbody.spring.framework.web.common.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.core.env.Environment;

/**
 * @author zhangdd on 2023/6/7
 */
public class EnvUtils {

    public static final String DEVELOPMENT = "dev";
    public static final String TESTING = "test";
    public static final String PRODUCTION = "prod";

    public static boolean isDev() {
        return DEVELOPMENT.equals(getProfile());
    }

    public static boolean isDevelopment() {
        return isDev();
    }

    public static boolean isTest() {
        return TESTING.equals(getProfile());
    }

    public static boolean isTesting() {
        return isTest();
    }

    public static boolean isProd() {
        return PRODUCTION.equals(getProfile());
    }

    public static boolean isProduction() {
        return isProd();
    }

    public static String getProfile() {
        Environment environment = SpringUtil.getBean(Environment.class);
        return getProfile(environment);
    }

    public static String getProfile(Environment environment) {
        String[] profiles = environment.getActiveProfiles();
        if (ArrayUtil.isEmpty(profiles)) {
            profiles = environment.getDefaultProfiles();
        }
        return profiles[0];
    }


}
