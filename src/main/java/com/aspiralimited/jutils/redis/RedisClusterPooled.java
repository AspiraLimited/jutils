package com.aspiralimited.jutils.redis;

import com.aspiralimited.jutils.logger.AbbLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;


public class RedisClusterPooled implements iRedis {
    private final static AbbLogger logger = new AbbLogger();

    private final static ObjectMapper MAPPER = new ObjectMapper().registerModule(new Jdk8Module());

    private JedisCluster cluster;
    private final Set<HostAndPort> nodes;

    RedisClusterPooled(Set<HostAndPort> nodes) {
        cluster = new JedisCluster(nodes);
        this.nodes = nodes;
    }

    @Override
    public String set(String key, String value) {
        return execute(() -> cluster.set(key, value));
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
        return execute(() -> cluster.get(key));
    }

    @Override
    public Object get(String key, Class clazz) {
        String json = get(key);

        if (json == null) return null;

        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("error by get({}, {})", key, clazz, e);

            return null;
        }
    }

    @Override
    public Boolean exists(String key) {
        return execute(() -> cluster.exists(key));
    }

    @Override
    public String setex(String key, int seconds, String value) {
        return execute(() -> cluster.setex(key, seconds, value));
    }

    @Override
    public String setex(String key, int seconds, Object value) {
        try {
            String json = MAPPER.writeValueAsString(value);
            return execute(() -> cluster.setex(key, seconds, json));
        } catch (JsonProcessingException e) {
            logger.error("error by setex({}, {}, {})", key, value, value, e);
        }

        return null;
    }

    @Override
    public Long sadd(String key, String... members) {
        return execute(() -> cluster.sadd(key, members));
    }

    @Override
    public Long sadd(String key, int seconds, String... members) {
        return execute(() -> {
            long result = cluster.sadd(key, members);
            cluster.expire(key, seconds);
            return result;
        });
    }

    @Override
    public Long incr(String key) {
        return execute(() -> cluster.incr(key));
    }

    @Override
    public Set<String> smembers(String key) {
        return execute(() -> cluster.smembers(key));
    }

    @Override
    public Long srem(String key, String... members) {
        return execute(() -> cluster.srem(key));
    }

    @Override
    @Deprecated
    /*
      @deprecated use scan instead
    */
    public Set<String> keys(String pattern) {
        // Redis cluster не поддерживает keys метод, нужно опросить все ноды
        Set<String> keys = new HashSet<>();
        for (Map.Entry<String, JedisPool> entry :
                execute(() -> cluster.getClusterNodes().entrySet())) {
            try {
                try (Jedis connection = entry.getValue().getResource()) {
                    keys.addAll(connection.keys(pattern));
                }
            } catch (Exception e) {
                logger.error("Error with: " + entry.getKey() + " : " + entry.getValue() + " : " + e.getMessage());
            }
        }

        return keys;
    }

    @Override
    public List<String> mget(String... keys) {
        // Возвращет значения в том порядке в котором ключи были переданы
        List<String> values = new ArrayList<>();

        for (String key : keys)
            values.add(get(key));

        return values;
    }

    @Override
    public Long del(String... keys) {
        long count = 0L;

        for (String key : keys)
            count += del(key);

        return count;
    }

    @Override
    public Long del(String key) {
        return execute(() -> cluster.del(key));
    }

    @Override
    public void rpush(String key, String... member) {
        execute(() -> cluster.rpush(key, member));
    }

    @Override
    public void rpush(String key, Object result, int seconds) {
        try {
            String json = MAPPER.writeValueAsString(result);
            execute(() -> cluster.rpush(key, json));
            execute(() -> cluster.expire(key, seconds));
        } catch (JsonProcessingException e) {
            logger.error("error by rpush({}, {}, {})", key, result, seconds, e);
        }
    }

    @Override
    public String type(String key) {
        return execute(() -> cluster.type(key));
    }

    @Override
    public String rpop(String key) {
        return execute(() -> cluster.rpop(key));
    }

    @Override
    public Object rpop(String key, Class clazz) {
        String json = execute(() -> cluster.rpop(key));

        if (json == null) return null;

        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("error by rpop({}, {})", key, clazz, e);

            return null;
        }
    }

    @Override
    public List<String> lrange(final String key, final long start, final long end) {
        return execute(() -> cluster.lrange(key, start, end));
    }

    @Override
    public String ltrim(String key, long start, long end) {
        return execute(() -> cluster.ltrim(key, start, end));
    }

    @Override
    public Long llen(String key) {
        return execute(() -> cluster.llen(key));
    }

    @Override
    public long ttl(String key) {
        return cluster.ttl(key);
    }

    @Override
    public long lpush(String key, String... string) {
        return cluster.lpush(key, string);
    }

    @Override
    public String lpop(String key) {
        return cluster.lpop(key);
    }

    private <U> U execute(Supplier<U> func) {
        try {
            return func.get();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof JedisConnectionException && canReconnect()) {
                cluster = new JedisCluster(nodes);
                return execute(func);
            }
            throw e;
        }
    }

    @Override
    public Long expire(String key, int seconds) {
        return execute(() -> cluster.expire(key, seconds));
    }

    @Override
    public Set<String> scan(String pattern) {
        Set<String> res = new HashSet<>();

        ScanParams scanParams = new ScanParams().count(1000).match(pattern);
        String i = "0";

        do {
            ScanResult<String> sr = cluster.scan(i, scanParams);
            if (!sr.getResult().isEmpty())
                res.addAll(sr.getResult());

            i = sr.getCursor();

        } while (!i.equals("0"));

        return res;
    }

    @Override
    public Map<String, String> hgetall(String key) {
        return cluster.hgetAll(key);
    }

    @Override
    public void hset(String key, String hashKey, String hashValue) {
        cluster.hset(key, hashKey, hashValue);
    }

    @Override
    public void hdel(String key, String hashKey) {
        cluster.hdel(key, hashKey);
    }

    @Override
    public void hincrBy(String key, String hashKey, long value) {
        cluster.hincrBy(key, hashKey, value);
    }

    @Override
    public String hget(String key, String hashKey) {
        return cluster.hget(key, hashKey);
    }

    @Override
    public Set<String> hkeys(String key) {
        return cluster.hkeys(key);
    }
}
