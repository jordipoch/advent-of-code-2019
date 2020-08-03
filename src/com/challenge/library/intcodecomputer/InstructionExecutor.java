package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.*;

import java.util.Iterator;
import java.util.List;

import static com.challenge.library.intcodecomputer.Instruction.Builder.createInstruction;

public class InstructionExecutor {
    long[] code;
    int pos;
    List<Long> input;
    int inputIndex;

    private InstructionExecutor(long[] code, List<Long> input, int pos) {
        this.code = code;
        this.input = input;
        this.pos = pos;
    }

    public Instruction.InstructionResult executeNextInstruction() throws ExecutionException {
        Instruction.InstructionResult result;

        Instruction instruction;
        try {
         instruction = readInstruction();
        } catch (InvalidInstructionException e) {
            throw new ExecutionException(String.format("Error reading instruction %d at position %d", code[pos], pos), e);
        } catch (EndOfCodeException e) {
            throw new ExecutionException(String.format("Error reading instruction at position %d", pos), e);
        }

        try {
            if (instruction.getOperation() == Instruction.Operation.INPUT) {
                result = instruction.execute(code, getNextInputValue());
            } else {
                result = instruction.execute(code);
            }
        } catch (InvalidPositionException | NoMoreInputValuesException e) {
            throw new ExecutionException(instruction, "Error executing instruction", e);
        }

        return result;
    }

    public void addInputValue(long inputValue) {
        input.add(inputValue);
    }

    private long getNextInputValue() throws NoMoreInputValuesException {
        if (inputIndex < input.size()) {
            return input.get(inputIndex++);
        } else {
            throw new NoMoreInputValuesException();
        }
    }

    private Instruction readInstruction() throws InvalidInstructionException, EndOfCodeException {
        if (pos >= code.length)
            throw new EndOfCodeException(pos, code.length);

        long initialPos = pos;
        long opCodeAndParamModes = code[pos];

        if (opCodeAndParamModes < 0)
            throw new InvalidInstructionException(opCodeAndParamModes, initialPos, "Instruction can't be a negative number");

        try {
            Instruction.Builder builder = createInstruction(opCodeAndParamModes, pos);
            int numParams = builder.getNumParameters();
            if (numParams > 0) {
                if (pos + numParams >= code.length)
                    throw new InvalidInstructionException(opCodeAndParamModes, initialPos,
                            String.format("Instruction needs %d parameters, but only %d were found in the code", numParams, code.length - 1 - pos));

                long[] params = new long[numParams];
                for (int i = 0; i < numParams; i++)
                    params[i] = code[pos+i+1];

                builder = builder.withParameters(params);
            }

            Instruction instruction = builder.build();
            pos = instruction.getNextInstructionPosition();
            return instruction;
        } catch (InvalidOperationException | InvalidParameterModesException e) {
            throw new InvalidInstructionException(opCodeAndParamModes, initialPos, e);
        }
    }

    public int getInstructionPointer() {
        return pos;
    }

    public void setInstructionPointer(int newPointer) { this.pos = newPointer; }

    public static class Builder {
        long[] code;
        int pos;
        List<Long> input;

        private Builder(long[] code) {
            this.code = code;
        }

        public static Builder createInstructionExecutor(long[] code) {
            return new Builder(code);
        }

        public Builder startWithPosition(int pos) {
            this.pos = pos;
            return this;
        }

        public Builder withInput(List<Long> input) {
            this.input = input;
            return this;
        }

        public InstructionExecutor build() {
            return new InstructionExecutor(code, input, pos);
        }
    }
}
