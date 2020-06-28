package com.challenge.day6;

import com.challenge.day6.exception.LocalOrbitsReadingException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import java.util.LinkedHashMap;

public class LocalOrbitsReaderTest {
    @Test
    public void testCorrectReading() throws LocalOrbitsReadingException {
        String[][] expectedLocalOrbitsArray = {
                {"COM", "B"},
                {"B", "C"},
                {"C", "D"},
                {"D", "E"},
                {"E", "F"},
                {"B", "G"},
                {"G", "H"},
                {"D", "I"},
                {"E", "J"},
                {"J", "K"},
                {"K", "L"}
        };

        LinkedHashMap<String, String> expectedLocalOrbitsMap = new LinkedHashMap<>();

        for (String[] orbit : expectedLocalOrbitsArray)
            expectedLocalOrbitsMap.put(orbit[1], orbit[0]);

        LinkedHashMap<String, String> readLocalOrbits = LocalOrbitsReader.readLocalOrbits("inputTest1.txt");

        System.out.println("Read orbits: " + readLocalOrbits);
        System.out.println("Expected orbits: " + expectedLocalOrbitsMap);

        assertEquals(expectedLocalOrbitsMap, readLocalOrbits, "The read local orbits map doesn't match the expected one");
    }

    @Test(expectedExceptions = {com.challenge.day6.exception.LocalOrbitsReadingException.class})
    public void testDuplicatesError() throws LocalOrbitsReadingException {
        LinkedHashMap<String, String> readLocalOrbits = LocalOrbitsReader.readLocalOrbits("inputTest2.txt");
    }
}
