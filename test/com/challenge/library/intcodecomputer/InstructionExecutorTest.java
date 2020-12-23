package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.*;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        assertEquals(instructionExecutor.getMemorySnapshot().get(4).longValue(), 3);
        assertFalse(result.getOutput().isPresent());
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
        System.out.println(instructionExecutor.getMemorySnapshot());

        result = instructionExecutor.executeNextInstruction();
        assertFalse(result.isExecutionFinished());
        System.out.println(instructionExecutor.getMemorySnapshot());

        result = instructionExecutor.executeNextInstruction();
        assertTrue(result.isExecutionFinished());

        assertEquals(instructionExecutor.getMemorySnapshot().get(10).longValue(), 20);
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
        assertEquals(result.getOutput().get().longValue(), firstValue * secondValue);
    }

    @Test
    public void testInputNeededResult() throws ExecutionException {
        long[] code = {3, 1, 0};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        Instruction.InstructionResult result = instructionExecutor.executeNextInstruction();
        assertTrue(result.isInputNeeded());
    }

    @Test
    public void testInputNeededAndThenInputProvided() throws ExecutionException {
        long[] code = {3, 2, 0};
        long input = 2L;

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        instructionExecutor.executeNextInstruction();
        instructionExecutor.addInputValue(input);
        Instruction.InstructionResult result = instructionExecutor.executeNextInstruction();

        assertTrue(result.isEmptyResult());
        assertEquals(instructionExecutor.getMemorySnapshot().get(2).longValue(), input);
    }

    @Test
    public void testRelativeBaseOffset() throws ExecutionException {
        long[] code = {109, 7, 22201, -1, 0, 1, 3, 2, 0};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        instructionExecutor.executeNextInstruction(); // adjust relative base
        instructionExecutor.executeNextInstruction(); // add operation

        List<BigInteger> memorySnapshot = instructionExecutor.getMemorySnapshot();
        System.out.println("Memory after execution = " + memorySnapshot);
        assertEquals(memorySnapshot.get(8).longValue(), 5);
    }

    @Test
    public void testMemoryAutoExpand() throws ExecutionException {
        long[] code = {109, 7, 22201, -1, 0, 10, 3, 2};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .withMemoryAutoExpand()
                .build();

        instructionExecutor.executeNextInstruction(); // adjust relative base
        instructionExecutor.executeNextInstruction(); // add operation

        List<BigInteger> memorySnapshot = instructionExecutor.getMemorySnapshot();
        System.out.println("Memory after execution = " + memorySnapshot);
        assertEquals(memorySnapshot.get(17).longValue(), 5);
    }

    @Test
    public void testMemoryInitialSize() throws ExecutionException {
        long[] code = {109, 7, 22201, -1, 0, 10, 3, 2};

        final int initialSize = 30;

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .withMemoryInitialSize(initialSize)
                .build();

        instructionExecutor.executeNextInstruction(); // adjust relative base
        instructionExecutor.executeNextInstruction(); // add operation

        List<BigInteger> memorySnapshot = instructionExecutor.getMemorySnapshot();
        System.out.println("Memory after execution = " + memorySnapshot);
        assertEquals(memorySnapshot.get(17).longValue(), 5);
        assertEquals(memorySnapshot.size(), initialSize);
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
            System.out.println("Inner exception: " + e.getCause().getCause());
            assertTrue(e.getCause() instanceof InvalidInstructionException);
            assertTrue(e.getCause().getCause() instanceof InvalidOperationException);
            throw e;
        }
    }

    @Test(expectedExceptions = {ExecutionException.class})
    public void testEndOfCodeError() throws ExecutionException {
        long[] code = {1102, 5, 5, 3};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        try {
            instructionExecutor.executeNextInstruction();
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
    public void testRelativeBaseOffsetInvalidPosition() throws ExecutionException {
        long[] code = {109, 7, 22201, -1, 0, 1, 3, 2};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .build();

        try {

            instructionExecutor.executeNextInstruction(); // adjust relative base
            instructionExecutor.executeNextInstruction(); // add operation
        } catch (ExecutionException e) {
            System.out.println("Exception: " + e);
            System.out.println("Inner exception: " + e.getCause());
            assertTrue(e.getCause() instanceof InvalidPositionException);
            throw e;
        }
    }

    @Test(expectedExceptions = {ExecutionException.class})
    public void testMemoryAutoExpandError() throws ExecutionException {
        long[] code = {109, 7, 22201, -1, 0, 1_000_000, 3, 2};

        InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                .startWithPosition(0)
                .withInput(new ArrayList<>())
                .withMemoryAutoExpand()
                .build();

        try {
            instructionExecutor.executeNextInstruction(); // adjust relative base
            instructionExecutor.executeNextInstruction(); // add operation
        } catch (ExecutionException e) {
            System.out.println("Exception: " + e);
            System.out.println("Caused by: " + e.getCause());
            System.out.println("Caused by: " + e.getCause().getCause());
            assertTrue(e.getCause().getCause() instanceof MemoryExpandException);
            throw e;
        }
    }
}
