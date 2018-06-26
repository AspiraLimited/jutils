package com.aspiralimited.jutils.logger;


import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.notifier.config.ConfigBuilder;

public class RollbarLogger {

    private static Rollbar rollbar = null;

    public static void load(String accessToken, String environment) {
        load(accessToken, environment, false);
    }

    public static void load(String accessToken, String environment, boolean force) {
        if (!force && rollbar != null)
            throw new RuntimeException("already loaded RollbarLogger");

        synchronized (RollbarLogger.class) {
            Config config = ConfigBuilder.withAccessToken(accessToken)
                    .environment(environment)
                    .codeVersion("1.2.0")
                    .build();
            RollbarLogger.rollbar = Rollbar.init(config);
        }
    }

    static void error(String className, String msg) {
        if (rollbar == null) return;
        rollbar.error(new Exception(className + ": " + msg));
    }

    static void error(String className, String msg, Object object) {
        if (rollbar == null) return;
        rollbar.error(new Exception(className + ": " + msg + ": " + object.toString()));
    }

    static void error(String className, String msg, Object obj1, Object obj2) {
        if (rollbar == null) return;
        rollbar.error(new Exception(className + ": " + msg + ": [" + obj1.toString() + ", [" + obj2.toString() + "]"));
    }

    static void error(String className, String msg, Throwable throwable) {
        if (rollbar == null) return;
        rollbar.error(throwable, className + ": " + msg);
    }

    static void error(String className, Throwable throwable) {
        if (rollbar == null) return;
        rollbar.error(throwable, className);
    }
}

