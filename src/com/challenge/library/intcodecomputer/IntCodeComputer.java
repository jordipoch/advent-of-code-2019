package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.ExecutionException;
import com.challenge.library.intcodecomputer.exception.IntComputerException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.challenge.library.intcodecomputer.InstructionExecutor.Builder.createInstructionExecutor;
import static com.challenge.library.utils.NumberUtils.convertToLong;

public class IntCodeComputer {
    InstructionExecutor instructionExecutor;
    boolean feedbackLoopMode;

    private IntCodeComputer(InstructionExecutor instructionExecutor, boolean feedbackLoopMode) {
        this.instructionExecutor = instructionExecutor;
        this.feedbackLoopMode = feedbackLoopMode;
    }

    public List<BigInteger> executeCode() throws IntComputerException {
        List<BigInteger> output = new ArrayList<>();

        Instruction.InstructionResult instructionResult;
        boolean finished;
        do {
            try {
                instructionResult = instructionExecutor.executeNextInstruction();
            } catch (ExecutionException e) {
                throw new IntComputerException("Error executing int computer code", e);
            }

            finished = processResult(instructionResult, output);
        } while (!finished);

        return output;
    }

    public void addInputValue(long inputValue) {
        this.instructionExecutor.addInputValue(inputValue);
    }

    public List<BigInteger> getMemorySnapshot() {
        return instructionExecutor.getMemorySnapshot();
    }

    private boolean processResult(Instruction.InstructionResult instructionResult,  List<BigInteger> output) {
        if (instructionResult.isExecutionFinished()) {
            return true;
        } else {
            if (instructionResult.getOutput().isPresent()) {
                output.add(instructionResult.getOutput().get());
                return feedbackLoopMode;
            }

            return false;
        }
    }

    public static class Builder {
        private InstructionExecutor.Builder instructionExecutorBuilder;
        private List<Long> input = new ArrayList<>();
        private boolean feedbackLoopMode;

        private Builder(InstructionExecutor.Builder instructionExecutorBuilder) {
            this.instructionExecutorBuilder = instructionExecutorBuilder;
        }

        public static Builder createNewIntCodeComputer(long[] code) {
            return new Builder(createInstructionExecutor(code));
        }

        public static Builder createNewIntCodeComputer(BigInteger[] code) {
            return new Builder(createInstructionExecutor(code));
        }

        public Builder withInputValue(long value) {
            input.add(value);
            return this;
        }

        public Builder withFeedbackLoopMode(boolean feedbackLoopMode) {
            this.feedbackLoopMode = feedbackLoopMode;
            return this;
        }

        public Builder withMemoryAutoExpand() {
            instructionExecutorBuilder.withMemoryAutoExpand();
            return this;
        }

        public Builder withMemoryInitialSize(int initialSize) {
            instructionExecutorBuilder.withMemoryInitialSize(initialSize);
            return this;
        }

        public IntCodeComputer build() {
            InstructionExecutor instructionExecutor = instructionExecutorBuilder
                    .startWithPosition(0)
                    .withInput(input)
                    .build();
            return new IntCodeComputer(instructionExecutor, feedbackLoopMode);
        }
    }
}
