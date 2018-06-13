package com.aspiralimited.jutils;

import java.util.ArrayList;
import java.util.List;

public class IntegerUtils {

    @Deprecated
    public static List<Integer> toSourceBits(int value) {
        final List<Integer> result = new ArrayList<>();

        for (int i = 0; i < 63; i++) {
            long bit = (long) Math.pow(2, i);

            if (bit > value || (value & bit) == 0) continue;

            result.add(i);
        }

        return result;
    }
}
