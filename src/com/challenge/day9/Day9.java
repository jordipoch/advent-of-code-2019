package com.challenge.day9;

import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.IntCodeLoader;
import com.challenge.library.intcodecomputer.exception.IntComputerException;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;

import static com.challenge.library.intcodecomputer.IntCodeComputer.Builder.createNewIntCodeComputer;

public class Day9 {
    public static void main(String[] args) {

        try {
            System.out.print("Running day 9 challenge, part 1...");
            BigInteger output = runDay9(1);
            System.out.println(" Output: " + output); // 4261108180

            System.out.print("Running day 9 challenge, part 2...");
            output = runDay9(2);
            System.out.println(" Output: " + output); // 77944
        } catch (Exception e) {
            System.out.println("Error running day 9 challenge: " + e);
            e.printStackTrace();
        }
    }

    private static BigInteger runDay9(int inputValue) throws IOException, IntComputerException {
        BigInteger[] code = IntCodeLoader
                .getInstance()
                .loadBigIntCodeFromFile(Paths.get("resources", "com", "challenge", "day9"), "input.txt");

        IntCodeComputer intCodeComputer = createNewIntCodeComputer(code)
                .withInputValue(inputValue)
                .withMemoryAutoExpand()
                .build();

        List<BigInteger> output = intCodeComputer.executeCode();

        return output.get(0);
    }
}
