package com.challenge.day10;

import com.challenge.day10.exception.AsteroidBeltBuildException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.challenge.library.files.TextFileReader.readAllLinesFromFile;
import static org.testng.Assert.*;

import static com.challenge.day10.AsteroidBelt.Builder.createAsteroidBelt;

public class AsteroidBeltTest {
    private static Path resourcePath = Paths.get("resources", "com", "challenge", "day10");

    @Test
    public void simpleTest1() throws AsteroidBeltBuildException {
        List<String> asteroidBeltChars = Arrays.asList(
                                    ".#..#",
                                    ".....",
                                    "#####",
                                    "....#",
                                    "...##");

        calculateAndCheckBestAsteroidLocation(asteroidBeltChars, new Asteroid(3, 4, 8));
    }

    @Test
    public void test1() throws AsteroidBeltBuildException, IOException {
        calculateAndCheckBestAsteroidLocation("testInput1.txt", new Asteroid(5, 8, 33));
    }

    @Test
    public void test2() throws AsteroidBeltBuildException, IOException {
        calculateAndCheckBestAsteroidLocation("testInput2.txt", new Asteroid(1, 2, 35));
    }

    @Test
    public void test3() throws AsteroidBeltBuildException, IOException {
        calculateAndCheckBestAsteroidLocation("testInput3.txt", new Asteroid(6, 3, 41));
    }

    @Test
    public void test4() throws AsteroidBeltBuildException, IOException {
        calculateAndCheckBestAsteroidLocation("testInput4.txt", new Asteroid(11, 13, 210));
    }

    @Test
    public void testDay10Part1() throws AsteroidBeltBuildException, IOException {
        calculateAndCheckBestAsteroidLocation("input.txt", new Asteroid(25, 31, 329));
    }

    private void calculateAndCheckBestAsteroidLocation(String filename, Asteroid expected) throws AsteroidBeltBuildException, IOException {
        List<String> asteroidBeltCharMap = readAllLinesFromFile(resourcePath.resolve(filename));
        calculateAndCheckBestAsteroidLocation(asteroidBeltCharMap, expected);
    }

    private void calculateAndCheckBestAsteroidLocation(List<String> asteroidBeltCharMap, Asteroid expected) throws AsteroidBeltBuildException {
        AsteroidBelt asteroidBelt = createAsteroidBelt(asteroidBeltCharMap).build();
        System.out.println("Asteroid belt:");
        System.out.println(asteroidBelt);

        Asteroid bestAsteroidLocation = asteroidBelt.calculateBestAsteroidLocationForMonitoring();

        System.out.println("Best location: " + bestAsteroidLocation);
        assertEquals(bestAsteroidLocation, expected);
    }
}