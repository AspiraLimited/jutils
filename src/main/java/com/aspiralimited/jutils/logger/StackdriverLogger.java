package com.aspiralimited.jutils.logger;

import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.*;

import java.util.Arrays;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.joining;

public class StackdriverLogger {

    private static final Logging stackdriver = LoggingOptions.getDefaultInstance().getService();
    private static boolean stackdriverEnabled = false;

    public static boolean isStackdriverEnabled() {
        return stackdriverEnabled;
    }

    public static void setStackdriverEnabled(boolean newState) {
        stackdriverEnabled = newState;
    }

    public static void error(String className, String msg) {
        if (!stackdriverEnabled) return;

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, String msg, Object object) {
        if (!stackdriverEnabled) return;

        msg += "\n" + object.toString();

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, String msg, Object obj1, Object obj2) {
        if (!stackdriverEnabled) return;

        msg += "\n" + obj1.toString() + "\n" + obj2.toString();

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, String msg, Object... objects) {
        if (!stackdriverEnabled) return;

        msg += Arrays.stream(objects).map(Object::toString).collect(joining("\n", "\n", ""));

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, String msg, Throwable throwable) {
        if (!stackdriverEnabled) return;

        msg += "\n" + throwableToString(throwable);

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, Throwable throwable) {
        if (!stackdriverEnabled) return;

        String msg = "\n" + throwableToString(throwable);

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    private static String throwableToString(Throwable throwable) {
        return Arrays.stream(throwable.getStackTrace()).map(StackTraceElement::toString).collect(joining("\n"));
    }
}
