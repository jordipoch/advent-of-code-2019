package com.challenge.day7;

import com.challenge.day7.exception.ThrusterSingalCalculatorException;
import static com.challenge.day7.ThrusterSignalCalculator.Builder.createThrusterSignalCalculator;

public class Day7 {
    public static void main(String[] args) {

        day7Part1();

        day7Part2();
    }

    public static long day7Part1() {
        long signalToThruster = -1;
        try {
            ThrusterSignalCalculator thrusterSignalCalculator = createThrusterSignalCalculator("input.txt")
                    .withPhaseSettingsSequence(new long[]{0, 1, 2, 3, 4})
                    .build();

            signalToThruster = thrusterSignalCalculator.calculateSignal();

            System.out.println("%nMax signal to the thrusters = " + signalToThruster);
        } catch (ThrusterSingalCalculatorException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return signalToThruster;
    }

    public static long day7Part2() {
        long signalToThruster = -1;
        try {
            ThrusterSignalCalculator thrusterSignalCalculator = createThrusterSignalCalculator("input.txt")
                    .withPhaseSettingsSequence(new long[]{5, 6, 7, 8, 9})
                    .withFeedbackLoopMode()
                    .build();

            signalToThruster = thrusterSignalCalculator.calculateSignal();

            System.out.println("%nMax signal to the thrusters = " + signalToThruster);
        } catch (ThrusterSingalCalculatorException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return signalToThruster;
    }
}
