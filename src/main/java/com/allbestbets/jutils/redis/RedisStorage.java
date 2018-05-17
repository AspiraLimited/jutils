package com.allbestbets.jutils.redis;


import com.allbestbets.jutils.AbbLogger;
import redis.clients.jedis.HostAndPort;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public final class RedisStorage {
    private final static AbbLogger logger = new AbbLogger();

    private final static String NODES_REGEX = "nodes\\.\\d\\.host";
    private final static String PROPERTIES_FILE = "/config/redis.properties";
    private final static String DEFAULT_HOST = "localhost";
    private final static String DEFAULT_PORT = "6379";

    private static iRedis instance;

    public static iRedis redis() {
        iRedis localInstance = instance;

        if (localInstance == null) {
            synchronized (RedisStorage.class) {
                localInstance = instance;
                if (localInstance == null) {
                    Properties properties = new Properties();

                    try (InputStream stream = new FileInputStream(System.getProperty("user.dir") + PROPERTIES_FILE)) {
                        properties.load(stream);
                    } catch (IOException e) {
                        // TODO NewRelic
                        logger.error("Can't load " + PROPERTIES_FILE, e.getCause());
                    }

                    if (Boolean.valueOf(properties.getProperty("cluster"))) {
                        Set<HostAndPort> nodes = new HashSet<>();
                        Pattern pattern = Pattern.compile(NODES_REGEX);
                        Enumeration<?> keys = properties.propertyNames();

                        while (keys.hasMoreElements()) {
                            String key = keys.nextElement().toString();

                            if (pattern.matcher(key).matches()) {
                                String host = properties.getProperty(key);
                                int port = Integer.valueOf(properties.getProperty(key.replaceFirst("host", "port")));

                                nodes.add(new HostAndPort(host, port));
                            }
                        }

                        instance = localInstance = new RedisClusterPooled(nodes);

                    } else {
                        instance = localInstance = new RedisSimplePooled(
                                properties.getProperty("host", DEFAULT_HOST),
                                parseInt(properties.getProperty("port", DEFAULT_PORT)));
                    }
                }
            }
        }
        return localInstance;
    }

    // TODO need ?
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//    }
}
