package com.aspiralimited.jutils.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.logging.log4j.LogManager.getLogger;

public class AbbLogger {

    private static final AtomicBoolean stackDriverEnabled = new AtomicBoolean(false);
    private static AtomicReference<Level> level = new AtomicReference<>(Level.ALL);

    private final Logger log4j;
    private String className;
    private String prefix; // TODO


    public AbbLogger() {
        this(new Error().getStackTrace()[1].getClassName());
    }

    public AbbLogger(String className) {
        this.className = className;
        this.log4j = getLogger(className);
    }

    public static boolean stackdriverEnabled() {
        return stackDriverEnabled.get();
    }

    public static void stackDriverEnabled(boolean stackdriverEnabled) {
        stackDriverEnabled.set(stackdriverEnabled);
    }

    public static void level(Level level) {
        AbbLogger.level.set(level);
    }

    public static Level getLevel() {
        return level.get();
    }

    public AbbLogger(Class klass) {
        this(klass.getSimpleName());
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void trace(String s) {
        log4j.trace(s);
    }

    public void trace(String s, Object o) {
        log4j.trace(s, o);
    }

    public void trace(String s, Object o, Object o1) {
        log4j.trace(s, o, o1);
    }

    public void trace(String s, Object o, Object o1, Object o2) {
        log4j.trace(s, o, o1, o2);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3) {
        log4j.trace(s, o, o1, o2, o3);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4) {
        log4j.trace(s, o, o1, o2, o3, o4);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
        log4j.trace(s, o, o1, o2, o3, o4, o5);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        log4j.trace(s, o, o1, o2, o3, o4, o5, o6);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        log4j.trace(s, o, o1, o2, o3, o4, o5, o6, o7);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        log4j.trace(s, o, o1, o2, o3, o4, o5, o6, o7, o8);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        log4j.trace(s, o, o1, o2, o3, o4, o5, o6, o7, o8, o9);
    }

    public void debug(String msg) {
        if (level.get().compareTo(Level.DEBUG) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.DEBUG, className, msg);
        else log4j.debug(msg);
    }

    public void debug(String msg, Object object) {
        if (level.get().compareTo(Level.DEBUG) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.DEBUG, className, msg, object);
        else log4j.debug(msg, object);
    }

    public void debug(String msg, Object object1, Object object2) {
        if (level.get().compareTo(Level.DEBUG) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.DEBUG, className, msg, object1, object2);
        else log4j.debug(msg, object1, object2);
    }

    public void debug(String msg, Object... objects) {
        if (level.get().compareTo(Level.DEBUG) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.DEBUG, className, msg, objects);
        else log4j.debug(msg, objects);
    }

    public void debug(String msg, Throwable throwable) {
        if (level.get().compareTo(Level.DEBUG) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.DEBUG, className, msg, throwable);
        else log4j.debug(msg, throwable);
    }

    public void info(String msg) {
        if (level.get().compareTo(Level.INFO) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.INFO, className, msg);
        else log4j.info(msg);
    }

    public void info(Throwable throwable) {
        if (level.get().compareTo(Level.INFO) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.INFO, className, throwable);
        else log4j.info("{}", throwable);
    }

    public void info(String msg, Object object) {
        if (level.get().compareTo(Level.INFO) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.INFO, className, msg, object);
        else log4j.info(msg, object);
    }

    public void info(String msg, Object object1, Object object2) {
        if (level.get().compareTo(Level.INFO) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.INFO, className, msg, object1, object2);
        else log4j.info(msg, object1, object2);
    }

    public void info(String msg, Object... objects) {
        if (level.get().compareTo(Level.INFO) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.INFO, className, msg, objects);
        else log4j.info(msg, objects);
    }

    public void info(String msg, Throwable throwable) {
        if (level.get().compareTo(Level.INFO) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.INFO, className, msg, throwable);
        else log4j.info(msg, throwable);
    }

    public void warn(String msg) {
        if (level.get().compareTo(Level.WARN) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.WARN, className, msg);
        else log4j.warn(msg);
    }

    public void warn(String msg, Object object) {
        if (level.get().compareTo(Level.WARN) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.WARN, className, msg, object);
        else log4j.warn(msg, object);
    }

    public void warn(String msg, Object... objects) {
        if (level.get().compareTo(Level.WARN) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.WARN, className, msg, objects);
        else log4j.warn(msg, objects);
    }

    public void warn(String msg, Object o, Object o1) {
        if (level.get().compareTo(Level.WARN) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.WARN, className, msg, o, o1);
        else log4j.warn(msg, o, o1);
    }

    public void warn(String msg, Throwable throwable) {
        if (level.get().compareTo(Level.WARN) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.WARN, className, msg, throwable);
        else log4j.warn(msg, throwable);
    }

    public void warn(Throwable throwable) {
        if (level.get().compareTo(Level.WARN) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.WARN, className, throwable);
        else log4j.warn("{}", throwable);
    }

    public void error(String msg) {
        if (level.get().compareTo(Level.ERROR) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.ERROR, className, msg);
        else log4j.error(msg);

        NewRelicLogger.error(className, msg);
        RollbarLogger.error(className, msg);
    }

    public void error(Throwable throwable) {
        if (level.get().compareTo(Level.ERROR) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.ERROR, className, throwable);
        else log4j.error("{}", throwable);

        NewRelicLogger.error(className, throwable);
        RollbarLogger.error(className, throwable);
    }

    public void error(String msg, Object object) {
        if (level.get().compareTo(Level.ERROR) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.ERROR, className, msg, object);
        else log4j.error(msg, object);

        NewRelicLogger.error(className, msg, object);
        RollbarLogger.error(className, msg, object);
    }

    public void error(String msg, Object obj1, Object obj2) {
        if (level.get().compareTo(Level.ERROR) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.ERROR, className, msg, obj1, obj2);
        else log4j.error(msg, obj1, obj2);

        NewRelicLogger.error(className, msg, obj1, obj2);
        RollbarLogger.error(className, msg, obj1, obj2);
    }

    public void error(String msg, Object... objects) {
        if (level.get().compareTo(Level.ERROR) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.ERROR, className, msg, objects);
        else log4j.error(msg, objects);

        NewRelicLogger.error(className, msg, objects);
        RollbarLogger.error(className, msg, objects);
    }

    public void error(String msg, Throwable throwable) {
        if (level.get().compareTo(Level.ERROR) < 0) return;

        if (stackdriverEnabled()) StackdriverLogger.send(Level.ERROR, className, msg, throwable);
        else log4j.error(msg, throwable);

        NewRelicLogger.error(className, msg, throwable);
        RollbarLogger.error(className, msg, throwable);
    }
}
