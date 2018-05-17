package com.allbestbets.jutils;

import java.util.Collection;

public class CollectionUtil {

    public static String join(Collection collection, String sep) {
        StringBuilder builder = new StringBuilder();

        boolean first = true;

        for (Object object : collection) {
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
