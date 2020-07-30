package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.IntComputerException;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.List;

import static com.challenge.library.intcodecomputer.IntCodeComputer.Builder.createNewIntCodeComputer;

public class IntCodeComputerTest {
    @Test
    public void testSimpleCode_1() throws IntComputerException {
        long[] code = {3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0};

        IntCodeComputer intCodeComputer = createNewIntCodeComputer(code)
                .withInputValue(4)
                .withInputValue(200)
                .build();

        List<Long> output = intCodeComputer.executeCode();

        assertEquals(output.size(), 1);
        System.out.println("Execution result: " + output.get(0));
        assertEquals(output.get(0).longValue(), 2004L);
    }

    @Test (expectedExceptions = {IntCodeComputer.class})
    public void testIntCodeComputerError() throws IntComputerException {
        long[] code = {3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0};

        IntCodeComputer intCodeComputer = createNewIntCodeComputer(code)
                .withInputValue(4)
                //.withInputValue(200)
                .build();

        try {
            List<Long> output = intCodeComputer.executeCode();
        } catch (IntComputerException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
