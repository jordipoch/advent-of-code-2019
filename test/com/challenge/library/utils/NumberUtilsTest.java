package com.challenge.library.utils;

import org.testng.annotations.Test;

import static com.challenge.library.utils.NumberUtils.mcd;
import static com.challenge.library.utils.NumberUtils.lcm;
import static com.challenge.library.utils.NumberUtils.sign;
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

    @Test
    public void testLcmNoError2Params() {
        assertEquals(lcm(2, 5), 10);
        assertEquals(lcm(3, 4), 12);
        assertEquals(lcm(1, 8), 8);
        assertEquals(lcm(-9, 4), 36);
        assertEquals(lcm(48, 80), 240);
    }

    @Test
    public void testLcmNoError3Params() {
        assertEquals(lcm(6, 10, 15), 30);
        assertEquals(lcm(9, 12, -24), 72);
        assertEquals(lcm(18, 28, 44), 2772);
        assertEquals(lcm(2028, 4702, 5898), 4_686_774_924L);
    }

    @Test(expectedExceptions = {java.lang.IllegalArgumentException.class})
    public void testLcmError1Params() {
        lcm(1);
    }

    @Test(expectedExceptions = {java.lang.IllegalArgumentException.class})
    public void testLcmErrorValue0() {
        lcm(64, 0, 12);
    }

    @Test(expectedExceptions = {java.lang.IllegalArgumentException.class})
    public void test2LcmErrorValue0() {
        lcm(0, 0);
    }

    @Test
    public void testSign() {
        assertEquals(sign(0), 0);
        assertEquals(sign(15), 1);
        assertEquals(sign(-47), -1);
    }
}