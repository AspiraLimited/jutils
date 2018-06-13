package com.aspiralimited.jutils;

import java.util.Collection;

public class CollectionUtil {

    public static String join(Collection collection) {
        return join(collection, ",");
    }

    public static String join(Collection collection, boolean skipNull) {
        return join(collection, ",", skipNull);
    }

    public static String join(Collection collection, String sep) {
        return join(collection, sep, true);
    }

    public static String join(Collection collection, String sep, boolean skipNull) {
        StringBuilder builder = new StringBuilder();

        boolean first = true;

        for (Object object : collection) {
            if (skipNull && object == null) continue;

            if (first) {
                builder.append(object);
                first = false;

            } else {
                builder.append(sep).append(object);
            }
        }

        return builder.toString();
    }
}
