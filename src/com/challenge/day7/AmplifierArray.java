package com.challenge.day7;

import com.challenge.day7.exception.AmplificationSignalCalculationException;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.exception.IntComputerException;

import java.math.BigInteger;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class AmplifierArray {

    private IntCodeComputer[] amplifiers;
    private boolean feedbackLoopMode;

    private AmplifierArray(IntCodeComputer[] amplifiers, boolean feedbackLoopMode) {
        this.amplifiers = amplifiers;
        this.feedbackLoopMode = feedbackLoopMode;
    }

    public long calculateAmplificationSignal() throws AmplificationSignalCalculationException {
        if (feedbackLoopMode)
            return calculateAmplificationSignalWithFeedbackLoop();
        else
            return calculateAmplificationSignalWithoutFeedbackLoop();
    }

    public long calculateAmplificationSignalWithoutFeedbackLoop() throws AmplificationSignalCalculationException {
        long output = 0;

        for (int i = 0; i < amplifiers.length; i++) {
            IntCodeComputer amplifier = getAndPrepareAmplifier(i, output);
            try {
                List<BigInteger> executionOutput = amplifier.executeCode().getOutput();
                if (executionOutput.isEmpty())
                    throw new AmplificationSignalCalculationException("No output returned from execution in amplifier");
                output = executionOutput.get(0).longValue();
            } catch (IntComputerException e) {
                throw new AmplificationSignalCalculationException(String.format("Error executing code in amplifier %d", i), e);
            }
        }

        return output;
    }

    public long calculateAmplificationSignalWithFeedbackLoop() throws AmplificationSignalCalculationException {
        long output = 0;
        long lastLoopOutput = 0;
        int loopCount = 0;
        boolean halt = false;

        while (!halt) {
            loopCount++;
            System.out.println("Loop " + loopCount);
            for (int i = 0; i < amplifiers.length; i++) {

                IntCodeComputer amplifier = getAndPrepareAmplifier(i, output);
                try {
                    List<BigInteger> executionOutput = amplifier.executeCode().getOutput();
                    if (executionOutput.isEmpty()) {
                        halt = true;
                        System.out.println(String.format("Halt reached in amplifier %d, loop %d", i, loopCount));
                        break;
                    } else
                        output = executionOutput.get(0).longValue();
                } catch (IntComputerException e) {
                    throw new AmplificationSignalCalculationException(String.format("Error executing code in amplifier %d, loop %d", i, loopCount), e);
                }
            }

            if (!halt)
                lastLoopOutput = output;
        }

        return lastLoopOutput;
    }

    private IntCodeComputer getAndPrepareAmplifier(int amplifierNumber, long input) {
        IntCodeComputer amplifier = amplifiers[amplifierNumber];
        amplifier.addInputValue(input);
        return amplifier;
    }

    public static class Builder {
        private final long[] code;
        private long[] phaseSettingSequence;
        private boolean feedbackLoopMode = false;

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

        public Builder withFeedbackLoopMode(boolean feedbackLoopMode) {
            this.feedbackLoopMode = feedbackLoopMode;
            return this;
        }

        public AmplifierArray build() {
            IntCodeComputer[] amplifiers = new IntCodeComputer[phaseSettingSequence.length];

            for (int i = 0; i < phaseSettingSequence.length; i++) {
                amplifiers[i] = IntCodeComputer.Builder.createNewIntCodeComputer(code)
                        .withInputValue(phaseSettingSequence[i])
                        .withFeedbackLoopMode(feedbackLoopMode)
                        .build();
            }

            return new AmplifierArray(amplifiers, feedbackLoopMode);
        }
    }
}
