package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.IntComputerException;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.challenge.library.intcodecomputer.IntCodeComputer.Builder.createNewIntCodeComputer;

public class IntCodeComputerTest {
    @Test
    public void testSimpleCode_1() throws IntComputerException {
        long[] code = {3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0};
        System.out.println("Code to execute: " + Arrays.toString(code));

        IntCodeComputer intCodeComputer = createNewIntCodeComputer(code)
                .withInputValue(4)
                .withInputValue(200)
                .build();

        List<BigInteger> output = intCodeComputer.executeCode();

        assertEquals(output.size(), 1);
        System.out.println("Execution result: " + output.get(0));
        assertEquals(output.get(0).longValue(), 2004L);
    }

    @Test
    public void testBaseOffset_1() throws IntComputerException {
        // This code takes no input and produces a copy of itself as output
        long[] code = {109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99};
        System.out.println("Code to execute: " + Arrays.toString(code));

        IntCodeComputer intCodeComputer = createNewIntCodeComputer(code)
                .withMemoryAutoExpand()
                .build();

        List<BigInteger> output = intCodeComputer.executeCode();

        assertEquals(output.size(), code.length);
        System.out.println("Execution result: " + output);
        System.out.println("Memory snapshot" + intCodeComputer.getMemorySnapshot());
    }

    @Test
    public void testBaseOffset_2() throws IntComputerException {
        // This code should output a 16-digit number
        long[] code = {1102,34915192,34915192,7,4,7,99,0};
        System.out.println("Code to execute: " + Arrays.toString(code));

        IntCodeComputer intCodeComputer = createNewIntCodeComputer(code)
                .withMemoryAutoExpand()
                .build();

        List<BigInteger> output = intCodeComputer.executeCode();

        System.out.println("Execution result: " + output);
        System.out.println("Memory snapshot:" + intCodeComputer.getMemorySnapshot());

        assertEquals(output.get(0).toString().length(), 16);
    }

    @Test
    public void testBaseOffset_3() throws IntComputerException {
        // This code should output the large number in the middle.
        String[] code = {"104","1125899906842624","99"};
        BigInteger[] biCode = Arrays.stream(code).map(BigInteger::new).toArray(BigInteger[]::new);

        System.out.println("Code to execute: " + Arrays.toString(biCode));

        IntCodeComputer intCodeComputer = createNewIntCodeComputer(biCode)
                .withMemoryAutoExpand()
                .build();

        List<BigInteger> output = intCodeComputer.executeCode();

        System.out.println("Execution result: " + output);
        System.out.println("Memory snapshot:" + intCodeComputer.getMemorySnapshot());

        assertEquals(output.get(0), biCode[1]);
    }

    @Test
    public void testRunBoostProgramFromDay9_1() throws Exception {
        System.out.print("Running day 9 challenge, part 1...");
        BigInteger output = runBoostProgram(1);
        System.out.println(" Output: " + output); // 4261108180

        assertEquals(output.longValue(), 4261108180L);
    }

    @Test
    public void testRunBoostProgramFromDay9_2() throws Exception {
        System.out.print("Running day 9 challenge, part 2...");
        BigInteger output = runBoostProgram(2);
        System.out.println(" Output: " + output); // 77944

        assertEquals(output.longValue(), 77944L);
    }


    @Test (expectedExceptions = {IntComputerException.class})
    public void testIntCodeComputerError() throws IntComputerException {
        long[] code = {3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0};

        IntCodeComputer intCodeComputer = createNewIntCodeComputer(code)
                .withInputValue(4)
                //.withInputValue(200)
                .build();

        try {
            intCodeComputer.executeCode();
        } catch (IntComputerException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private BigInteger runBoostProgram(int inputValue) throws IOException, IntComputerException {
        BigInteger[] code = IntCodeLoader
                .getInstance()
                .loadBigIntCodeFromFile(Paths.get("resources", "com", "challenge", "library", "intcodecomputer"), "day9_input.txt");

        IntCodeComputer intCodeComputer = createNewIntCodeComputer(code)
                .withInputValue(inputValue)
                .withMemoryAutoExpand()
                .build();

        List<BigInteger> output = intCodeComputer.executeCode();

        return output.get(0);
    }
}
