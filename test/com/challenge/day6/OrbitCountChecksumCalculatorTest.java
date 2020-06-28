package com.challenge.day6;

import com.challenge.day6.exception.LocalOrbitsReadingException;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import java.util.LinkedHashMap;

import static com.challenge.day6.OrbitCountChecksumCalculator.Builder.createOrbitCountChecksumCalculator;

public class OrbitCountChecksumCalculatorTest {

    @Test
    public void testCorrectChecksumCalculation() throws LocalOrbitsReadingException {

        LinkedHashMap<String, String> localOrbits = LocalOrbitsReader.readLocalOrbits("inputTest1.txt");

        OrbitCountChecksumCalculator calculator = createOrbitCountChecksumCalculator(localOrbits).build();
        int checksum = calculator.calculateChecksum();

        System.out.println("Local orbits: " + localOrbits);
        System.out.println("Checksum: " + checksum);

        assertEquals(checksum, 42, "The calculated checksum doesn't match the expected one.");
    }
}
