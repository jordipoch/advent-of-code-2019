package com.challenge.day10;

import static com.challenge.day10.AsteroidVaporizationListGenerator.Builder.createAsteroidVaporizationListGenerator;

import com.challenge.day10.util.exception.AsteroidException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.challenge.day10.util.AsteroidUtil.*;
import static com.challenge.library.files.TextFileReader.readAllLinesFromFile;
import static org.testng.Assert.*;

public class AsteroidVaporizationListGeneratorTest {
    private static Path resourcePath = Paths.get("resources", "com", "challenge", "day10");

    @Test
    public void testGenerateSimple() {
        Asteroid asteroidWithLaser = new Asteroid(1, 3);
        List<Asteroid> asteroidList = Arrays.asList(new Asteroid(0,0), new Asteroid(4,0), new Asteroid(2, 2),
                                                    new Asteroid(3, 3), new Asteroid(1, 4), asteroidWithLaser);

        AsteroidVaporizationListGenerator generator = createAsteroidVaporizationListGenerator(asteroidWithLaser, asteroidList).build();
        List<Asteroid> vaporizationList = generator.generate();

        System.out.println("Vaporization list: " + vaporizationList);

        List<Asteroid> expected = generateExpectedList(asteroidList, 2, 3, 4, 0, 1);

        assertEquals(vaporizationList, expected);
    }

    @Test
    public void testGenerateMediumComplexity() throws IOException, AsteroidException {
        List<String> asteroidBeltCharMap = readAllLinesFromFile(resourcePath.resolve("part2TestInput.txt"));
        checkAsteroidCharsMap(asteroidBeltCharMap);
        Asteroid[][] asteroidsArray = createAsteroidArrayFromAsteroidCharsMap(asteroidBeltCharMap);
        Asteroid asteroidWithLaser = asteroidsArray[8][3];
        List<Asteroid> asteroidList = createAsteroidListFromGrid(asteroidsArray);


        List<Asteroid> expected = generateExpectedListFromCoordArray(new int[][] {
                    {8,1}, {9,0}, {9,1}, {10,0}, {9,2}, {11,1}, {12,1}, {11,2}, {15,1}, {12,2},
                    {13,2}, {14,2}, {15,2}, {12,3}, {16,4}, {15,4}, {10,4}, {4,4}, {2,4}, {2,3},
                    {0,2}, {1,2}, {0,1}, {1,1}, {5,2}, {1,0}, {5,1}, {6,1}, {6,0}, {7,0},
                    {8,0}, {10,1}, {14,0}, {16,1}, {13,3}, {14,3}
                });

        AsteroidVaporizationListGenerator generator = createAsteroidVaporizationListGenerator(asteroidWithLaser, asteroidList).build();
        List<Asteroid> vaporizationList = generator.generate();

        System.out.println("Vaporization list: " + vaporizationList);

        assertEquals(vaporizationList, expected);
    }

    private List<Asteroid> generateExpectedList(List<Asteroid> source, int... indexes) {
        return Arrays.stream(indexes).mapToObj(source::get).collect(Collectors.toList());
    }

    private List<Asteroid> generateExpectedListFromCoordArray(int[][] coordArray) {
        return Arrays.stream(coordArray)
                .map(coord -> new Asteroid(coord[0], coord[1]))
                .collect(Collectors.toList());
    }
}