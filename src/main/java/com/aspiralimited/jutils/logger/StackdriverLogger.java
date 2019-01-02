package com.aspiralimited.jutils.logger;

import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.*;

import java.util.Arrays;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.joining;

public class StackdriverLogger {

    private static final Logging stackdriver = LoggingOptions.getDefaultInstance().getService();

    public static void error(String className, String msg) {
        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, String msg, Object object) {
        msg += "\n" + object.toString();

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, String msg, Object obj1, Object obj2) {
        msg += "\n" + obj1.toString() + "\n" + obj2.toString();

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, String msg, Object... objects) {
        msg += Arrays.stream(objects).map(Object::toString).collect(joining("\n", "\n", ""));

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, String msg, Throwable throwable) {
        msg += "\n" + throwableToString(throwable);

        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(Severity.ERROR)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void error(String className, Throwable throwable) {
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
