package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.*;

import static com.challenge.library.intcodecomputer.InstructionExecutor.Builder.createInstructionExecutor;

public class InstructionExecutorTest {
    @Test
    public void testExecuteOneSimpleInstruction() throws ExecutionException {
        long[] code = {1101, 1, 2, 4, -1};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        Instruction.InstructionResult result = instructionExecutor.executeNextInstruction();

        assertEquals(code[4], 3);
        assertFalse(result.getOutput().isPresent());
        assertFalse(result.getNewPosition().isPresent());
        assertFalse(result.isExecutionFinished());
    }

    @Test
    public void testExecuteThreeInstructionSequenceWithHalt() throws ExecutionException {
        long[] code = {1102, 5, 5, 9, 1001, 9, -5, 10, 99, -1, -2};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        Instruction.InstructionResult result = instructionExecutor.executeNextInstruction();
        assertFalse(result.isExecutionFinished());

        result = instructionExecutor.executeNextInstruction();
        assertFalse(result.isExecutionFinished());

        result = instructionExecutor.executeNextInstruction();
        assertTrue(result.isExecutionFinished());

        assertEquals(code[10], 20);
    }

    @Test
    public void testExecuteInputAndOutputInstructions() throws ExecutionException {
        long[] code = {3, 10, 3, 11, 2, 10, 11, 12, 4, 12, 0, 0, 0};

        long firstValue = 10;
        long secondValue = 12;

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(Arrays.asList(firstValue, secondValue))
                .build();

        instructionExecutor.executeNextInstruction(); // first input instruction
        instructionExecutor.executeNextInstruction(); // second input instruction
        instructionExecutor.executeNextInstruction(); // multiply instruction
        Instruction.InstructionResult result = instructionExecutor.executeNextInstruction(); // output instruction

        assertTrue(result.getOutput().isPresent());
        assertEquals(result.getOutput().getAsLong(), firstValue * secondValue);
    }

    @Test(expectedExceptions = {ExecutionException.class})
    public void testInvalidInstructionError() throws ExecutionException {
       long[] code = {1141, 1, 2, 3};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        try {
            instructionExecutor.executeNextInstruction();
        } catch (ExecutionException e) {
            System.out.println("Exception: " + e);
            System.out.println("Inner exception: " + e.getCause());
            assertTrue(e.getCause() instanceof InvalidInstructionException);
            throw e;
        }
    }

    @Test(expectedExceptions = {ExecutionException.class})
    public void testEndOfCodeError() throws ExecutionException {
        long[] code = {};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        try {
            instructionExecutor.executeNextInstruction();
        } catch (ExecutionException e) {
            System.out.println("Exception: " + e);
            System.out.println("Inner exception: " + e.getCause());
            assertTrue(e.getCause() instanceof EndOfCodeException);
            throw e;
        }
    }

    @Test(expectedExceptions = {ExecutionException.class})
    public void testInvalidPositionError() throws ExecutionException {
        long[] code = {1101, 1, 2, 4};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        try {
            instructionExecutor.executeNextInstruction();
        } catch (ExecutionException e) {
            System.out.println("Exception: " + e);
            System.out.println("Inner exception: " + e.getCause());
            assertTrue(e.getCause() instanceof InvalidPositionException);
            throw e;
        }
    }

    @Test(expectedExceptions = {ExecutionException.class})
    public void testNoMoreInputValuesError() throws ExecutionException {
        long[] code = {3, 10};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        try {
            instructionExecutor.executeNextInstruction();
        } catch (ExecutionException e) {
            System.out.println("Exception: " + e);
            System.out.println("Inner exception: " + e.getCause());
            assertTrue(e.getCause() instanceof NoMoreInputValuesException);
            throw e;
        }
    }
}
