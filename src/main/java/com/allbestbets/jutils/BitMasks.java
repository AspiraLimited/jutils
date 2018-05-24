package com.allbestbets.jutils;

import java.util.List;

public class BitMasks {
    private static final int BIT_SIZE = 63;

    public static long[] generateMask(Short[] ids) {
        int index;
        long[] masks = {0, 0};

        for (Short id : ids) {
            if (id == 0) continue;

            index = id / BIT_SIZE;
            if (index > 1) continue;

            masks[index] = masks[index] | (long) Math.pow(2, id % BIT_SIZE);
        }

        return masks;
    }

    public static String generateMaskString(List<Long> values) {
        final Short[] ids = new Short[values.size()];

        for (int i = 0; i < values.size(); i++)
            ids[i] = values.get(i).shortValue();

        long[] masks = generateMask(ids);

        return masks[0] + "," + masks[1];
    }
}
