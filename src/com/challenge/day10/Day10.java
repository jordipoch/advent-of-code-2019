package com.challenge.day10;

import com.challenge.day10.exception.Day10ExecutionException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.challenge.day10.AsteroidBelt.Builder.createAsteroidBelt;
import static com.challenge.library.files.TextFileReader.readAllLinesFromFile;

public class Day10 {
    public static void main(String[] args) {
        try {
            runDay10Part1();
            runDay10Part2();
        } catch (Day10ExecutionException e) {
            System.out.println("Error running day 10 challenge: " + e);
            e.printStackTrace();
        }
    }

    public static int runDay10Part1() throws Day10ExecutionException {
        try {
            Path inputPath = Paths.get("resources", "com", "challenge", "day10", "input.txt");
            List<String> asteroidBeltCharMap = readAllLinesFromFile(inputPath);

            AsteroidBelt asteroidBelt = createAsteroidBelt(asteroidBeltCharMap).build();
            System.out.println("Asteroid belt:");
            System.out.println(asteroidBelt);

            Asteroid bestAsteroidLocation = asteroidBelt.calculateBestAsteroidLocationForMonitoring();
            System.out.println(String.format("Best location: %s, with %d asteroids detected.", bestAsteroidLocation, bestAsteroidLocation.getNumAsteroidsVisible()));

            return bestAsteroidLocation.getNumAsteroidsVisible();
        } catch (Exception e) {
            throw new Day10ExecutionException("Error running day 10 challenge, part 1", e);
        }
    }

    public static int runDay10Part2() throws Day10ExecutionException {

        try {
            Path inputPath = Paths.get("resources", "com", "challenge", "day10", "input.txt");
            List<String> asteroidBeltCharMap = readAllLinesFromFile(inputPath);

            AsteroidBelt asteroidBelt = createAsteroidBelt(asteroidBeltCharMap).build();
            System.out.println("Asteroid belt:");
            System.out.println(asteroidBelt);

            Asteroid bestAsteroidLocation = asteroidBelt.calculateBestAsteroidLocationForMonitoring();
            System.out.println(String.format("Best location: %s, with %d asteroids detected.", bestAsteroidLocation, bestAsteroidLocation.getNumAsteroidsVisible()));

            List<Asteroid> vaporizationList = asteroidBelt.createAsteroidVaporizationOrderList(bestAsteroidLocation);
            Asteroid asteroid200th = vaporizationList.get(199);
            int score = asteroid200th.getPosition().getX() * 100 + asteroid200th.getPosition().getY();

            System.out.println("Vaporization list: " + vaporizationList);
            System.out.println(String.format("200th asteroid to be vaporized: %s. Score: %d", asteroid200th, score));

            return score;
        } catch (Exception e) {
            throw new Day10ExecutionException("Error running day 10 challenge, part 1", e);
        }
    }
}
