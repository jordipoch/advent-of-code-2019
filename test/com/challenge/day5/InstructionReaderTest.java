package com.challenge.day5;

import static com.challenge.day5.InstructionReader.Builder.*;

import com.challenge.day5.exception.EndOfCodeException;
import com.challenge.day5.exception.InvalidInstructionException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class InstructionReaderTest {
    @Test
    public void testValidAddInstructionReading() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {1001, 1, 2, 3};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("ADD instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.ADD, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 3, "Incorrect number of parameters read: ");

        Instruction.Parameter param = instruction.getParameters().get(0);
        assertEquals(param.getValue(), code[1], "Incorrect value read for parameter 1: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.POSITION_MODE, "Incorrect mode read for parameter 1: ");

        param = instruction.getParameters().get(1);
        assertEquals(param.getValue(), code[2], "Incorrect value read for parameter 2: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.IMMEDIATE_MODE, "Incorrect mode read for parameter 2: ");

        param = instruction.getParameters().get(2);
        assertEquals(param.getValue(), code[3], "Incorrect value read for parameter 3: ");
    }

    @Test
    public void testValidMultiplyInstructionReading() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {2, 1, 2, 3};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("MULTIPLY instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.MULTIPLY, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 3, "Incorrect number of parameters read: ");

        Instruction.Parameter param = instruction.getParameters().get(0);
        assertEquals(param.getValue(), code[1], "Incorrect value read for parameter 1: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.POSITION_MODE, "Incorrect mode read for parameter 1: ");

        param = instruction.getParameters().get(1);
        assertEquals(param.getValue(), code[2], "Incorrect value read for parameter 2: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.POSITION_MODE, "Incorrect mode read for parameter 2: ");

        param = instruction.getParameters().get(2);
        assertEquals(param.getValue(), code[3], "Incorrect value read for parameter 3: ");
    }

    @Test
    public void testValidInputInstructionReading() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {3, 1};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("INPUT instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.INPUT, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 1, "Incorrect number of parameters read: ");

        Instruction.Parameter param = instruction.getParameters().get(0);
        assertEquals(param.getValue(), code[1], "Incorrect value read for parameter 1: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.POSITION_MODE, "Incorrect mode read for parameter 1: ");
    }

    @Test
    public void testValidOutputInstructionReading() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {4, 1};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("OUTPUT instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.OUTPUT, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 1, "Incorrect number of parameters read: ");

        Instruction.Parameter param = instruction.getParameters().get(0);
        assertEquals(param.getValue(), code[1], "Incorrect value read for parameter 1: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.POSITION_MODE, "Incorrect mode read for parameter 1: ");
    }

    @Test
    public void testValidOutputInstructionReadingImmediateMode() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {104, 1};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("OUTPUT instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.OUTPUT, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 1, "Incorrect number of parameters read: ");

        Instruction.Parameter param = instruction.getParameters().get(0);
        assertEquals(param.getValue(), code[1], "Incorrect value read for parameter 1: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.IMMEDIATE_MODE, "Incorrect mode read for parameter 1: ");
    }

    @Test
    public void testValidHaltInstructionReading() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {99};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("HALT instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.HALT, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 0, "Incorrect number of parameters read: ");
    }

    @Test
    public void testValidJumpIfTrueInstructionReading_PositionMode() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {5, 8, 10};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("JUMP_IF_TRUE instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.JUMP_IF_TRUE, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 2, "Incorrect number of parameters read: ");

        Instruction.Parameter param = instruction.getParameters().get(0);
        assertEquals(param.getValue(), code[1], "Incorrect value read for parameter 1: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.POSITION_MODE, "Incorrect mode read for parameter 1: ");

        param = instruction.getParameters().get(1);
        assertEquals(param.getValue(), code[2], "Incorrect value read for parameter 2: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.POSITION_MODE, "Incorrect mode read for parameter 2: ");
    }

    @Test
    public void testValidJumpIfFalseInstructionReading_ImmediateMode() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {1106, 8, 10};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("JUMP_IF_FALSE instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.JUMP_IF_FALSE, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 2, "Incorrect number of parameters read: ");

        Instruction.Parameter param = instruction.getParameters().get(0);
        assertEquals(param.getValue(), code[1], "Incorrect value read for parameter 1: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.IMMEDIATE_MODE, "Incorrect mode read for parameter 1: ");

        param = instruction.getParameters().get(1);
        assertEquals(param.getValue(), code[2], "Incorrect value read for parameter 2: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.IMMEDIATE_MODE, "Incorrect mode read for parameter 2: ");
    }

    @Test
    public void testValidLessThanInstructionReading_PositionMode() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {7, 8, 10, 11};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("LESS_THAN instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.LESS_THAN, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 3, "Incorrect number of parameters read: ");

        Instruction.Parameter param = instruction.getParameters().get(0);
        assertEquals(param.getValue(), code[1], "Incorrect value read for parameter 1: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.POSITION_MODE, "Incorrect mode read for parameter 1: ");

        param = instruction.getParameters().get(1);
        assertEquals(param.getValue(), code[2], "Incorrect value read for parameter 2: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.POSITION_MODE, "Incorrect mode read for parameter 2: ");

        param = instruction.getParameters().get(2);
        assertEquals(param.getValue(), code[3], "Incorrect value read for parameter 3: ");
    }

    @Test
    public void testValidEqualsInstructionReading_ImmediateMode() throws InvalidInstructionException, EndOfCodeException {
        long[] code = {1108, 8, 10, 11};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        Instruction instruction = instructionReader.readInstruction();

        System.out.println("LESS_THAN instruction read: " + instruction);

        assertEquals(instruction.operation, Instruction.Operation.EQUALS, "Incorrect operation read: ");
        assertEquals(instruction.getParameters().size(), 3, "Incorrect number of parameters read: ");

        Instruction.Parameter param = instruction.getParameters().get(0);
        assertEquals(param.getValue(), code[1], "Incorrect value read for parameter 1: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.IMMEDIATE_MODE, "Incorrect mode read for parameter 1: ");

        param = instruction.getParameters().get(1);
        assertEquals(param.getValue(), code[2], "Incorrect value read for parameter 2: ");
        assertEquals(param.getMode(), Instruction.ParameterMode.IMMEDIATE_MODE, "Incorrect mode read for parameter 2: ");

        param = instruction.getParameters().get(2);
        assertEquals(param.getValue(), code[3], "Incorrect value read for parameter 3: ");
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.InvalidInstructionException.class})
    public void testInvalidInstructionReading_NumParameters () throws InvalidInstructionException, EndOfCodeException {
        long[] code = {1001, 1, 2};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        instructionReader.readInstruction();
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.InvalidInstructionException.class})
    public void testInvalidInstructionReading_ParameterModes () throws InvalidInstructionException, EndOfCodeException {
        long[] code = {2001, 1, 2, 3};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        instructionReader.readInstruction();
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.InvalidInstructionException.class})
    public void testInvalidInstructionReading_InvalidParameterModeInput () throws InvalidInstructionException, EndOfCodeException {
        long[] code = {103, 1};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        instructionReader.readInstruction();
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.EndOfCodeException.class})
    public void testEndOfCodeException () throws InvalidInstructionException, EndOfCodeException {
        long[] code = {};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        instructionReader.readInstruction();
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.InvalidInstructionException.class})
    public void testInvalidLessThanInstruction_InvalidParameterMode () throws InvalidInstructionException, EndOfCodeException {
        long[] code = {10007, 1, 2, 3};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        instructionReader.readInstruction();
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.InvalidInstructionException.class})
    public void testInvalidEqualsInstruction_InvalidParameterMode () throws InvalidInstructionException, EndOfCodeException {
        long[] code = {10008, 1, 2, 3};

        InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();
        instructionReader.readInstruction();
    }
}
