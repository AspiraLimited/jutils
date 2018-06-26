package com.aspiralimited.jutils.logger;

import io.airbrake.javabrake.Notice;
import io.airbrake.javabrake.Notifier;

public class AirbrakeLogger {

    private static Notifier notifier = null;

    public static Notifier load(int projectId, String projectKey, String environment) {
        return load(projectId, projectKey, environment, false);
    }

    public static Notifier load(int projectId, String projectKey, String environment, boolean force) {
        if (!force && notifier != null)
            throw new RuntimeException("already loaded AirbrakeLogger");

        synchronized (AirbrakeLogger.class) {
            AirbrakeLogger.notifier = new Notifier(projectId, projectKey);

            notifier.addFilter(
                    (Notice notice) -> {
                        notice.setContext("environment", environment);
                        return notice;
                    });
        }

        return notifier;
    }

    public static void setContext(String key, String value) {
        if (notifier == null) return;
        notifier.addFilter(
                (Notice notice) -> {
                    notice.setContext(key, value);
                    return notice;
                });
    }


    static void error(String className, String msg) {
        if (notifier == null) return;
        notifier.report(new Exception(className + ": " + String.valueOf(msg)));
    }

    static void error(String className, String msg, Object object) {
        if (notifier == null) return;
        notifier.report(new Exception(className + ": " + String.valueOf(msg) + ": " + object.toString()));
    }

    static void error(String className, String msg, Object obj1, Object obj2) {
        if (notifier == null) return;
        notifier.report(new Exception(className + ": " + String.valueOf(msg) + ": [" + obj1.toString() + ", [" + obj2.toString() + "]"));
    }

    static void error(String className, String msg, Throwable throwable) {
        if (notifier == null) return;
        notifier.report(new Exception(className + ": " + String.valueOf(msg), throwable));
    }

    static void error(String className, Throwable throwable) {
        if (notifier == null) return;
        notifier.report(new Exception(className, throwable));
    }
}
