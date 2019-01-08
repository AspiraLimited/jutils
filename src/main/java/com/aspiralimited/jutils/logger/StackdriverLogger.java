package com.aspiralimited.jutils.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.currentThread;

class StackdriverLogger {

    static void send(Level level, String className, String msg) {
        print(level, className, msg, null);
    }

    static void send(Level level, String className, String msg, Object... objects) {
        print(level, className, msg, objects);
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

            Map<String, Object> map = new HashMap<>(Map.of(
                    "severity", severity,
                    "thread", currentThread().getId(),
                    "name", className,
                    "message", msg

            ));

            if (object != null) map.put("payload", object);

            System.out.println(new ObjectMapper().writeValueAsString(map));
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
            throwable.printStackTrace();
        }
    }
}
