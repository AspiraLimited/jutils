package com.aspiralimited.jutils.redis;

import com.aspiralimited.jutils.logger.AbbLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisSimplePooled implements iRedis {
    private final static AbbLogger logger = new AbbLogger();

    private JedisPool redisPool;
    private final static ObjectMapper MAPPER = new ObjectMapper().registerModule(new Jdk8Module());

    private String host;
    private int port;

    public RedisSimplePooled() {
        redisPool = new JedisPool();
    }

    RedisSimplePooled(String host, int port) {
        this.host = host;
        this.port = port;
        redisPool = new JedisPool(new JedisPoolConfig(), host, port);
    }

    @Override
    public String set(String key, String value) {
        try (Jedis connection = getResource()) {
            return connection.set(key, value);
        }
    }

    @Override
    public String setObject(String key, Object value) {
        String json = null;
        try {
            json = MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            logger.error("error by setObject({}, {})", key, value, e);
        }

        return set(key, json);
    }

    @Override
    public String get(String key) {
        try (Jedis connection = getResource()) {
            return connection.get(key);
        }
    }

    @Override
    public Object get(String key, Class clazz) {
        String json = get(key);

        if (json == null) return null;

        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("error by get({}, {})", key, clazz, e);

            return null;
        }
    }

    @Override
    public Boolean exists(String key) {
        try (Jedis connection = getResource()) {
            return connection.exists(key);
        }
    }

    @Override
    public String setex(String key, int seconds, String value) {
        try (Jedis connection = getResource()) {
            return connection.setex(key, seconds, value);
        }
    }

    @Override
    public String setex(String key, int seconds, Object value) {
        try (Jedis connection = getResource()) {
            String json = MAPPER.writeValueAsString(value);
            return connection.setex(key, seconds, json);
        } catch (JsonProcessingException e) {
            logger.error("error by setex({}, {}, {})", key, value, value, e);
        }

        return null;
    }

    @Override
    public Long sadd(String key, String... members) {
        try (Jedis connection = getResource()) {
            return connection.sadd(key, members);
        }
    }

    @Override
    public Long sadd(String key, int ttl, String... member) {
        try (Jedis connection = getResource()) {
            Long result = connection.sadd(key, member);
            connection.expire(key, ttl);
            return result;
        }
    }

    @Override
    public Long incr(String key) {
        try (Jedis connection = getResource()) {
            return connection.incr(key);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        try (Jedis connection = getResource()) {
            return connection.smembers(key);
        }
    }

    @Override
    public Long srem(String key, String... members) {
        try (Jedis connection = getResource()) {
            return connection.srem(key, members);
        }
    }

    @Override
    public List<String> mget(String... keys) {
        try (Jedis connection = getResource()) {
            return connection.mget(keys);
        }
    }

    @Override
    public Long del(String... keys) {
        try (Jedis connection = getResource()) {
            return connection.del(keys);
        }
    }

    @Override
    public Long del(String key) {
        try (Jedis connection = getResource()) {
            return connection.del(key);
        }
    }

    @Override
    public void rpush(String key, String... member) {
        try (Jedis connection = getResource()) {
            connection.rpush(key, member);
        }
    }

    @Override
    public void rpush(String key, Object result, int seconds) {
        try (Jedis connection = getResource()) {
            String json = MAPPER.writeValueAsString(result);
            connection.rpush(key, json);
            connection.expire(key, seconds);

        } catch (JsonProcessingException e) {
            logger.error("error by rpush({}, {}, {})", key, result, seconds, e);
        }
    }

    @Override
    public String type(String key) {
        try (Jedis connection = getResource()) {
            return connection.type(key);
        }
    }

    @Override
    public String rpop(String key) {
        try (Jedis connection = getResource()) {
            return connection.rpop(key);
        }
    }

    @Override
    public Object rpop(String key, Class clazz) {
        try (Jedis connection = getResource()) {
            String json = connection.rpop(key);

            if (json == null) return null;

            try {
                return MAPPER.readValue(json, clazz);
            } catch (IOException e) {
                logger.error("error by rpop({}, {})", key, clazz, e);

                return null;
            }
        }
    }

    @Override
    public List<String> lrange(final String key, final long start, final long end) {
        try (Jedis connection = getResource()) {
            return connection.lrange(key, start, end);
        }
    }

    @Override
    public String ltrim(String key, long start, long end) {
        try (Jedis connection = getResource()) {
            return connection.ltrim(key, start, end);
        }
    }

    @Override
    public Long llen(String key) {
        try (Jedis connection = getResource()) {
            return connection.llen(key);
        }
    }

    @Override
    public long ttl(String key) {
        try (Jedis connection = getResource()) {
            return connection.ttl(key);
        }
    }

    @Override
    public long lpush(String key, String... string) {
        try (Jedis connection = getResource()) {
            return connection.lpush(key, string);
        }
    }

    @Override
    public String lpop(String key) {
        try (Jedis connection = getResource()) {
            return connection.lpop(key);
        }
    }

    private Jedis getResource() {
        try {
            return redisPool.getResource();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof JedisConnectionException && canReconnect()) {
                if (host != null) {
                    redisPool = new JedisPool(new JedisPoolConfig(), host, port);
                } else {
                    redisPool = new JedisPool();
                }
                return getResource();
            }
            throw e;
        }
    }

    @Override
    public Long expire(String key, int seconds) {
        try (Jedis connection = getResource()) {
            return connection.expire(key, seconds);
        }
    }

    @Override
    public Set<String> scan(String pattern) {
        Set<String> res = new HashSet<>();
        try (Jedis connection = getResource()) {

            ScanParams scanParams = new ScanParams().count(1000).match(pattern);
            String i = "0";

            do {
                ScanResult<String> sr = connection.scan(i, scanParams);
                if (!sr.getResult().isEmpty())
                    res.addAll(sr.getResult());

                i = sr.getCursor();

            } while (!i.equals("0"));
        }

        return res;
    }

    @Override
    public Map<String, String> hgetall(String key) {
        try (Jedis connection = getResource()) {
            return connection.hgetAll(key);
        }
    }

    @Override
    public void hset(String key, String hashKey, String hashValue) {
        try (Jedis connection = getResource()) {
            connection.hset(key, hashKey, hashValue);
        }
    }

    @Override
    public void hdel(String key, String... fields) {
        try (Jedis connection = getResource()) {
            for (String field : fields) {
                connection.hdel(key, field);
            }
        }
    }

    @Override
    public void hincrBy(String key, String hashKey, long value) {
        try (Jedis connection = getResource()) {
            connection.hincrBy(key, hashKey, value);
        }
    }

    @Override
    public String hget(String key, String hashKey) {
        try (Jedis connection = getResource()) {
            return connection.hget(key, hashKey);
        }
    }

    @Override
    public Set<String> hkeys(String key) {
        try (Jedis connection = getResource()) {
            return connection.hkeys(key);
        }
    }
}
