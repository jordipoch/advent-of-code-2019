package com.challenge.library.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

public class NumberUtils {
    private NumberUtils() {}

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

    public static int sign(int i) {
        if (i < 0) {
            return -1;
        } else {
            return i > 0 ? 1 : 0;
        }
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

    public static long lcm(long... args) {
        long[] numbers = prepareArrayAndValidate(args);
        long lcm = numbers[0];

        boolean lcmFound = false;
        while (!lcmFound) {
            lcmFound = checkIfLcmFound(lcm, numbers);
            if (!lcmFound) {
                lcm += numbers[0];
            }
        }

        return lcm;
    }

    private static long[] prepareArrayAndValidate(long... numbers) {
        if (numbers.length < 2) {
            throw new IllegalArgumentException("At least 2 numbers are expected.");
        }

        long[] copy = new long[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 0) {
                throw new IllegalArgumentException("Can't calculate lcm if any of the numbers is 0.");
            } else {
                copy[i] = Math.abs(numbers[i]);
            }
        }

        Arrays.sort(copy);
        ArrayUtils.reverse(copy);

        return copy;
    }

    private static boolean checkIfLcmFound(long lcm, long[] numbers) {
        for (int i = 1; i < numbers.length; i++) {
            if (lcm % numbers[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static String format(double d) {
        final DecimalFormat decimalFormat = new DecimalFormat("###.####", new DecimalFormatSymbols(Locale.ENGLISH));
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(d);
    }
}
