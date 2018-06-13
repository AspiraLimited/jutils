package com.aspiralimited.jutils;

import java.util.Random;

public class RandUtils {

    private static final Random RANDOM = new Random();

    public static int rand(int max) {
        if (max < 1) return 0;

        return RANDOM.nextInt(max);
    }

    public static int rand(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }
}
