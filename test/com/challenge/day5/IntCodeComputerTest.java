package com.challenge.day5;

import static com.challenge.day5.IntCodeComputer.Builder.*;

import com.challenge.day5.exception.TestComputationException;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class IntCodeComputerTest {
    @Test
    public void runTestOk1_1DiagnosticTest() throws TestComputationException {
        final long[] code = {3, 3, 101, 0, 6, 6, 1092, 5, 5, 9, 4, 9, 99};
        final long input = 10;

        System.out.println("Code before running test diagnostic program: " + Arrays.toString(code));

        IntCodeComputer intCodeComputer = createIntCodeComputer(code).withInput(input).build();
        List<Long> output = intCodeComputer.computeTests();

        assertEquals(output.size(), 1);
        assertEquals(output.get(0).longValue(), 25L);
        assertTrue(Arrays.equals(code, new long[] {3, 3, 101, 10, 6, 6, 1102, 5, 5, 25, 4, 9, 99}));

        System.out.println("Code after running test diagnostic program: " + Arrays.toString(code));
    }

    @Test
    public void runTestOk2_2DiagnosticTests() throws TestComputationException {
        final long[] code = {
                //  0  1  2    3  4  5  6     7  8  9 10 11
                    3, 3, 101, 0, 6, 6, 1092, 5, 5, 9, 4, 9,
                //    12 13  14  15   16   17  18  19 20  21 22  23  24
                    1002, 9, -5, 23, 101, 224, 23, 22, 4, 22, 0, 12, 99};
        final long input = 10;

        System.out.println("Code before running test diagnostic program: " + Arrays.toString(code));

        IntCodeComputer intCodeComputer = createIntCodeComputer(code).withInput(input).build();
        List<Long> output = intCodeComputer.computeTests();

        assertEquals(output.size(), 2);
        assertEquals(output.get(0).longValue(), 25L);
        assertEquals(output.get(1).longValue(), 99L);
        assertTrue(Arrays.equals(code, new long[] {
                3, 3, 101, 10, 6, 6, 1102, 5, 5, 25, 4, 9,
                1002, 9, -5, 23, 101, 224, 23, 22, 4, 22, 99, -125, 99}));

        System.out.println("Code after running test diagnostic program: " + Arrays.toString(code));
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.TestComputationException.class})
    public void runTestKo_NoHaltInstruction() throws TestComputationException {
        final long[] code = {3, 3, 101, 0, 6, 6, 1092, 5, 5, 9, 4, 9};
        final long input = 10;

        IntCodeComputer intCodeComputer = createIntCodeComputer(code).withInput(input).build();
        intCodeComputer.computeTests();
    }
}
