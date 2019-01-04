package com.aspiralimited.jutils.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.Map;

import static java.lang.Thread.currentThread;

public class StackdriverLogger {

    public static void send(Level level, String className, String msg) {
        send(level, className, msg, msg);
    }

    public static void send(Level level, String className, String msg, Object object) {
        Map<String, Object> map = Map.of(
                "severity", level == Level.WARN ? "WARNING" : level.getStandardLevel().toString(),
                "thread", currentThread().getId(),
                "name", className,
                "message", msg,
                "payload", object
        );

        try {
            System.out.println(new ObjectMapper().writeValueAsString(map));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void send(Level level, String className, String msg, Object obj1, Object obj2) {
        send(level, className, msg, Arrays.asList(obj1, obj2));
    }

    public static void send(Level level, String className, String msg, Object... objects) {
        send(level, className, msg, objects);
    }

    public static void send(Level level, String className, String msg, Throwable throwable) {
        send(level, className, msg, throwable);
    }

    public static void send(Level level, String className, Throwable throwable) {
        send(level, className, throwable.getMessage(), throwable);
    }
}
