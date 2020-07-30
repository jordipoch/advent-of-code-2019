package com.challenge.day7;

import static com.challenge.day7.ThrusterSignalCalculator.Builder.createThrusterSignalCalculator;
import com.challenge.day7.exception.ThrusterSingalCalculatorException;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ThrusterSignalCalculationTest {
    @Test
    public void testSimpleCalculation1() throws ThrusterSingalCalculatorException {
        ThrusterSignalCalculator thrusterSignalCalculator = createThrusterSignalCalculator("testInput1.txt")
                .withPhaseSettingsSequence(new long[] {0, 1, 2, 3, 4})
                .build();

        long signalToThruster = thrusterSignalCalculator.calculateSignal();

        assertEquals(signalToThruster, 43210);
    }

    @Test
    public void testSimpleCalculation2() throws ThrusterSingalCalculatorException {
        ThrusterSignalCalculator thrusterSignalCalculator = createThrusterSignalCalculator("testInput2.txt")
                .withPhaseSettingsSequence(new long[] {0, 1, 2, 3, 4})
                .build();

        long signalToThruster = thrusterSignalCalculator.calculateSignal();

        assertEquals(signalToThruster, 54321);
    }

    @Test
    public void testSimpleCalculation3() throws ThrusterSingalCalculatorException {
        ThrusterSignalCalculator thrusterSignalCalculator = createThrusterSignalCalculator("testInput3.txt")
                .withPhaseSettingsSequence(new long[] {0, 1, 2, 3, 4})
                .build();

        long signalToThruster = thrusterSignalCalculator.calculateSignal();

        assertEquals(signalToThruster, 65210);
    }
}
