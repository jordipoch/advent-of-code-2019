package com.challenge.day6;

import com.challenge.day6.exception.LocalOrbitsReadingException;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import static com.challenge.day6.OrbitDistanceCalculator.Builder.createOrbitDistanceCalculator;

import java.util.LinkedHashMap;

public class OrbitDistanceCalculatorTest {
    @Test
    public void testCalculateDistance() throws LocalOrbitsReadingException {
        LinkedHashMap<String, String> localOrbits = LocalOrbitsReader.readLocalOrbits("inputTest1Part2.txt");

        OrbitDistanceCalculator distanceCalculator = createOrbitDistanceCalculator(localOrbits).withOriginAndDestination("YOU", "SAN").build();
        int distance = distanceCalculator.calculate();

        System.out.println("Distance between you and Santa: " + distance);
        assertEquals(distance, 4);
    }
}
