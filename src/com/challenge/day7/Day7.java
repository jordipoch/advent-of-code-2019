package com.challenge.day7;

import com.challenge.day7.exception.ThrusterSingalCalculatorException;
import static com.challenge.day7.ThrusterSignalCalculator.Builder.createThrusterSignalCalculator;

public class Day7 {
    public static void main(String[] args) {

        try {
            ThrusterSignalCalculator thrusterSignalCalculator = createThrusterSignalCalculator("input.txt")
                    .withPhaseSettingsSequence(new long[]{0, 1, 2, 3, 4})
                    .build();

            long signalToThruster = thrusterSignalCalculator.calculateSignal();

            System.out.println("Max signal to the thrusters = " + signalToThruster);
        } catch (ThrusterSingalCalculatorException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
