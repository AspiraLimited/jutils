package com.aspiralimited.jutils.redis;

import com.aspiralimited.jutils.AbbLogger;
import com.aspiralimited.jutils.Cache;

import static java.util.concurrent.TimeUnit.SECONDS;

public interface ExternalStorage {

    AbbLogger logger = new AbbLogger();

    String RETRY_KEY = "retryKey";
    int MAX_RETRY_COUNT = 1;
    Cache<String, Integer> restartCount = new Cache<>("RedisRetryCount", 1, SECONDS.toMillis(10));

    default boolean canReconnect() {
        Integer count = restartCount.get(RETRY_KEY);

        if (count == null) {
            count = 1;
        } else {
            count++;
        }

        if (count <= MAX_RETRY_COUNT) {
            restartCount.cache(RETRY_KEY, count);
            logger.error("Problems with redis! Try to reconnect!");
            return true;
        }

        return false;
    }
}
