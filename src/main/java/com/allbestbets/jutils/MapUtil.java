package com.allbestbets.jutils;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    public static <K, V> Map<K, V> newMap() {
        return new HashMap<>();
    }

    public static <K, V> Map<K, V> newHashMap(Object... objects) {
        HashMap<K, V> result = new HashMap<>();

        for (int i = 0; i < objects.length; i = i + 2) {
            K key = (K) objects[i];
            V value = (V) objects[i + 1];
            result.put(key, value);
        }
        return result;
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0) {
        HashMap<K, V> result = new HashMap<>();
        result.put(key0, value0);
        return result;
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1) {
        return sum(newMap(key0, value0),
                key1, value1);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2) {
        return sum(newMap(key0, value0, key1, value1),
                key2, value2);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3) {
        return sum(newMap(key0, value0, key1, value1, key2, value2),
                key3, value3);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3),
                key4, value4);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3, key4, value4),
                key5, value5);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5),
                key6, value6);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6),
                key7, value7);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7),
                key8, value8);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8),
                key9, value9);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8, key9, value9),
                key10, value10);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10, K key11, V value11) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8, key9, value9, key10, value10),
                key11, value11);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10, K key11, V value11, K key12, V value12) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8, key9, value9, key10, value10, key11, value11),
                key12, value12);
    }

    public static <K, V> Map<K, V> newMap(K key0, V value0, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10, K key11, V value11, K key12, V value12, K key13, V value13) {
        return sum(newMap(key0, value0, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8, key9, value9, key10, value10, key11, value11, key12, value12),
                key13, value13);
    }

    public static <K, V> boolean isSubMap(Map<K, V> child, Map<K, V> parent) {
        return child.entrySet().stream().allMatch(
                entry -> entry.getValue().equals(parent.get(entry.getKey()))
        );
    }

    public static <K, V> boolean isProperSubMap(Map<K, V> child, Map<K, V> parent) {
        return isSubMap(child, parent) && !child.equals(parent);
    }

    public static <K, V> Map<K, V> sum(Map<K, V> map, K addKey, V addValue) {
        Map<K, V> result = new HashMap<>(map);
        result.put(addKey, addValue);
        return result;
    }
}
