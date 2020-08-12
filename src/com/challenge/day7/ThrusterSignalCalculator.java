package com.challenge.day7;

import com.challenge.day7.exception.AmplificationSignalCalculationException;
import com.challenge.day7.exception.ThrusterSingalCalculatorException;
import com.challenge.library.intcodecomputer.IntCodeLoader;
import org.apache.commons.lang3.math.NumberUtils;

import static com.challenge.day7.AmplifierArray.Builder.createAmplifierArray;
import static com.challenge.day7.SequencePermutator.Builder.createSequencePermutator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class ThrusterSignalCalculator {
    private long[] code;
    private SequencePermutator sequencePermutator;
    private boolean feedbackLoopMode;

    private ThrusterSignalCalculator(long[] code, SequencePermutator sequencePermutator, boolean feedbackLoopMode) {
        this.code = code;
        this.sequencePermutator = sequencePermutator;
        this.feedbackLoopMode = feedbackLoopMode;
    }

    public long calculateSignal() throws ThrusterSingalCalculatorException {
        long maxSignalToThruster = 0;
        long[] maxSignalPhaseSetting = null;

        while (sequencePermutator.hasMoreSequences()) {
            long[] phaseSettingsSequencePermutation = sequencePermutator.getNextSequence();
            try {
                long signal = calculateAmplifierArraySignal(phaseSettingsSequencePermutation);

                System.out.println(String.format("Signal %d obtained from phase settings permutation %s", signal, Arrays.toString(phaseSettingsSequencePermutation)));

                if (signal > maxSignalToThruster) {
                    maxSignalToThruster = signal;
                    maxSignalPhaseSetting = Arrays.copyOf(phaseSettingsSequencePermutation, phaseSettingsSequencePermutation.length);
                }
            } catch (AmplificationSignalCalculationException e) {
                throw new ThrusterSingalCalculatorException(String.format("Error calculating signal for phase settings permutation %s", Arrays.toString(phaseSettingsSequencePermutation)), e);
            }
        }

        System.out.println(String.format("%nMax signal %d generated from phase settings %s", maxSignalToThruster, Arrays.toString(maxSignalPhaseSetting)));

        return maxSignalToThruster;
    }

    private long calculateAmplifierArraySignal(long[] phaseSettingSequence) throws AmplificationSignalCalculationException {
        AmplifierArray amplifierArray = createAmplifierArray(code)
                .withPhaseSettingSequence(phaseSettingSequence)
                .withFeedbackLoopMode(feedbackLoopMode)
                .build();

        return amplifierArray.calculateAmplificationSignal();
    }

    public static class Builder {
        private String inputFileName;
        private long[] phaseSettingsSequence;
        private boolean feedbackLoopMode;


        private Builder(String inputFileName) {
            this.inputFileName = inputFileName;
        }

        public static Builder createThrusterSignalCalculator(String inputFileName) {
            return new Builder(inputFileName);
        }

        public Builder withPhaseSettingsSequence(long[] phaseSettingsSequence) {
            this.phaseSettingsSequence = phaseSettingsSequence;
            return this;
        }

        public Builder withFeedbackLoopMode() {
            this.feedbackLoopMode = true;
            return this;
        }

        public ThrusterSignalCalculator build() throws ThrusterSingalCalculatorException {
            try {
                long[] intCode = IntCodeLoader
                        .getInstance()
                        .loadIntCodeFromFile(Paths.get("resources", "com", "challenge", "day7"), inputFileName);

                System.out.println(String.format("Int computer code read: %s", Arrays.toString(intCode)));
                System.out.println(String.format("Initial phase settings sequence: %s", Arrays.toString(phaseSettingsSequence)));

                return new ThrusterSignalCalculator(intCode, createSequencePermutator(phaseSettingsSequence).build(), feedbackLoopMode);
            } catch (IOException e) {
                throw new ThrusterSingalCalculatorException(String.format("Can't open file \"%s\"", inputFileName), e);
            }
        }
    }
}
