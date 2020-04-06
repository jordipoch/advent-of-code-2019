package com.challenge;

public class Day4 {
    Part part;

    public static void main(String[] args) {
        System.out.println("Result: " + new Day4(Part.PART_TWO).doCalculations(193651, 649729));
    }

    public Day4(Part part) {
        this.part = part;
    }

    public int doCalculations(int minRange, int maxRange) {
        int numPwds = 0;
        for (int num = minRange; num <= maxRange; num++) {
            char[] digits = String.valueOf(num).toCharArray();
            if (meetsConditions(digits)) {
                numPwds ++;
                System.out.println("New possible password found: " + num);
            }
        }
        return numPwds;
    }

    private boolean meetsConditions(char[] digits) {
        switch(part) {
            case PART_ONE:
                return hasTwoAdjacentDigits(digits) && digitsNeverDecrease(digits);
            case PART_TWO:
                return hasTwoSingleConsecutiveDigits(digits) && digitsNeverDecrease(digits);
        }

        return false;
    }

    private boolean hasTwoAdjacentDigits(char[] digits) {
        char previousDigit = '_';
        for (int i = 0; i < digits.length; i++) {
            if (digits[i] == previousDigit)
                return true;
            previousDigit = digits[i];
        }

        return false;
    }

    private boolean digitsNeverDecrease(char[] digits) {
        int previousDigit = 0;
        for (int i = 0; i < digits.length; i++) {
            int digit = Character.digit(digits[i], 10);
            if(digit < previousDigit)
                return false;
            previousDigit = digit;
        }

        return true;
    }

    private boolean hasTwoSingleConsecutiveDigits(char[] digits) {
        char previousDigit = '_';
        char skipCharacter = '_';
        for (int i = 0; i < digits.length; i++) {
            if (digits[i] == skipCharacter)
                continue;
            if (digits[i] == previousDigit)
                if (i < digits.length-1 && digits[i] == digits[i+1])
                    skipCharacter = digits[i];
                else
                    return true;

            previousDigit = digits[i];
        }

        return false;
    }

    enum Part { PART_ONE, PART_TWO}
}
