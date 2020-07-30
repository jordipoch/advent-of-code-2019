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
    long[] code;
    SequencePermutator sequencePermutator;

    private ThrusterSignalCalculator(long[] code, SequencePermutator sequencePermutator) {
        this.code = code;
        this.sequencePermutator = sequencePermutator;
    }

    public long calculateSignal() throws ThrusterSingalCalculatorException {
        long maxSignalToThruster = 0;

        while (sequencePermutator.hasMoreSequences()) {
            long[] phaseSettingsSequencePermutation = sequencePermutator.getNextSequence();
            try {
                long signal = calculateAmplifierArraySignal(phaseSettingsSequencePermutation);

                System.out.println(String.format("Signal %d obtained from phase settings permutation %s", signal, Arrays.toString(phaseSettingsSequencePermutation)));

                maxSignalToThruster = NumberUtils.max(signal, maxSignalToThruster);
            } catch (AmplificationSignalCalculationException e) {
                throw new ThrusterSingalCalculatorException(String.format("Error calculating signal for phase settings permutation %s", Arrays.toString(phaseSettingsSequencePermutation)), e);
            }
        }

        return maxSignalToThruster;
    }

    private long calculateAmplifierArraySignal(long[] phaseSettingSequence) throws AmplificationSignalCalculationException {
        AmplifierArray amplifierArray = createAmplifierArray(code)
                .withPhaseSettingSequence(phaseSettingSequence)
                .build();

        return amplifierArray.calculateAmplificationSignal();
    }

    public static class Builder {
        String inputFileName;
        long[] phaseSettingsSequence;

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

        public ThrusterSignalCalculator build() throws ThrusterSingalCalculatorException {
            try {
                long[] intCode = IntCodeLoader
                        .getInstance()
                        .loadIntCodeFromFile(Paths.get("resources", "com", "challenge", "day7"), inputFileName);

                System.out.println(String.format("Int computer code read: %s", Arrays.toString(intCode)));
                System.out.println(String.format("Initial phase settings sequence: %s", Arrays.toString(phaseSettingsSequence)));

                return new ThrusterSignalCalculator(intCode, createSequencePermutator(phaseSettingsSequence).build());
            } catch (IOException e) {
                throw new ThrusterSingalCalculatorException(String.format("Can't open file \"%s\"", inputFileName), e);
            }
        }
    }
}
