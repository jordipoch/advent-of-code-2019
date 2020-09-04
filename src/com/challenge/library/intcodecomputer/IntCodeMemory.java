package com.challenge.library.intcodecomputer;

import com.challenge.library.intcodecomputer.exception.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.challenge.library.intcodecomputer.Instruction.Builder.createInstruction;
import static com.challenge.library.utils.NumberUtils.convertToInt;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class IntCodeMemory {
    private int MAX_MEMORY_SIZE = 1_000_000;

    private List<BigInteger> memory;
    private int currentPos;
    private BigInteger relativeBaseOffset = BigInteger.ZERO;
    private boolean autoExpand;

    public IntCodeMemory(List<BigInteger> memory, int StartingPos, boolean autoExpand) {
        this.memory = memory;
        this.currentPos = StartingPos;
        this.autoExpand = autoExpand;
    }

    public Instruction readInstruction() throws InvalidInstructionException, EndOfCodeException {
        if (currentPos >= memory.size())
            throw new EndOfCodeException(currentPos, memory.size());

        int intInstruction = getIntValueFromInstruction(memory.get(currentPos));

        try {
            Instruction.Builder builder = createInstruction(intInstruction, currentPos);
            int numParams = builder.getNumParameters();
            if (numParams > 0) {
                if (currentPos + numParams >= memory.size())
                    throw new InvalidInstructionException(intInstruction, currentPos,
                            String.format("Instruction needs %d parameters, but only %d were found in the code", numParams, memory.size() - currentPos - 1));

                BigInteger[] params = new BigInteger[numParams];
                for (int i = 0; i < numParams; i++)
                    params[i] = memory.get(currentPos+i+1);

                builder = builder.withParameters(params);
            }

            Instruction instruction = builder.build();
            currentPos = instruction.getNextInstructionPosition();
            return instruction;
        } catch (InvalidOperationException | InvalidParameterModesException e) {
            throw new InvalidInstructionException(intInstruction, currentPos, e);
        }
    }

    public BigInteger getValue(BigInteger position) throws InvalidPositionException {
        return memory.get(convertMemoryPositionToIntAndCheck(position));
    }

    public void setValue(BigInteger value, BigInteger position) throws InvalidPositionException {
        memory.set(convertMemoryPositionToIntAndCheck(position), value);
    }

    public void setCurrentPos(BigInteger newPosition) throws InvalidPositionException {
        this.currentPos = getIntValueFromPosition(newPosition);
    }

    public BigInteger getRelativeBaseOffset() {
        return relativeBaseOffset;
    }

    public void incrementRelativeBaseOffset(BigInteger increment) {
        relativeBaseOffset = relativeBaseOffset.add(increment);
    }

    public List<BigInteger> getSnapshot() {
        return new ArrayList<>(memory);
    }

    private int convertMemoryPositionToIntAndCheck(BigInteger position) throws InvalidPositionException {
        final int intPosition = getIntValueFromPosition(position);

        if (intPosition >= memory.size()) {
            if (autoExpand)
                try {
                    autoExpandMemory(intPosition + 1);
                } catch (MemoryExpandException e) {
                    throw new InvalidPositionException("Attempting to store a value to an invalid position", intPosition, memory.size(), e);
                }
            else
                throw new InvalidPositionException("Attempting to store a value to an invalid position", intPosition, memory.size());
        }

        return intPosition;
    }

    private void autoExpandMemory(int newSize) throws MemoryExpandException {
        if (newSize > MAX_MEMORY_SIZE)
            throw new MemoryExpandException(newSize, MAX_MEMORY_SIZE);

        //System.out.println(String.format("Expanding code memory from %d to %d", memory.size(), newSize));

        final int currentSize = memory.size();
        for (int i = currentSize; i < newSize; i++)
            memory.add(BigInteger.ZERO);

        assert(memory.size() == newSize);
    }

    private int getIntValueFromInstruction(BigInteger instruction) throws InvalidInstructionException {
        int intInstruction = 0;
        try {
            intInstruction = convertToInt(instruction);
        } catch (IllegalArgumentException e) {
            throw new InvalidInstructionException(intInstruction, currentPos, e.getMessage());
        }

        if (intInstruction < 0)
            throw new InvalidInstructionException(intInstruction, currentPos, "Instruction can't be a negative number");

        return intInstruction;
    }

    private int getIntValueFromPosition(BigInteger position) throws InvalidPositionException {
        int intPosition;
        try {
            intPosition = convertToInt(position);
        } catch (IllegalArgumentException e) {
            throw new InvalidPositionException(e.getMessage());
        }

        if (intPosition < 0)
            throw new InvalidPositionException(intPosition, "A memory position can't be a negative value");

        return intPosition;
    }


    public static class Builder {
        private List<BigInteger> memory;
        private int startingPos;
        private boolean autoExpand;

        public Builder(List<BigInteger> memory) {
            this.memory = memory;
        }

        public static Builder createIntCodeMemory(long[] code) {
            checkCodeArray(code);

            List<BigInteger> memory = new ArrayList<>();
            for (long l : code)
                memory.add(BigInteger.valueOf(l));

            return new Builder(memory);
        }

        public static Builder createIntCodeMemory(BigInteger[] code) {
            checkCodeArray(code);

            List<BigInteger> memory = new ArrayList<>();
            Collections.addAll(memory, code);

            return new Builder(memory);
        }

        private static void checkCodeArray(long[] code) {
            requireNonNull(code, "The code array cannot be null");

            if (isEmpty(code)) {
                throw new IllegalArgumentException("The code array cannot be empty");
            }
        }

        private static void checkCodeArray(BigInteger[] code) {
            requireNonNull(code, "The code array cannot be null");

            if (isEmpty(code)) {
                throw new IllegalArgumentException("The code array cannot be empty");
            }
        }

        public Builder withInitialSize(int initialSize) {
            for (int i = memory.size(); i < initialSize; i++)
                memory.add(BigInteger.valueOf(0L));

            assert(memory.size() == initialSize);

            return this;
        }

        public Builder withStartingPos(int startingPos) {
            if (startingPos != 0)
                if (startingPos < 0 || startingPos >= memory.size())
                    throw new IllegalArgumentException(String.format("The starting position (%d) must be within range [0, %d]", startingPos, memory.size()));

            this.startingPos = startingPos;
            return this;
        }

        public Builder withAutoExpand() {
            this.autoExpand = true;

            return this;
        }

        public IntCodeMemory build() {
            return new IntCodeMemory(memory, startingPos, autoExpand);
        }
    }
}
