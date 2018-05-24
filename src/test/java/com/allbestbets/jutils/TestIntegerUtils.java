package com.allbestbets.jutils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TestIntegerUtils {
    @Test
    public void toSourceBits() {
        Assert.assertEquals(new ArrayList<Integer>() {{
            add(2);
            add(5);
            add(6);
        }}, IntegerUtils.toSourceBits(100));
    }

    @Test
    public void generateMask() {
        long[] masks = BitMasks.generateMask(new Short[]{20, 30});
        Assert.assertEquals("1074790400,0", masks[0] + "," + masks[1]);

        masks = BitMasks.generateMask(new Short[]{15, 33, 40, 33});
        Assert.assertEquals("1108101595136,0", masks[0] + "," + masks[1]);

        String maskString = BitMasks.generateMaskString(new ArrayList<Long>() {{
            add(15L);
            add(33L);
            add(40L);
            add(33L);
        }});
        Assert.assertEquals("1108101595136,0", maskString);
    }
}
