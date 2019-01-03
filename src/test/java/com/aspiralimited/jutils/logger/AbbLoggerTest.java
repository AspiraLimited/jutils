package com.aspiralimited.jutils.logger;

import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Test;

public class AbbLoggerTest {

    @Test
    public void integrationTest() throws InterruptedException {
        AbbLogger logger = new AbbLogger();
        AirbrakeLogger.load(188506, "0d5e86233c8e4131f4895f95bcd35811", "test");
        RollbarLogger.load("c54a13e8bf894a0f983c2dc76ba50326", "test");

        logger.error("test error msg from jutils");

        Thread.sleep(10000);
    }

    @Test
    public void levelTest() {
        Assert.assertTrue(Level.ALL.compareTo(Level.DEBUG) >= 0);
    }
}
