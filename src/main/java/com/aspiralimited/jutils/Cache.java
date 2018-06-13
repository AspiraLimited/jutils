package com.aspiralimited.jutils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.aspiralimited.jutils.MapUtil.newMap;
import static java.lang.System.currentTimeMillis;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Cache<K, T> {
    private final static AtomicLong idGenerator = new AtomicLong(0);

    public final static long ONE_MINUTES = TimeUnit.MINUTES.toMillis(1);
    public final static long FIVE_MINUTES = TimeUnit.MINUTES.toMillis(5);
    public final static long TEN_MINUTES = TimeUnit.MINUTES.toMillis(10);
    public final static long THIRTY_MINUTES = TimeUnit.MINUTES.toMillis(30);
    public final static long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    public final static long ONE_DAY = TimeUnit.DAYS.toMillis(1);

    private final Map<K, CacheObject<T>> memory = new ConcurrentHashMap<>();

    private final long id = idGenerator.incrementAndGet();
    private final String name;
    private final int maxItems;
    private final long defaultTTL;
    private final boolean availableNull;

    private final static Map<Long, Cache> cacheList = new ConcurrentHashMap<>();

    static {
//        scheduleAtFixedRate(() -> {
//            long nowTime = currentTimeMillis();
//            for (Cache cache : cacheList.values()) cache.cleanup(nowTime);
//        }, 10, 10);
    }

    public Cache(String name, int maxItems, long defaultTTL) {
        this(name, maxItems, defaultTTL, false);
    }

    public Cache(String name, int maxItems, long defaultTTL, boolean availableNull) {
        this(name, maxItems, defaultTTL, availableNull, true);
    }

    public Cache(String name, int maxItems, long defaultTTL, boolean availableNull, boolean saveInCacheList) {
        this.name = name;
        this.maxItems = maxItems;
        this.defaultTTL = defaultTTL;
        this.availableNull = availableNull;
        if (saveInCacheList) cacheList.put(id, this);
    }

    // Cache methods

    public T cache(K key, Supplier<T> supplier) {
        return cache(key, defaultTTL, supplier);
    }

    public T cache(K key, T object) {
        return cache(key, defaultTTL, object);
    }

    public T cache(K key, long ttl, Supplier<T> supplier) {
        return cache(key, ttl, supplier.get());
    }

    public T cache(K key, long ttl, T object) {
        setex(key, ttl, object);
        return object;
    }

    // Fetch methods

    public T fetch(K key, Supplier<T> supplier) {
        return fetch(key, supplier, false);
    }

    public T fetch(K key, Supplier<T> supplier, boolean force) {
        if (force) remove(key);
        return fetch(key, defaultTTL, supplier);
    }

    public T fetch(K key, long ttl, Supplier<T> supplier) {
        T value = get(key);

        if (value == null) {
            value = supplier.get();
            if (!availableNull && value == null) return null;
            setex(key, ttl, value);
        }

        return value;
    }

    // TODO review
    private CacheObject<T> setex(K key, long ttl, T value) {
        if (ttl <= 0)
            throw new Error("Wrong ttl = '" + ttl + "' for object '" + String.valueOf(value) + "'");

        if (!availableNull && value == null)
            throw new Error("Wrong value; Value can not be NULL");

        CacheObject<T> object = new CacheObject<>(value, ttl);
        memory.put(key, object);
        return object;
    }

    public static void cleanupAll() {
        long nowTime = currentTimeMillis();
        for (Cache cache : cacheList.values()) cache.cleanup(nowTime);
    }

    public static void clearAll() {
        cacheList.values().forEach(Cache::clear);
    }

    public void clear() {
        memory.values().forEach(object -> {
            T val;
            if (object == null || (val = object.value()) == null || val.getClass() != Cache.class) return;
            ((Cache) val).destroy();
        });
        memory.clear();
    }

    // delegate methods

    public int size() {
        return memory.size();
    }

    public boolean isEmpty() {
        return memory.isEmpty();
    }

    public boolean containsKey(K key) {
        return memory.containsKey(key);
    }

    public Set<K> keySet() {
        return memory.keySet();
    }

    public Collection<T> values() {
        long now = currentTimeMillis();
        return memory.values().stream().map(x -> x.value(now)).filter(Objects::nonNull).collect(toList());
    }

    public void forEach(BiConsumer<? super K, ? super T> action) {
        Objects.requireNonNull(action);
        long now = currentTimeMillis();
        for (Map.Entry<K, CacheObject<T>> entry : memory.entrySet()) {
            K k;
            T v;
            try {
                k = entry.getKey();
                v = entry.getValue().value(now);
                if (v == null) continue;
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    public Collection<K> aliveKeys() {
        return memory.keySet().stream().filter(k -> k != null && get(k) != null).collect(toList());
    }

    public T get(K key) {
        if (key == null)
            throw new RuntimeException("Cache key can not be NULL");

        CacheObject<T> object = memory.get(key);

        if (object == null) return null;

        T val = object.value();
        if (availableNull || val != null)
            return object.value();

        remove(key);
        return null;
    }

    public T remove(K key) {
        CacheObject<T> object = memory.remove(key);

        T val = null;
        if (object != null && (val = object.value()) != null && val.getClass() == Cache.class)
            ((Cache) val).destroy();

        return object == null ? null : val;
    }

    private void destroy() {
        clear();
        cacheList.remove(id);
    }

    private void cleanup(Long nowTime) {
        // Clear expired keys
        memory.entrySet().stream()
                .filter(x -> x.getValue().value(nowTime) == null)
                .map(Map.Entry::getKey)
                .collect(toList())
                .forEach(this::remove);

        // Clear oversize
        long oversize = memory.size() - maxItems;

        if (oversize > 0) {
            memory.entrySet()
                    .stream()
                    .sorted(comparingLong(o -> o.getValue().ttl()))
                    .limit(oversize)
                    .forEach(entry -> remove(entry.getKey()));
        }
    }

    public static Map<String, Object> toMap() {
        Map<String, Object> result = MapUtil.newMap("cacheList.size()", cacheList.size());

        cacheList.values().stream().collect(groupingBy(x -> x.name)).forEach((name, caches) -> {
            if (caches.size() <= 1) {
                result.put(name, "" + caches.get(0).size() + " of " + caches.get(0).maxItems);

            } else {
                int count = 0;
                int total = 0;
                long maxItems = 0;

                for (Cache cache : caches) {
                    count++;
                    total = total + cache.size();
                    maxItems = cache.maxItems;
                }

                List<String> items = caches.stream().map(x -> "" + x.size() + " of " + x.maxItems).collect(toList());
                result.put(name, "total " + total + "; avg " + ((float) total / count) + " of " + maxItems);
            }
        });

        return result;
    }

    public static class CacheObject<T> {
        private long ttl;
        private T value;

        CacheObject(T value, long ttl) {
            this.value = value;
            this.ttl = currentTimeMillis() + ttl;
        }

        public T value() {
            return value;
        }

        public T value(long now) {
            if (value == null || ttl >= now) return value;

            if (value.getClass() == Cache.class) {
                ((Cache) value).destroy();
                value = null;
            }

            return null;
        }

        long ttl() {
            return ttl;
        }
    }
}
