package com.challenge.day6;

import com.challenge.day6.exception.LocalOrbitsReadingException;
import static com.challenge.day6.OrbitCountChecksumCalculator.Builder.createOrbitCountChecksumCalculator;
import static com.challenge.day6.OrbitDistanceCalculator.Builder.createOrbitDistanceCalculator;

import java.util.LinkedHashMap;

public class Day6 {
    public static void main(String[] args) {
        LinkedHashMap<String, String> localOrbits;
        try {
            localOrbits = LocalOrbitsReader.readLocalOrbits("input.txt");
        } catch (LocalOrbitsReadingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        day6Part1(localOrbits);
        day6Part2(localOrbits);
    }

    private static void day6Part1(LinkedHashMap<String, String> localOrbits) {

        OrbitCountChecksumCalculator calculator = createOrbitCountChecksumCalculator(localOrbits).build();
        int checksum = calculator.calculateChecksum();

        System.out.println("Local orbits map: " + localOrbits);
        System.out.println("Checksum: " + checksum);
    }

    private static void day6Part2(LinkedHashMap<String, String> localOrbits) {
        OrbitDistanceCalculator calculator = createOrbitDistanceCalculator(localOrbits)
                .withOriginAndDestination("YOU", "SAN")
                .build();

        int distance = calculator.calculate();

        System.out.println("Distance between You and Santa: " + distance);
    }
}
