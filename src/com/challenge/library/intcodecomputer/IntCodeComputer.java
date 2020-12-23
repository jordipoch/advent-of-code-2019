package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.ExecutionException;
import com.challenge.library.intcodecomputer.exception.ExecutionResultBuilderException;
import com.challenge.library.intcodecomputer.exception.IntComputerException;
import static com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult.Builder.createExecutionResult;

import com.challenge.library.intcodecomputer.exception.NoMoreInputValuesException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.challenge.library.intcodecomputer.InstructionExecutor.Builder.createInstructionExecutor;

public class IntCodeComputer {
    private static final Logger logger = LogManager.getLogger();

    private final InstructionExecutor instructionExecutor;
    private final Configuration configuration;

    private IntCodeComputer(InstructionExecutor instructionExecutor, Configuration configuration) {
        this.instructionExecutor = instructionExecutor;
        this.configuration = configuration;
    }

    public ExecutionResult executeCode() throws IntComputerException {
        ExecutionResult.Builder result = createExecutionResult();

        boolean finished;
        do {
            try {
                Instruction.InstructionResult instructionResult = instructionExecutor.executeNextInstruction();
                if (logger.isDebugEnabled() && instructionResult.isExecutionFinished()) {
                    logger.debug("Int code computer: HALT operation received!! Execution finished!!");
                }

                finished = updateResult(instructionResult, result);
            } catch (ExecutionException | NoMoreInputValuesException e) {
                throw new IntComputerException("Error executing int computer code", e);
            }
        } while (!finished);

        return result.build();
    }

    public void addInputValue(long inputValue) {
        this.instructionExecutor.addInputValue(inputValue);
    }

    public List<BigInteger> getMemorySnapshot() {
        return instructionExecutor.getMemorySnapshot();
    }

    private boolean updateResult(Instruction.InstructionResult instructionResult, ExecutionResult.Builder result) throws NoMoreInputValuesException {
        switch (instructionResult.getResultType()) {
            case EXECUTION_FINISHED -> {
                result.withResultType(ExecutionResult.ResultType.EXECUTION_FINISHED);
                return true;
            }
            case OUTPUT -> {
                if (instructionResult.getOutput().isPresent()) {
                    result.addOutputValue(instructionResult.getOutput().get());
                } else {
                    throw new IllegalStateException("Expecting an output value for an instruction result of type OUTPUT");
                }
                if (configuration.isFeedbackLoopMode()) {
                    result.withResultType(ExecutionResult.ResultType.NEXT_OUTPUT);
                    return true;
                } else {
                    return false;
                }
            }
            case INPUT_NEEDED -> {
                if (configuration.isAskForInputMode()) {
                    result.withResultType(ExecutionResult.ResultType.INPUT_NEEDED);
                    return true;
                } else {
                    throw new NoMoreInputValuesException();
                }

            }
            default -> { return false; }
        }
    }

    public static class Builder {
        private InstructionExecutor.Builder instructionExecutorBuilder;
        private List<Long> input = new ArrayList<>();
        private Configuration.Builder configurationBuilder = Configuration.Builder.createConfiguration();

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

        public Builder withFeedbackLoopMode() {
            configurationBuilder.withFeedBackLoopMode();
            return this;
        }

        public Builder withFeedbackLoopMode(boolean feedbackLoopMode) {
            if (feedbackLoopMode) {
                withFeedbackLoopMode();
            }
            return this;
        }

        public Builder withAskForInputMode() {
            configurationBuilder.withAskForInputMode();
            return this;
        }

        public Builder withAskForInputMode(boolean askForInputMode) {
            if (askForInputMode) {
                withAskForInputMode();
            }
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
            return new IntCodeComputer(instructionExecutor, configurationBuilder.build());
        }
    }

    public static class Configuration {
        private final boolean feedbackLoopMode;
        private final boolean askForInputMode;

        private Configuration(boolean feedbackLoopMode, boolean askForInputMode) {
            this.feedbackLoopMode = feedbackLoopMode;
            this.askForInputMode = askForInputMode;
        }

        public boolean isFeedbackLoopMode() {
            return feedbackLoopMode;
        }

        public boolean isAskForInputMode() {
            return askForInputMode;
        }

        public static class Builder {
            private boolean feedbackLoopMode;
            private boolean askForInputMode;

            public static Builder createConfiguration() {
                return new Builder();
            }

            public Builder withFeedBackLoopMode() {
                feedbackLoopMode = true;
                return this;
            }

            public Builder withAskForInputMode() {
                askForInputMode = true;
                return this;
            }

            public Configuration build() {
                return new Configuration(feedbackLoopMode, askForInputMode);
            }
        }
    }

    public static class ExecutionResult {
        private final ResultType resultType;
        private final List<BigInteger> output;

        private ExecutionResult(ResultType resultType, List<BigInteger> output) {
            this.resultType = resultType;
            this.output = output;
        }

        public List<BigInteger> getOutput() {
            return output;
        }

        public boolean isExecutionFinished() {
            return resultType == ResultType.EXECUTION_FINISHED;
        }

        public boolean isInputNeeded() {
            return resultType == ResultType.INPUT_NEEDED;
        }

        public boolean isNextOutput() {
            return resultType == ResultType.NEXT_OUTPUT;
        }

        public boolean hasAnyOutput() {
            return !output.isEmpty();
        }

        @Override
        public String toString() {
            return "{" + resultType +
                    (resultType == ResultType.NEXT_OUTPUT ? output : "") +
                    '}';
        }

        public static class Builder {
            private ResultType resultType;
            private final List<BigInteger> output = new ArrayList<>();

            public static Builder createExecutionResult() {
                return new Builder();
            }

            public Builder withResultType(ResultType resultType) {
                this.resultType = resultType;
                return this;
            }

            public Builder addOutputValue(BigInteger value) {
                Objects.requireNonNull(value);
                output.add(value);
                return this;
            }

            public ExecutionResult build() {
                performChecks();
                return new ExecutionResult(resultType, output);
            }

            private void performChecks() {
                Objects.requireNonNull(resultType, "Result type not assigned");

                if (resultType == ResultType.NEXT_OUTPUT && output.isEmpty()) {
                    throw new ExecutionResultBuilderException(String.format("With result type = %s the output list can't be empty", ResultType.NEXT_OUTPUT));
                }
            }
        }

        public enum ResultType {
            NEXT_OUTPUT, INPUT_NEEDED, EXECUTION_FINISHED
        }
    }
}
