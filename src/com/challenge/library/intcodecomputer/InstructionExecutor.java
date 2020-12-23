package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static com.challenge.library.intcodecomputer.IntCodeMemory.Builder.createIntCodeMemory;

public class InstructionExecutor {
    private static final Logger logger = LogManager.getLogger();

    IntCodeMemory code;
    List<BigInteger> input;
    int inputIndex;

    private InstructionExecutor(IntCodeMemory code, List<BigInteger> input) {
        this.code = code;
        this.input = input;
    }

    public Instruction.InstructionResult executeNextInstruction() throws ExecutionException {
        Instruction.InstructionResult result;

        Instruction instruction;
        try {
         instruction = code.readInstruction();
        } catch (Exception e) {
            throw new ExecutionException("Error reading next instruction", e);
        }

        try {
            if (instruction.getOperation() == Instruction.Operation.INPUT) {
                result = instruction.execute(code, getNextInputValue());
                if (result.isInputNeeded()) {
                    code.goBackToPreviousPosition();
                }
            } else {
                result = instruction.execute(code);
            }
        } catch (InvalidPositionException e) {
            throw new ExecutionException(instruction, "Error executing instruction", e);
        }

        return result;
    }

    public void addInputValue(long inputValue) {
        input.add(BigInteger.valueOf(inputValue));
        logger.trace("Input added. Input index = {}, Input list = {}", inputIndex, input);
    }

    public void rewindLastInstruction() {
        code.goBackToPreviousPosition();
    }

    public List<BigInteger> getMemorySnapshot() {
        return code.getSnapshot();
    }

    private BigInteger getNextInputValue() {
        if (inputIndex < input.size()) {
            return input.get(inputIndex++);
        } else {
            return null;
        }
    }

    public static class Builder {
        private IntCodeMemory.Builder intCodeMemoryBuilder;
        private List<BigInteger> input;

        private Builder(long[] code) {
            intCodeMemoryBuilder = createIntCodeMemory(code);
        }

        private Builder(BigInteger[] code) {
            intCodeMemoryBuilder = createIntCodeMemory(code);
        }

        public static Builder createInstructionExecutor(long[] code) {
            return new Builder(code);
        }

        public static Builder createInstructionExecutor(BigInteger[] code) {
            return new Builder(code);
        }

        public Builder startWithPosition(int pos) {
            this.intCodeMemoryBuilder.withStartingPos(pos);
            return this;
        }

        public Builder withInput(List<Long> input) {
            this.input = input.stream().map(BigInteger::valueOf).collect(Collectors.toList());
            return this;
        }

        public Builder withMemoryAutoExpand() {
            intCodeMemoryBuilder.withAutoExpand();
            return this;
        }

        public Builder withMemoryInitialSize(int initialSize) {
            intCodeMemoryBuilder.withInitialSize(initialSize);
            return this;
        }

        public InstructionExecutor build() {
            return new InstructionExecutor(intCodeMemoryBuilder.build(), input);
        }
    }
}
