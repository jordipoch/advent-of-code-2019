package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.InvalidOperationException;
import com.challenge.library.intcodecomputer.exception.InvalidParameterModesException;
import com.challenge.library.intcodecomputer.exception.InvalidPositionException;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Instruction {
    Operation operation;
    List<Parameter> parameters;
    int position;

    private Instruction(Operation operation, List<Parameter> parameters, int position) {
        this.operation = operation;
        this.parameters = parameters;
        this.position = position;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getNextInstructionPosition() {
        return position + parameters.size() + 1;
    }

    public InstructionResult execute(IntCodeMemory code, BigInteger ...inputValues) throws InvalidPositionException {
        return operation.executeInstruction(code, parameters, inputValues);
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "operation=" + operation +
                ", parameters=" + parameters +
                '}';
    }

    public static class Parameter {
        private BigInteger value;
        private ParameterMode mode;

        Parameter(BigInteger value, ParameterMode mode) {
            this.value = value;
            this.mode = mode;
        }

        public BigInteger getValue() {
            return value;
        }

        public ParameterMode getMode() {
            return mode;
        }

        @Override
        public String toString() {
            return "Parameter{" +
                    "value=" + value +
                    ", mode=" + mode +
                    '}';
        }
    }

    public static class InstructionResult {
        private final BigInteger output;
        private final ResultType resultType;

        private InstructionResult() {
            this(ResultType.EMPTY);
        }

        private InstructionResult(ResultType resultType) {
            this(resultType, null);
        }

        private InstructionResult(BigInteger output) {
            this(ResultType.OUTPUT, output);
        }

        private InstructionResult(ResultType resultType, BigInteger output) {
            this.resultType = resultType;
            this.output = output;
        }

        public static InstructionResult emptyInstructionResult() {
            return new InstructionResult();
        }

        public static InstructionResult endOfExecutionResult() {
            return new InstructionResult(ResultType.EXECUTION_FINISHED);
        }

        private static InstructionResult inputNeededResult() {
            return new InstructionResult(ResultType.INPUT_NEEDED);
        }

        public static InstructionResult ofOutput(BigInteger output) {
            Objects.requireNonNull(output, "The output should not be null");
            return new InstructionResult(output);
        }

        public Optional<BigInteger> getOutput() {
            return Optional.ofNullable(output);
        }

        public ResultType getResultType() {
            return resultType;
        }

        public boolean isExecutionFinished() { return resultType == ResultType.EXECUTION_FINISHED; }

        public boolean isInputNeeded() { return resultType == ResultType.INPUT_NEEDED; }

        public boolean isEmptyResult() { return resultType == ResultType.EMPTY; }

        public enum ResultType {
            EMPTY, OUTPUT, INPUT_NEEDED, EXECUTION_FINISHED
        }
    }

    enum ParameterMode {
        POSITION_MODE(0), IMMEDIATE_MODE(1), RELATIVE_MODE(2);

        int mode;

        ParameterMode(int mode) {
            this.mode = mode;
        }

        @Override
        public String toString() {
            return Integer.toString(mode);
        }

        private static final Map<String, ParameterMode> stringToEnum = Stream.of(values()).collect(toMap(Object::toString, e -> e));

        public static Optional<ParameterMode> fromString(String paramMode) {
            return Optional.ofNullable(stringToEnum.get(paramMode));
        }
    }

    enum Operation {
        ADD(1, 3) {
            @Override
            public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
                if (parameterList.get(2).mode == ParameterMode.IMMEDIATE_MODE)
                    throw new InvalidParameterModesException("For the ADD operation the 3rd parameter can't be in immediate mode");
            }

            @Override
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException {
                BigInteger result = getParameterValue(parameters.get(0), code).add(getParameterValue(parameters.get(1), code));
                code.setValue(result, getMemoryPosition(parameters.get(2), code));

                return InstructionResult.emptyInstructionResult();
            }
        },

        MULTIPLY(2, 3) {
            @Override
            public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
                if (parameterList.get(2).mode == ParameterMode.IMMEDIATE_MODE)
                    throw new InvalidParameterModesException("For the MULTIPLY operation the 3rd parameter can't be in immediate mode");
            }

            @Override
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException {
                BigInteger result = getParameterValue(parameters.get(0), code).multiply(getParameterValue(parameters.get(1), code));
                code.setValue(result, getMemoryPosition(parameters.get(2), code));

                return InstructionResult.emptyInstructionResult();
            }
        },

        INPUT(3, 1) {
            @Override
            public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
                if (parameterList.get(0).mode == ParameterMode.IMMEDIATE_MODE)
                    throw new InvalidParameterModesException("For the INPUT operation the parameter can't be in immediate mode");
            }

            @Override
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException {
                if (inputValues[0] != null) {
                    code.setValue(inputValues[0], getMemoryPosition(parameters.get(0), code));
                    return InstructionResult.emptyInstructionResult();
                } else {
                    return InstructionResult.inputNeededResult();
                }
            }
        },

        OUTPUT(4, 1) {
            @Override
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException {
                return InstructionResult.ofOutput(getParameterValue(parameters.get(0), code));
            }
        },
        HALT(99, 0) {
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) {
                return InstructionResult.endOfExecutionResult();
            }
        },
        JUMP_IF_TRUE(5, 2) {
            @Override
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException {
                if (getParameterValue(parameters.get(0), code).compareTo(BigInteger.ZERO) != 0) {
                   code.setCurrentPos(getParameterValue(parameters.get(1), code));
                }

                return InstructionResult.emptyInstructionResult();
            }
        },
        JUMP_IF_FALSE(6, 2) {
            @Override
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException {
                if (getParameterValue(parameters.get(0), code).compareTo(BigInteger.ZERO) == 0) {
                    code.setCurrentPos(getParameterValue(parameters.get(1), code));
                }

                return InstructionResult.emptyInstructionResult();
            }
        },
        LESS_THAN(7, 3) {
            @Override
            public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
                if (parameterList.get(2).mode == ParameterMode.IMMEDIATE_MODE)
                    throw new InvalidParameterModesException("For the LESS_THAN operation the third parameter can't be in immediate mode");
            }

            @Override
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException {
                if (getParameterValue(parameters.get(0), code).compareTo(getParameterValue(parameters.get(1), code)) < 0) {
                    code.setValue(BigInteger.ONE, getMemoryPosition(parameters.get(2), code));
                } else {
                    code.setValue(BigInteger.ZERO, getMemoryPosition(parameters.get(2), code));
                }

                return InstructionResult.emptyInstructionResult();
            }
        },
        EQUALS(8, 3) {
            @Override
            public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
                if (parameterList.get(2).mode == ParameterMode.IMMEDIATE_MODE)
                    throw new InvalidParameterModesException("For the EQUALS operation the third parameter can't be in immediate mode");
            }

            @Override
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException {
                if (getParameterValue(parameters.get(0), code).compareTo(getParameterValue(parameters.get(1), code)) == 0) {
                    code.setValue(BigInteger.ONE, getMemoryPosition(parameters.get(2), code));
                } else {
                    code.setValue(BigInteger.ZERO, getMemoryPosition(parameters.get(2), code));
                }

                return InstructionResult.emptyInstructionResult();
            }
        },
        ADJUST_RELATIVE_BASE(9, 1) {
            @Override
            public InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException {
                code.incrementRelativeBaseOffset(getParameterValue(parameters.get(0), code));

                return InstructionResult.emptyInstructionResult();
            }
        };

        int opCode;
        int numParams;

        Operation (int opCode, int numParams) {
            this.opCode = opCode;
            this.numParams = numParams;
        }

        @Override
        public String toString() {
            return Integer.toString(opCode);
        }

        public int getNumParams() {
            return numParams;
        }

        protected BigInteger getParameterValue(Parameter parameter, IntCodeMemory code) throws InvalidPositionException {
           switch (parameter.getMode()) {
               case IMMEDIATE_MODE:
                   return parameter.getValue();
               case POSITION_MODE:
                   return code.getValue(parameter.getValue());
               default: // RELATIVE_MODE
                   return code.getValue(parameter.getValue().add(code.getRelativeBaseOffset()));
           }
        }

        protected BigInteger getMemoryPosition(Parameter parameter, IntCodeMemory code) {
            switch (parameter.getMode()) {
                case POSITION_MODE:
                    return parameter.getValue();
                case RELATIVE_MODE:
                    return code.getRelativeBaseOffset().add(parameter.getValue());
                default: // IMMEDIATE_MODE
                    throw new IllegalArgumentException("Parameter mode IMMEDIATE_MODE not allowed");
            }
        }

        public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
            // Override only if the operation have some validations to do.
        }
        public abstract InstructionResult executeInstruction(IntCodeMemory code, List<Parameter> parameters, BigInteger ...inputValues) throws InvalidPositionException;

        private static final Map<String, Operation> stringToEnum = Stream.of(values()).collect(toMap(Object::toString, e -> e));

        public static Optional<Operation> fromString(String op) {
            return Optional.ofNullable(stringToEnum.get(op));
        }

    }

    public static class Builder {
        Operation operation;
        List<ParameterMode> parameterModeList;
        List<Parameter> parameterList;
        int numParameters;
        int position;

        private Builder(Operation operation, List<ParameterMode> parameterModeList, int numParameters, int position) {
            this.operation = operation;
            this.parameterModeList = parameterModeList;
            this.numParameters = numParameters;
            this.position = position;

            parameterList = new ArrayList<>();
        }

        public int getNumParameters() {
            return numParameters;
        }

        public static Builder createInstruction(int opCodeAndParamModes, int position) throws InvalidOperationException, InvalidParameterModesException {

            final int opCode = opCodeAndParamModes % 100;
            Optional<Operation> optionalOperation = Operation.fromString(Integer.toString(opCode));
            if(optionalOperation.isPresent()) {
                Operation operation = optionalOperation.get();
                List<ParameterMode> parameterModeList = getParameterModes(opCodeAndParamModes, operation.getNumParams());
                return new Builder(operation, parameterModeList, operation.getNumParams(), position);
            } else {
                throw new InvalidOperationException(opCode);
            }
        }

        public Builder withParameters(BigInteger ...parameters) {
            Objects.requireNonNull(parameters);
            if (parameters.length != parameterModeList.size()) {
                throw new IllegalArgumentException("The size of the parameters array must mach the size of the parameter modes");
            }

            Iterator<ParameterMode> it = parameterModeList.iterator();
            for (BigInteger param : parameters) {
                parameterList.add(new Parameter(param, it.next()));
            }

            return this;
        }

        public Instruction build() throws InvalidParameterModesException {
            operation.validateParameters(parameterList);
            return new Instruction(operation, parameterList, position);
        }

        private static List<ParameterMode> getParameterModes(int opCodeAndParamModes, int numParams) throws InvalidParameterModesException {
            List<ParameterMode> parameterModeList = new ArrayList<>();

            int remaining = opCodeAndParamModes/100;
            for (int i = 0; i < numParams; i++) {
                String strParamMode = Long.toString(remaining%10);
                Optional<ParameterMode> parameterMode = ParameterMode.fromString(strParamMode);
                if (parameterMode.isPresent()) {
                    parameterModeList.add(parameterMode.get());
                } else {
                    throw new InvalidParameterModesException(opCodeAndParamModes, String.format("Invalid parameter mode %s for param nº %d", strParamMode, i+1));
                }
                remaining /= 10;
            }

            if (remaining > 0)
                throw new InvalidParameterModesException(opCodeAndParamModes, "Too many parameter modes found");

            return parameterModeList;
        }
    }
}
