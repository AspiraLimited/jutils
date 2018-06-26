package com.aspiralimited.jutils.logger;

import com.newrelic.api.agent.NewRelic;

import java.util.Map;

public class NewRelicLogger {

    public static void error(String className, String msg) {
        NewRelic.noticeError(className + ": " + String.valueOf(msg));
    }

    public static void error(String className, String msg, Object object) {
        NewRelic.noticeError(className + ": " + String.valueOf(msg) + ": " + object.toString());
    }

    public static void error(String className, String msg, Object obj1, Object obj2) {
        NewRelic.noticeError(className + ": " + String.valueOf(msg) + ": [" + obj1.toString() + ", [" + obj2.toString() + "]", Map.of(
                "obj1", obj1.toString(),
                "obj2", obj2.toString()));
    }

    public static void error(String className, String msg, Throwable throwable) {
        NewRelic.noticeError(throwable, Map.of(
                "className", className,
                "msg", String.valueOf(msg)
        ));
    }

    public static void error(String className, Throwable throwable) {
        NewRelic.noticeError(throwable, Map.of("className", className));
    }
}
