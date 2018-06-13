//package com.aspiralimited.jutils.old.apm;
//
//import com.newrelic.api.agent.NewRelic;
//import org.apache.commons.lang.exception.ExceptionUtils;
//
//public class NewRelicLogger implements IAPMLogger {
//
//    @Override
//    public void error(Throwable t) {
//        NewRelic.noticeError(t.getMessage() + ":" + ExceptionUtils.getStackTrace(t));
//    }
//
//    @Override
//    public void error(String error) {
//        NewRelic.noticeError(error);
//    }
//
//    @Override
//    public void error(String error, Throwable t) {
//        NewRelic.noticeError(error + ":" + ExceptionUtils.getStackTrace(t));
//    }
//
//    @Override
//    public void fatal(Throwable t) {
//        NewRelic.noticeError(t);
//    }
//
//    @Override
//    public void fatal(String error) {
//        NewRelic.noticeError(error);
//    }
//
//    @Override
//    public void fatal(String error, Throwable t) {
//        NewRelic.noticeError(error + ":" + ExceptionUtils.getStackTrace(t));
//    }
//
//    @Override
//    public void info(Throwable t) {
//        NewRelic.noticeError(t.getMessage() + ":" + ExceptionUtils.getStackTrace(t));
//    }
//
//    @Override
//    public void info(String error) {
//        NewRelic.noticeError(error);
//    }
//
//    @Override
//    public void info(String error, Throwable t) {
//        NewRelic.noticeError(t);
//    }
//
//    @Override
//    public void debug(Throwable t) {
//        NewRelic.noticeError(t.getMessage() + ":" + ExceptionUtils.getStackTrace(t));
//    }
//
//    @Override
//    public void debug(String error) {
//        NewRelic.noticeError(error);
//    }
//
//    @Override
//    public void debug(String error, Throwable t) {
//        NewRelic.noticeError(t);
//    }
//}