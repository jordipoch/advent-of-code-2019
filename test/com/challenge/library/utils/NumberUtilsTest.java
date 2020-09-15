package com.challenge.library.utils;

import org.testng.annotations.Test;

import static com.challenge.library.utils.NumberUtils.mcd;
import static org.testng.Assert.*;

public class NumberUtilsTest {

    @Test
    public void testMcd() {
        assertEquals(mcd(42, 56), 14);
        assertEquals(mcd(56, 42), 14);
        assertEquals(mcd(15, 20), 5);
        assertEquals(mcd(-42, -56), 14);
        assertEquals(mcd(16, 21), 1);
        assertEquals(mcd(7, 7), 7);
        assertEquals(mcd(0, 0), 0);
    }
}