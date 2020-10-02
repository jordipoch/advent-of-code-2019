package com.challenge.library.utils;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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

    public static int mcd(int int1, int int2) {
        if (int1 < 0 || int2 < 0) {
            return mcd(Math.abs(int1), Math.abs(int2));
        }
        if (int1 == int2) {
            return int1;
        }
        if (int1 == 0) {
            return int2;
        } else if (int2 == 0){
            return int1;
        }

        int max = Math.max(int1, int2);
        int min = Math.min(int1, int2);

        return mcd(min, max % min);
    }

    public static String format(double d) {
        final DecimalFormat decimalFormat = new DecimalFormat("###.####", new DecimalFormatSymbols(Locale.ENGLISH));
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(d);
    }
}
