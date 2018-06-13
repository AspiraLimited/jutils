package com.aspiralimited.jutils.old.apm;

public interface IAPMLogger {
    void error(Throwable t);

    void error(String error);

    void error(String error, Throwable t);

    void fatal(Throwable t);

    void fatal(String error);

    void fatal(String error, Throwable t);

    void info(Throwable t);

    void info(String error);

    void info(String error, Throwable t);

    void debug(Throwable t);

    void debug(String error);

    void debug(String error, Throwable t);
}
