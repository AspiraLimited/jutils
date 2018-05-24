//package com.allbestbets.jutils.old;
//
//import com.allbestbets.jutils.old.apm.APMLogger;
//import com.allbestbets.jutils.old.apm.IAPMLogger;
//import org.apache.commons.lang.exception.ExceptionUtils;
//import org.apache.log4j.Logger;
//
//import java.sql.SQLException;
//
//public class ABBLogger {
//
//    private Logger log4jlogger;
//    private IAPMLogger apmLogger;
//    private String className;
//    private String prefix;
//
//    public static ABBLogger getLogger() {
//        return new ABBLogger((new Throwable()).getStackTrace()[1].getClassName());
//    }
//
//    public static ABBLogger getLogger(Class clazz) {
//        return new ABBLogger(clazz.getSimpleName());
//    }
//
//    protected ABBLogger(String name) {
//        className = name;
//        log4jlogger = Logger.getLogger(name);
//        apmLogger = APMLogger.instance();
//    }
//
//    protected ABBLogger(Class clazz) {
//        className = clazz.getSimpleName();
//        log4jlogger = Logger.getLogger(clazz.getSimpleName());
//        apmLogger = APMLogger.instance();
//    }
//
//    public void error(Object message, Throwable t) {
//        log4jlogger.error(buildMessage("error", message, null), t);
//        apmLogger.error(buildMessage("error", message, t));
//    }
//
//    public void error(Object message) {
//        String msg = buildMessage("error", message, null);
//        log4jlogger.error(msg);
//        apmLogger.error(msg);
//    }
//
//    public void fatal(Object message, Throwable t) {
//        log4jlogger.fatal(buildMessage("fatal", message, null), t);
//        apmLogger.fatal(buildMessage("fatal", message, t));
//    }
//
//    public void fatal(Object message) {
//        String msg = buildMessage("fatal", message, null);
//        log4jlogger.fatal(msg);
//        apmLogger.fatal(msg);
//    }
//
//    public void info(Object message, Throwable t) {
//        log4jlogger.info(buildMessage("info", message, null), t);
//    }
//
//    public void info(Object message) {
//        log4jlogger.info(buildMessage("info", message, null));
//    }
//
//    public void debug(Object message, Throwable t) {
//        log4jlogger.debug(buildMessage("debug", message, null), t);
//    }
//
//    public void debug(Object message) {
//        log4jlogger.debug(buildMessage("debug", message, null));
//    }
//
//    public String buildMessage(String type, Object message, Throwable t) {
//        String msg = prefix == null ? "" : prefix;
//        msg = msg + (message == null ? "" : message.toString());
//
//        final StringBuilder sb = new StringBuilder().
//                append("[").
//                append(type.toUpperCase()).
//                append("]").
//                append(" : ").
//                append(msg);
//
//        if (t != null) {
//            sb.append(" Stacktrace: ").append(ExceptionUtils.getStackTrace(t));
//
//        } else if (message instanceof Throwable) {
//            sb.append(" Stacktrace: ").append(prefix == null ? ExceptionUtils.getStackTrace((Throwable) message) : prefix + ExceptionUtils.getStackTrace((Throwable) message));
//
//        }
//
//        return sb.toString();
//    }
//
//    public void trace(String s) {
//        log4jlogger.trace(s);
//    }
//
//    public void warn(String s) {
//        log4jlogger.warn(s);
//    }
//
//    public void warn(SQLException e) {
//        log4jlogger.warn(e);
//    }
//
//    public String getPrefix() {
//        return prefix;
//    }
//
//    public void setPrefix(String prefix) {
//        this.prefix = prefix;
//    }
//
//    public Logger getLog4jlogger() {
//        return log4jlogger;
//    }
//
//    public void setLog4jlogger(Logger log4jlogger) {
//        this.log4jlogger = log4jlogger;
//    }
//}
