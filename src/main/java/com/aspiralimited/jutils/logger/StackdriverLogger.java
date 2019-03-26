package com.aspiralimited.jutils.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;

import java.util.Map;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static java.lang.Thread.currentThread;

class StackdriverLogger {

    private static final ObjectMapper MAPPER = new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS);

    static void send(Level level, String className, String msg) {
        print(level, className, msg, null, 1);
    }

    static void send(Level level, String className, String msg, Object... objects) {
        try {
            if (msg != null) {
                msg = String.format(msg.replaceAll("\\{}", "%s"), objects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        print(level, className, msg, null, 1);
    }

    static void send(Level level, String className, String msg, Throwable throwable) {
        print(level, className, msg, throwable, 1);
    }

    static void send(Level level, String className, Throwable throwable) {
        print(level, className, throwable.getMessage(), throwable, 1);
    }

    private static void print(Level level, String className, String msg, Object object, int retryCount) {
        String severity = (level == Level.WARN) ? "WARNING" : level.getStandardLevel().toString();

        Map<String, Object> map = Map.of(
                "severity", severity,
                "thread", currentThread().getId(),
                "name", className,
                "message", msg == null ? "null" : msg,
                "payload", object == null ? "" : object
        );

        try {
            System.out.println(MAPPER.writeValueAsString(map));
        } catch (Exception e) {
            if (retryCount == 1) {
                print(level, className, msg, object == null ? "" : object.toString(), ++retryCount);
            } else {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
