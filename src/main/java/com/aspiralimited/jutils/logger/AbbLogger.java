package com.aspiralimited.jutils.logger;

import org.apache.logging.log4j.Logger;

import static org.apache.logging.log4j.LogManager.getLogger;

public class AbbLogger {
    private final Logger log4j;

    private String className; // TODO
    private String prefix; // TODO

    public AbbLogger() {
        this(new Error().getStackTrace()[1].getClassName());
    }

    public AbbLogger(String className) {
        this.className = className;
        this.log4j = getLogger(className);
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

    public void debug(String s) {
        log4j.debug(s);
    }

    public void debug(String s, Object o) {
        log4j.debug(s, o);
    }

    public void debug(String s, Object o, Object o1) {
        log4j.debug(s, o, o1);
    }

    public void debug(String s, Object... objects) {
        log4j.debug(s, objects);
    }

    public void debug(String s, Throwable throwable) {
        log4j.debug(s, throwable);
    }

    public void info(String s) {
        log4j.info(s);
    }

    public void info(Throwable throwable) {
        log4j.info("{}", throwable);
    }

    public void info(String s, Object o) {
        log4j.info(s, o);
    }

    public void info(String s, Object o, Object o1) {
        log4j.info(s, o, o1);
    }

    public void info(String s, Object... objects) {
        log4j.info(s, objects);
    }

    public void info(String s, Throwable throwable) {
        log4j.info(s, throwable);
    }

    public void warn(String s) {
        log4j.warn(s);
    }

    public void warn(String s, Object o) {
        log4j.warn(s, o);
    }

    public void warn(String s, Object... objects) {
        log4j.warn(s, objects);
    }

    public void warn(String s, Object o, Object o1) {
        log4j.warn(s, o, o1);
    }

    public void warn(String s, Throwable throwable) {
        log4j.warn(s, throwable);
    }

    public void warn(Throwable throwable) {
        log4j.warn("{}", throwable);
    }

    public void error(String msg) {
        log4j.error(msg);

        NewRelicLogger.error(className, msg);
        AirbrakeLogger.error(className, msg);
        RollbarLogger.error(className, msg);
    }

    public void error(Throwable throwable) {
        log4j.error("{}", throwable);

        NewRelicLogger.error(className, throwable);
        AirbrakeLogger.error(className, throwable);
        RollbarLogger.error(className, throwable);
    }

    public void error(String msg, Object object) {
        log4j.error(msg, object);

        NewRelicLogger.error(className, msg, object);
        AirbrakeLogger.error(className, msg, object);
        RollbarLogger.error(className, msg, object);
    }

    public void error(String msg, Object obj1, Object obj2) {
        log4j.error(msg, obj1, obj2);

        NewRelicLogger.error(className, msg, obj1, obj2);
        AirbrakeLogger.error(className, msg, obj1, obj2);
        RollbarLogger.error(className, msg, obj1, obj2);
    }

    public void error(String msg, Object... objects) {
        log4j.error(msg, objects);

        NewRelicLogger.error(className, msg, objects);
        AirbrakeLogger.error(className, msg, objects);
        RollbarLogger.error(className, msg, objects);
    }

    public void error(String msg, Throwable throwable) {
        log4j.error(msg, throwable);

        NewRelicLogger.error(className, msg, throwable);
        AirbrakeLogger.error(className, msg, throwable);
        RollbarLogger.error(className, msg, throwable);
    }
}
