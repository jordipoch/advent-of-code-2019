package com.challenge.library.utils;

import java.math.BigInteger;

public class NumberUtils {
    public static long convertToLong(BigInteger number) {
        if (number.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 || number.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0)
            throw new IllegalArgumentException(String.format("Number %s is not a valid long value", number));

        return number.longValue();
    }

    public static int convertToInt(BigInteger number) {
        if (number.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0 || number.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0)
            throw new IllegalArgumentException(String.format("Number %s is not a valid int value", number));

        return number.intValue();
    }
}
