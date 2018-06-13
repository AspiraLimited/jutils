package com.aspiralimited.jutils;

import com.newrelic.api.agent.NewRelic;
import org.apache.logging.log4j.Logger;

import static org.apache.logging.log4j.LogManager.getLogger;

public class AbbLogger {
    private final Logger logger;
    private String prefix; // TODO

    public AbbLogger() {
        this(new Error().getStackTrace()[1].getClassName());
    }

    private AbbLogger(String name) {
        logger = getLogger(name);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void trace(String s) {
        logger.trace(s);
    }

    public void trace(String s, Object o) {
        logger.trace(s, o);
    }

    public void trace(String s, Object o, Object o1) {
        logger.trace(s, o, o1);
    }

    public void trace(String s, Object o, Object o1, Object o2) {
        logger.trace(s, o, o1, o2);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3) {
        logger.trace(s, o, o1, o2, o3);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4) {
        logger.trace(s, o, o1, o2, o3, o4);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
        logger.trace(s, o, o1, o2, o3, o4, o5);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        logger.trace(s, o, o1, o2, o3, o4, o5, o6);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        logger.trace(s, o, o1, o2, o3, o4, o5, o6, o7);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        logger.trace(s, o, o1, o2, o3, o4, o5, o6, o7, o8);
    }

    public void trace(String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        logger.trace(s, o, o1, o2, o3, o4, o5, o6, o7, o8, o9);
    }

    public void debug(String s) {
        logger.debug(s);
    }

    public void debug(String s, Object o) {
        logger.debug(s, o);
    }

    public void debug(String s, Object o, Object o1) {
        logger.debug(s, o, o1);
    }

    public void debug(String s, Object... objects) {
        logger.debug(s, objects);
    }

    public void debug(String s, Throwable throwable) {
        logger.debug(s, throwable);
    }

    public void info(String s) {
        logger.info(s);
    }

    public void info(Throwable throwable) {
        logger.info(throwable);
    }

    public void info(String s, Object o) {
        logger.info(s, o);
    }

    public void info(String s, Object o, Object o1) {
        logger.info(s, o, o1);
    }

    public void info(String s, Object... objects) {
        logger.info(s, objects);
    }

    public void info(String s, Throwable throwable) {
        logger.info(s, throwable);
    }

    public void warn(String s) {
        logger.warn(s);
    }

    public void warn(String s, Object o) {
        logger.warn(s, o);
    }

    public void warn(String s, Object... objects) {
        logger.warn(s, objects);
    }

    public void warn(String s, Object o, Object o1) {
        logger.warn(s, o, o1);
    }

    public void warn(String s, Throwable throwable) {
        logger.warn(s, throwable);
    }

    public void error(String s) {
        logger.error(s);
        NewRelic.noticeError(s);
    }

    public void error(Throwable throwable) {
        logger.error(throwable);
        NewRelic.noticeError(throwable);
    }

    public void error(String s, Object o) {
        logger.error(s, o);
        NewRelic.noticeError(s + " " + o.toString());
    }

    public void error(String s, Object o, Object o1) {
        logger.error(s, o, o1);
        NewRelic.noticeError(s + " " + o.toString() + " " + o1.toString());
    }

    public void error(String s, Object... objects) {
        logger.error(s, objects);
    }

    public void error(String s, Throwable throwable) {
        logger.error(s, throwable);
    }
}
