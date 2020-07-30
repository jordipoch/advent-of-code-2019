package com.challenge.day7;

import com.challenge.day7.exception.AmplificationSignalCalculationException;
import org.testng.annotations.Test;

import static com.challenge.day7.AmplifierArray.Builder.createAmplifierArray;
import static org.testng.Assert.*;

public class AmplifierArrayTest {
    @Test
    public void test1CalculateAmplificationSignal() throws AmplificationSignalCalculationException {
        long[] code = {3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0};
        long[] phaseSequence = {4,3,2,1,0};

       testCalculation(code, phaseSequence, 43210);
    }

    @Test
    public void test2CalculateAmplificationSignal() throws AmplificationSignalCalculationException {
        long[] code = {3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0};
        long[] phaseSequence = {0,1,2,3,4};

        testCalculation(code, phaseSequence, 54321);
    }

    @Test
    public void test3CalculateAmplificationSignal() throws AmplificationSignalCalculationException {
        long[] code = {3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0};
        long[] phaseSequence = {1,0,4,3,2};

        testCalculation(code, phaseSequence, 65210);
    }

    private void testCalculation(long[] code, long[] phaseSequence, long expected) throws AmplificationSignalCalculationException {
        AmplifierArray amplifierArray = createAmplifierArray(code)
                .withPhaseSettingSequence(phaseSequence)
                .build();

        long amplifiedSignal = amplifierArray.calculateAmplificationSignal();
        System.out.println("Amplified signal: " + amplifiedSignal);

        assertEquals(amplifiedSignal, expected, "The amplified signal calculated doesn't match the expected one");
    }
}
