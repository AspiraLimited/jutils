package com.aspiralimited.jutils.redis;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @note Основные методы которые мы используем, добавляются по необходимости.
 */
public interface iRedis extends ExternalStorage {

    String set(final String key, final String value);

    default String set(final String key, final long value) {
        return set(key, String.valueOf(value));
    }

    String set(final String key, final String value, final String nxxx, final String expx, final long time);

    String setObject(final String key, final Object value);

    String setObject(final String key, final Object value, final String nxxx, final String expx, final long time);

    String get(final String key);

    Object get(final String key, Class clazz);

    Boolean exists(final String key);

    String setex(final String key, final int seconds, final String value);

    String setex(final String key, final int seconds, final Object value);

    Long sadd(final String key, final String... member);

    Long sadd(final String key, int seconds, final String... member);

    default Long sadd(final String key, final Set<String> member) {
        return sadd(key, member.toArray(new String[]{}));
    }

    Long incr(final String key);

    Set<String> smembers(final String key);

    Long srem(final String key, final String... members);

    default Long srem(final String key, Set<String> members) {
        return srem(key, members.toArray(new String[]{}));
    }

    Set<String> keys(String pattern);

    List<String> mget(String... keys);

    Long del(String... keys);

    Long del(String key);

    void rpush(String key, final String... member);

    void rpush(String key, Object result, int seconds);

    default void rpush(String key, long value) {
        rpush(key, String.valueOf(value));
    }

    String type(String key);

    String rpop(String key);

    Object rpop(String s, Class clazz);

    List<String> lrange(final String key, final long start, final long end);

    String ltrim(String key, long start, long end);

    Long llen(String key);

    long ttl(String key);

    long lpush(String key, String... string);

    default long lpush(String key, Collection<String> values) {
        String[] _values = values.toArray(new String[values.size()]);
        return lpush(key, _values);
    }

    String lpop(String key);

    Long expire(String key, int seconds);

    Set<String> scan(String pattern);

    Map<String, String> hgetall(String key);

    void hset(String key, String hashKey, String hashValue);

    void hdel(String key, String hashKey);

    void hincrBy(String key, String hashKey, long value);

    String hget(String key, String hashKey);

    Set<String> hkeys(String key);
}
