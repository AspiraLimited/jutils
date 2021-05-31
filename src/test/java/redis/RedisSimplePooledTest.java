package redis;

import com.aspiralimited.jutils.redis.RedisSimplePooled;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class RedisSimplePooledTest {

    @Test
    public void hgetallTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test");
        redis.hset("test", "key1", "value1");
        Map<String, String> all = redis.hgetall("test");
        Assert.assertEquals("value1", all.get("key1"));

        redis.hdel("test", "key1");
        all = redis.hgetall("test");
        Assert.assertNull(all.get("key1"));

        redis.del("test");
    }

    @Test
    public void hscanTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test:1");
        redis.del("test:2");
        redis.del("test:3");
        redis.setex("test:1", 60, "1");
        redis.setex("test:2", 60, "1");
        redis.setex("test:3", 60, "1");

        Set<String> list = redis.scan("test:*");

        Assert.assertEquals(3, list.size());
    }

    @Test
    public void hincrByTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_hincr_by");
        redis.hincrBy("test_hincr_by", "key1", 22);
        String v = redis.hget("test_hincr_by", "key1");

        Assert.assertEquals(22, parseInt(v));

        Set<String> keys = redis.hkeys("test_hincr_by");
        Assert.assertEquals(1, keys.size());

        Assert.assertTrue(keys.contains("key1"));
    }
}
