package com.aspiralimited.jutils.logger;

import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.*;

import java.util.Arrays;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.joining;

public class StackdriverLogger {

    private static final Logging stackdriver = LoggingOptions.getDefaultInstance().getService();

    public static void send(Severity severity, String className, String msg) {
        LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(msg))
                .setSeverity(severity)
                .setLogName(className)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        stackdriver.write(singleton(entry));
    }

    public static void send(Severity severity, String className, String msg, Object object) {
        msg += "\n" + object.toString();
        send(severity, className, msg);
    }

    public static void send(Severity severity, String className, String msg, Object obj1, Object obj2) {
        msg += "\n" + obj1.toString() + "\n" + obj2.toString();
        send(severity, className, msg);
    }

    public static void send(Severity severity, String className, String msg, Object... objects) {
        msg += Arrays.stream(objects).map(Object::toString).collect(joining("\n", "\n", ""));
        send(severity, className, msg);
    }

    public static void send(Severity severity, String className, String msg, Throwable throwable) {
        msg += "\n" + throwableToString(throwable);
        send(severity, className, msg);
    }

    public static void send(Severity severity, String className, Throwable throwable) {
        String msg = "\n" + throwableToString(throwable);
        send(severity, className, msg);
    }

    private static String throwableToString(Throwable throwable) {
        return Arrays.stream(throwable.getStackTrace()).map(StackTraceElement::toString).collect(joining("\n"));
    }
}
