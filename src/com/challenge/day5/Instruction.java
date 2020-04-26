package com.challenge.day5;

import com.challenge.day5.exception.InvalidOperationException;
import com.challenge.day5.exception.InvalidParameterModesException;
import com.challenge.day5.exception.InvalidPositionException;

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

    public List<Parameter> getParameters() {
        return parameters;
    }

    public int getPosition() {
        return position;
    }

    public int getNextInstructionPosition() {
        return position + parameters.size() + 1;
    }

    public InstructionResult execute(long[] code, long ...inputValues) throws InvalidPositionException {
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
        private long value;
        private ParameterMode mode;

        Parameter(long value, ParameterMode mode) {
            this.value = value;
            this.mode = mode;
        }

        public long getValue() {
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
        OptionalLong output;
        OptionalInt newPosition;


        private InstructionResult(OptionalLong output, OptionalInt newPosition) {
            this.output = output;
            this.newPosition = newPosition;
        }

        public static InstructionResult emptyInstructionResult() {
            return new InstructionResult(OptionalLong.empty(), OptionalInt.empty());
        }

        public static InstructionResult ofOutput(OptionalLong output) {
            return new InstructionResult(output, OptionalInt.empty());
        }

        public static InstructionResult ofNewPosition(OptionalInt newPosition) {
            return new InstructionResult(OptionalLong.empty(), newPosition);
        }

        public OptionalLong getOutput() {
            return output;
        }

        public OptionalInt getNewPosition() {
            return newPosition;
        }
    }

    enum ParameterMode {
        POSITION_MODE(0), IMMEDIATE_MODE(1);

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
                    throw new InvalidParameterModesException("For the ADD operation the 3rd parameter must be in position mode");
            }

            @Override
            public InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) throws InvalidPositionException {
                long result = getParameterValue(parameters.get(0), code) + getParameterValue(parameters.get(1), code);
                storeInstructionResult(result, parameters.get(2).value, code);

                return InstructionResult.emptyInstructionResult();
            }
        },

        MULTIPLY(2, 3) {
            @Override
            public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
                if (parameterList.get(2).mode == ParameterMode.IMMEDIATE_MODE)
                    throw new InvalidParameterModesException("For the MULTIPLY operation the 3rd parameter must be in position mode");
            }

            @Override
            public InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) throws InvalidPositionException {
                long result = getParameterValue(parameters.get(0), code) * getParameterValue(parameters.get(1), code);
                storeInstructionResult(result, parameters.get(2).value, code);

                return InstructionResult.emptyInstructionResult();
            }
        },

        INPUT(3, 1) {
            @Override
            public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
                if (parameterList.get(0).mode == ParameterMode.IMMEDIATE_MODE)
                    throw new InvalidParameterModesException("For the INPUT operation the parameter must be in position mode");
            }

            @Override
            public InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) throws InvalidPositionException {
                storeInstructionResult(inputValues[0], parameters.get(0).value, code);

                return InstructionResult.emptyInstructionResult();
            }
        },

        OUTPUT(4, 1) {
            @Override
            public void validateParameters(List<Parameter> parameterList) {
            }

            @Override
            public InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) throws InvalidPositionException {
                return InstructionResult.ofOutput(OptionalLong.of(getParameterValue(parameters.get(0), code)));
            }
        },
        HALT(99, 0) {
            public void validateParameters(List<Parameter> parameterList) {}

            public InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) {
                return InstructionResult.emptyInstructionResult();
            }
        },
        JUMP_IF_TRUE(5, 2) {
            @Override
            public void validateParameters(List<Parameter> parameterList) {
            }

            @Override
            public InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) throws InvalidPositionException {
                if (getParameterValue(parameters.get(0), code) != 0) {
                    return InstructionResult.ofNewPosition(OptionalInt.of((int) getParameterValue(parameters.get(1), code)));
                } else {
                    return InstructionResult.emptyInstructionResult();
                }
            }
        },
        JUMP_IF_FALSE(6, 2) {
            @Override
            public void validateParameters(List<Parameter> parameterList) {
            }

            @Override
            public InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) throws InvalidPositionException {
                if (getParameterValue(parameters.get(0), code) == 0) {
                    return InstructionResult.ofNewPosition(OptionalInt.of((int) getParameterValue(parameters.get(1), code)));
                } else {
                    return InstructionResult.emptyInstructionResult();
                }
            }
        },
        LESS_THAN(7, 3) {
            @Override
            public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
                if (parameterList.get(2).mode == ParameterMode.IMMEDIATE_MODE)
                    throw new InvalidParameterModesException("For the LESS_THAN operation the third parameter must be in position mode");
            }

            @Override
            public InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) throws InvalidPositionException {
                if (getParameterValue(parameters.get(0), code) < getParameterValue(parameters.get(1), code)) {
                    storeInstructionResult(1L, parameters.get(2).getValue(), code);
                } else {
                    storeInstructionResult(0L, parameters.get(2).getValue(), code);
                }

                return InstructionResult.emptyInstructionResult();
            }
        },
        EQUALS(8, 3) {
            @Override
            public void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException {
                if (parameterList.get(2).mode == ParameterMode.IMMEDIATE_MODE)
                    throw new InvalidParameterModesException("For the EQUALS operation the third parameter must be in position mode");
            }

            @Override
            public InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) throws InvalidPositionException {
                if (getParameterValue(parameters.get(0), code) == getParameterValue(parameters.get(1), code)) {
                    storeInstructionResult(1L, parameters.get(2).getValue(), code);
                } else {
                    storeInstructionResult(0L, parameters.get(2).getValue(), code);
                }

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

        protected long getParameterValue(Parameter parameter, long[] code) throws InvalidPositionException {
            if (parameter.getMode() == ParameterMode.IMMEDIATE_MODE) {
                return parameter.getValue();
            } else {
                if (parameter.getValue() < code.length) {
                    return code[(int) parameter.getValue()];
                } else {
                    throw new InvalidPositionException("Attempting to retrieve a value from an invalid position", parameter.getValue(), code.length);
                }
            }
        }

        protected void storeInstructionResult(long result, long position, long[] code) throws InvalidPositionException {
            if (position >= code.length || position < 0)
                throw new InvalidPositionException("Attempting to store a value to an invalid position", position, code.length);

            code[(int) position] = result;
        }



        public abstract void validateParameters(List<Parameter> parameterList) throws InvalidParameterModesException;
        public abstract InstructionResult executeInstruction(long[] code, List<Parameter> parameters, long ...inputValues) throws InvalidPositionException;

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

        public static Builder createInstruction(long opCodeAndParamModes, int position) throws InvalidOperationException, InvalidParameterModesException {
            final int opCode = (int) opCodeAndParamModes % 100;
            Optional<Operation> optionalOperation = Operation.fromString(Integer.toString(opCode));
            if(optionalOperation.isPresent()) {
                Operation operation = optionalOperation.get();
                List<ParameterMode> parameterModeList = getParameterModes(opCodeAndParamModes, operation.getNumParams());
                return new Builder(operation, parameterModeList, operation.getNumParams(), position);
            } else {
                throw new InvalidOperationException(opCode);
            }
        }

        public Builder withParameters(long ...parameters) {
            assert parameters.length == parameterModeList.size();

            Iterator<ParameterMode> it = parameterModeList.iterator();
            for (long param : parameters) {
                parameterList.add(new Parameter(param, it.next()));
            }

            return this;
        }

        public Instruction build() throws InvalidParameterModesException {
            operation.validateParameters(parameterList);
            return new Instruction(operation, parameterList, position);
        }

        private static List<ParameterMode> getParameterModes(long opCodeAndParamModes, int numParams) throws InvalidParameterModesException {
            List<ParameterMode> parameterModeList = new ArrayList<>();

            long remaining = opCodeAndParamModes/100;
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
