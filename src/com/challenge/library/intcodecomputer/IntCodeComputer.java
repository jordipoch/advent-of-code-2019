package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.ExecutionException;
import com.challenge.library.intcodecomputer.exception.IntComputerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.challenge.library.intcodecomputer.InstructionExecutor.Builder.createInstructionExecutor;

public class IntCodeComputer {
    InstructionExecutor instructionExecutor;
    boolean feedbackLoopMode;

    private IntCodeComputer(InstructionExecutor instructionExecutor, boolean feedbackLoopMode) {
        this.instructionExecutor = instructionExecutor;
        this.feedbackLoopMode = feedbackLoopMode;
    }

    public List<Long> executeCode() throws IntComputerException {
        List<Long> output = new ArrayList<>();

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

    private boolean processResult(Instruction.InstructionResult instructionResult,  List<Long> output) {
        if (instructionResult.isExecutionFinished()) {
            return true;
        } else {
            if (instructionResult.getOutput().isPresent()) {
                output.add(instructionResult.getOutput().getAsLong());
                if (feedbackLoopMode)
                    return true;
            }

            if (instructionResult.getNewPosition().isPresent()) {
                instructionExecutor.setInstructionPointer(instructionResult.getNewPosition().getAsInt());
            }

            return false;
        }
    }

    public static class Builder {
        private long[] code;
        private List<Long> input = new ArrayList<>();
        private boolean feedbackLoopMode;

        private Builder(long[] code) {
            this.code = code;
        }

        public static Builder createNewIntCodeComputer(long[] code) {
            return new Builder(Arrays.copyOf(code, code.length));
        }

        public Builder withInputValue(long value) {
            input.add(value);
            return this;
        }

        public Builder withFeedbackLoopMode(boolean feedbackLoopMode) {
            this.feedbackLoopMode = feedbackLoopMode;
            return this;
        }

        public IntCodeComputer build() {
            InstructionExecutor instructionExecutor = createInstructionExecutor(code)
                    .startWithPosition(0)
                    .withInput(input)
                    .build();
            return new IntCodeComputer(instructionExecutor, feedbackLoopMode);
        }
    }
}
