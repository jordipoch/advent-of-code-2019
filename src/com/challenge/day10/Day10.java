package com.challenge.day10;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.challenge.day10.AsteroidBelt.Builder.createAsteroidBelt;
import static com.challenge.library.files.TextFileReader.readAllLinesFromFile;

public class Day10 {
    public static void main(String[] args) {
        runDay10Part1();
    }

    public static void runDay10Part1() {

        try {
            Path inputPath = Paths.get("resources", "com", "challenge", "day10", "input.txt");
            List<String> asteroidBeltCharMap = readAllLinesFromFile(inputPath);

            AsteroidBelt asteroidBelt = createAsteroidBelt(asteroidBeltCharMap).build();
            System.out.println("Asteroid belt:");
            System.out.println(asteroidBelt);

            Asteroid bestAsteroidLocation = asteroidBelt.calculateBestAsteroidLocationForMonitoring();

            System.out.println("Best location: " + bestAsteroidLocation);

        } catch (Exception e) {
            System.out.println("Error running day 10 challenge: " + e);
            e.printStackTrace();
        }
    }
}
