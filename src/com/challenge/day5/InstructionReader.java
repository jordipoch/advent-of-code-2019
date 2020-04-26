package com.challenge.day5;

import com.challenge.day5.exception.EndOfCodeException;
import com.challenge.day5.exception.InvalidInstructionException;
import com.challenge.day5.exception.InvalidOperationException;
import com.challenge.day5.exception.InvalidParameterModesException;

import static com.challenge.day5.Instruction.Builder.*;

public class InstructionReader {
    long[] code;
    int pos;

    private InstructionReader(long[] code, int pos) {
        this.code = code;
        this.pos = pos;
    }

    public Instruction readInstruction() throws InvalidInstructionException, EndOfCodeException{
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

        private Builder(long[] code) {
            this.code = code;
        }

        public static Builder createInstructionReader(long[] code) {
            return new Builder(code);
        }

        public Builder startWithPosition(int pos) {
            this.pos = pos;
            return this;
        }

        public InstructionReader build() {
            return new InstructionReader(code, pos);
        }
    }
}
