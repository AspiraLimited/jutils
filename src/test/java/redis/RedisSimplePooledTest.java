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
    public void scanTest() {
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

    @Test
    public void hsetTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_hset");
        redis.hset("test_hset", "key1", "value1");
        String v = redis.hget("test_hset", "key1");

        Assert.assertEquals("value1", v);
    }

    @Test
    public void hdelTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_hdel");
        redis.hset("test_hdel", "key1", "value1");
        redis.hdel("test_hdel", "key1", "key2");
        String v = redis.hget("test_hdel", "key1");

        Assert.assertNull(v);
    }

    @Test
    public void hgetTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_hget");
        redis.hset("test_hget", "key1", "value1");
        String v = redis.hget("test_hget", "key1");

        Assert.assertEquals("value1", v);
    }

    @Test
    public void hkeysTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_hkeys");
        redis.hset("test_hkeys", "key1", "value1");
        Set<String> keys = redis.hkeys("test_hkeys");

        Assert.assertEquals(1, keys.size());
        Assert.assertTrue(keys.contains("key1"));
    }

    @Test
    public void setexTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_setex");
        redis.setex("test_setex", 60, "1");
        String v = redis.get("test_setex");
        Assert.assertEquals("1", v);
    }

    @Test
    public void hdelMultipleFieldsTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_hdel_multi");

        redis.hset("test_hdel_multi", "key1", "value1");
        redis.hset("test_hdel_multi", "key2", "value2");
        redis.hset("test_hdel_multi", "key3", "value3");

        redis.hdel("test_hdel_multi", "key1", "key2");

        Assert.assertNull(redis.hget("test_hdel_multi", "key1"));
        Assert.assertNull(redis.hget("test_hdel_multi", "key2"));
        Assert.assertEquals("value3", redis.hget("test_hdel_multi", "key3"));
    }

    @Test
    public void hdelNonExistingFieldTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_hdel_missing");

        redis.hset("test_hdel_missing", "key1", "value1");

        // Should not throw
        redis.hdel("test_hdel_missing", "missing_key");

        Assert.assertEquals("value1", redis.hget("test_hdel_missing", "key1"));
    }

    @Test
    public void hdelOnEmptyHashTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_hdel_empty");

        // Should not throw
        redis.hdel("test_hdel_empty", "key1");

        Map<String, String> all = redis.hgetall("test_hdel_empty");
        Assert.assertTrue(all.isEmpty());
    }

    @Test
    public void hdelCompatibilityRegressionTest() {
        RedisSimplePooled redis = new RedisSimplePooled();
        redis.del("test_hdel_regression");

        redis.hset("test_hdel_regression", "key1", "value1");

        // This used to throw NoSuchMethodError with mismatched Jedis
        redis.hdel("test_hdel_regression", "key1");

        Assert.assertNull(redis.hget("test_hdel_regression", "key1"));
    }

}
