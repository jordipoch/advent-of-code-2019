package com.challenge.day5;

import static com.challenge.day5.InstructionReader.Builder.*;

import com.challenge.day5.exception.*;

import java.util.OptionalLong;

public class DiagnosticTestRunner {
    private long[] code;
    private InstructionReader instructionReader;

    private DiagnosticTestRunner(long[] code, InstructionReader instructionReader) {
        this.code = code;
        this.instructionReader = instructionReader;
    }

    public OptionalLong runDiagnosticTest() throws ExecutionException, EndOfCodeException {

        Instruction.InstructionResult result;
        do {
            Instruction instruction;
            try {
                instruction = instructionReader.readInstruction();
            } catch (InvalidInstructionException e) {
                throw new ExecutionException("Error reading instruction at position " + instructionReader.getInstructionPointer(), e);
            }

            //if (instruction.operation == Instruction.Operation.OUTPUT)
            //    System.out.println(String.format("Output instruction found at position %d: %s", instruction.getPosition(), instruction));

            if (instruction.operation == Instruction.Operation.HALT) {
                return OptionalLong.empty();
            }

            if (instruction.operation == Instruction.Operation.INPUT) {
                throw new ExecutionException("Un expected INPUT instruction found at position " + instruction.getPosition());
            }

            try {
                result = instruction.execute(code);
            } catch (InvalidPositionException e) {
                throw new ExecutionException(instruction, "Error executing instruction at position " + instruction.getPosition(), e);
            }

            if (result.getNewPosition().isPresent()) {
                instructionReader.setInstructionPointer(result.getNewPosition().getAsInt());
            }

        } while (!result.getOutput().isPresent());

        return result.getOutput();
    }

    public void setInput(long input) throws ExecutionException, EndOfCodeException {
        Instruction inputInstruction;
        try {
            inputInstruction = instructionReader.readInstruction();
        } catch (InvalidInstructionException e) {
            throw new ExecutionException("Error reading instruction at position " + instructionReader.getInstructionPointer(), e);
        }

        if (inputInstruction.operation != Instruction.Operation.INPUT) {
            throw new ExecutionException(inputInstruction, "Input instruction was expected");
        }

        try {
            inputInstruction.execute(code, input);
        } catch (InvalidPositionException e) {
            throw new ExecutionException(inputInstruction, "Error executing instruction at position " + inputInstruction.getPosition(), e);
        }
    }

    public static class Builder {
        private long[] code;


        private Builder(long[] code) {
            this.code = code;
        }

        public static Builder createDiagnosticTestRunner(long[] code) {
            return new Builder(code);
        }

        public DiagnosticTestRunner build() {
            InstructionReader instructionReader = createInstructionReader(code).startWithPosition(0).build();

            return new DiagnosticTestRunner(code, instructionReader);
        }

    }
}
