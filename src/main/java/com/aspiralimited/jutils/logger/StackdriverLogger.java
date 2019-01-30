package com.aspiralimited.jutils.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;

import java.util.Map;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static java.lang.Thread.currentThread;

class StackdriverLogger {

    private static final ObjectMapper MAPPER = new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS);

    static void send(Level level, String className, String msg) {
        print(level, className, msg, null);
    }

    static void send(Level level, String className, String msg, Object... objects) {
        try {
            msg = String.format(msg, objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        print(level, className, msg, null);
    }

    static void send(Level level, String className, String msg, Throwable throwable) {
        print(level, className, msg, throwable);
    }

    static void send(Level level, String className, Throwable throwable) {
        print(level, className, throwable.getMessage(), throwable);
    }

    private static void print(Level level, String className, String msg, Object object) {
        try {
            String severity = (level == Level.WARN) ? "WARNING" : level.getStandardLevel().toString();

            Map<String, Object> map = Map.of(
                    "severity", severity,
                    "thread", currentThread().getId(),
                    "name", className,
                    "message", msg == null ? "null" : msg,
                    "payload", object == null ? "" : object

            );

            System.out.println(MAPPER.writeValueAsString(map));
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
            throwable.printStackTrace();
        }
    }
}
