package com.challenge.day7;

import com.challenge.day7.exception.AmplificationSignalCalculationException;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.exception.IntComputerException;

import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class AmplifierArray {

    IntCodeComputer.Builder[] intCodeComputerBuilders;

    private AmplifierArray(IntCodeComputer.Builder[] intCodeComputerBuilders) {
        this.intCodeComputerBuilders = intCodeComputerBuilders;
    }

    public long calculateAmplificationSignal() throws AmplificationSignalCalculationException {
        long output = 0;

        for (int i = 0; i < intCodeComputerBuilders.length; i++) {
            IntCodeComputer intCodeComputer = intCodeComputerBuilders[i].withInputValue(output).build();
            try {
                List<Long> executionOutput = intCodeComputer.executeCode();
                if (executionOutput.size() == 0)
                    throw new AmplificationSignalCalculationException("No output returned from execution in amplifier");
                output = executionOutput.get(0);
            } catch (IntComputerException e) {
                throw new AmplificationSignalCalculationException(String.format("Error executing code in amplifier %d", i), e);
            }
        }

        return output;
    }

    public static class Builder {
        private long[] code;
        private long[] phaseSettingSequence;

        private Builder(long[] code) {
            this.code = code;
        }

        public static Builder createAmplifierArray(long[] code) {
            if (isEmpty(code))
                throw new IllegalArgumentException("The int computer code array can't be empty or null");

            return new Builder(code);
        }

        public Builder withPhaseSettingSequence(long[] phaseSettingSequence) {
            if (isEmpty(phaseSettingSequence))
                throw new IllegalArgumentException("The phase setting sequence array can't be empty or null");

            this.phaseSettingSequence = phaseSettingSequence;
            return this;
        }

        public AmplifierArray build() {
            IntCodeComputer.Builder[] amplifiers = new IntCodeComputer.Builder[phaseSettingSequence.length];

            for (int i = 0; i < phaseSettingSequence.length; i++) {
                amplifiers[i] = CreateIntCodeComputerBuilder(phaseSettingSequence[i]);
            }

            return new AmplifierArray(amplifiers);
        }

        private IntCodeComputer.Builder CreateIntCodeComputerBuilder(long phaseSetting) {
            return IntCodeComputer.Builder.createNewIntCodeComputer(code)
                    .withInputValue(phaseSetting);
        }
    }
}
