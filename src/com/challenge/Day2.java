package com.challenge;

import java.util.Arrays;

public class Day2 {
    public static void main(String[] args) {
        //runDay2Part1Challenge();
        runDay2Part2Challenge();
        //runTests();
    }

    private static void runDay2Part1Challenge() {
        try {
            long[] intCode = new Day2().runIntCode(restoreTo1202ProgramAlarm(getInput()));
            System.out.println("Final IntCode: " + Arrays.toString(intCode));
            System.out.println("Result: " + intCode[0]);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void runDay2Part2Challenge() {
        Day2 day2 = new Day2();
        final long output = 19690720;
        final int max = 99;

        for(int noun = 0; noun <= max; noun++) {
            for (int verb = 0; verb <= max; verb++) {
                long[] intCode = getInput();
                intCode[1] = noun;
                intCode[2] = verb;

                try {
                    day2.runIntCode(intCode);
                    if (intCode[0] == output) {
                        System.out.println("Answer: " + (100 * noun + verb));
                        return;
                    }
                } catch (Exception e) {
                    System.out.printf("Error found for noun %d and verb %d: %s%n", noun, verb, e.getMessage());
                }
            }
        }

        System.out.println("Sorry, no answer found");

    }


    private static void runTests() {
        try {
            long[] intCode = new Day2().runIntCode(new long[]{1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50});
            System.out.println(Arrays.toString(intCode));

            intCode = new Day2().runIntCode(new long[]{1, 0, 0, 0, 99});
            System.out.println(Arrays.toString(intCode));

            intCode = new Day2().runIntCode(new long[]{2, 3, 0, 3, 99});
            System.out.println(Arrays.toString(intCode));

            intCode = new Day2().runIntCode(new long[]{2, 4, 4, 5, 99, 0});
            System.out.println(Arrays.toString(intCode));

            intCode = new Day2().runIntCode(new long[]{1, 1, 1, 4, 99, 5, 6, 0, 99});
            System.out.println(Arrays.toString(intCode));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private long[] runIntCode(long[] intCode) throws InvalidPositionException, InvalidOperationException {
        boolean halt = false;
        int currentPos = 0;

        while(!halt) {
            halt = runCodeBlock(intCode, currentPos);
            currentPos += 4;
        }

        return intCode;
    }

    private boolean runCodeBlock(long[] intCode, int pos) throws InvalidPositionException, InvalidOperationException {
        boolean halt = false;

        if (intCode[pos] == 99L) {
            halt = true;
        } else {
            validateParams(intCode, pos);

            if (intCode[pos] == 1L) {
                intCode[(int) intCode[pos + 3]] = intCode[(int) intCode[pos + 1]] + intCode[(int) intCode[pos + 2]];
            } else if (intCode[pos] == 2L) {
                intCode[(int) intCode[pos + 3]] = intCode[(int) intCode[pos + 1]] * intCode[(int) intCode[pos + 2]];
            } else {
                // assert false : "Unknown opcode " + intCode[pos] + " at position " + pos;
                throw new InvalidOperationException(pos, intCode[pos]);
            }
        }

        return halt;
    }

    private void validateParams(long[] intCode, int pos) throws InvalidPositionException {
        if (intCode[++pos] >= intCode.length) {
            throw new InvalidPositionException(pos, intCode[pos], intCode.length);
        } else if (intCode[++pos] >= intCode.length) {
            throw new InvalidPositionException(pos, intCode[pos], intCode.length);
        } else if (intCode[++pos] >= intCode.length) {
            throw new InvalidPositionException(pos, intCode[pos], intCode.length);
        }
    }

    private static long[] getInput() {
        return new long[] {
                1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,13,1,19,1,10,19,23,1,6,23,27,1,5,27,31,
                1,10,31,35,2,10,35,39,1,39,5,43,2,43,6,47,2,9,47,51,1,51,5,55,1,5,55,59,2,
                10,59,63,1,5,63,67,1,67,10,71,2,6,71,75,2,6,75,79,1,5,79,83,2,6,83,87,2,13,
                87,91,1,91,6,95,2,13,95,99,1,99,5,103,2,103,10,107,1,9,107,111,1,111,6,115,
                1,115,2,119,1,119,10,0,99,2,14,0,0
        };
    }

    private static long[] restoreTo1202ProgramAlarm(long[] intCode) {
        intCode[1] = 12;
        intCode[2] = 2;

        return intCode;
    }
}

class InvalidPositionException extends Exception {
    InvalidPositionException(int pos, long address, int arrayLength) {
        super(String.format("Found incorrect address %d at position %d of the IntCode (max: %d)", address, pos, arrayLength));
    }
}

class InvalidOperationException extends Exception {
    InvalidOperationException(int pos, long operation) {
        super(String.format("Found incorrect operation %d at position %d", pos, operation));
    }
}
