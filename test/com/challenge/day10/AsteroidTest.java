package com.challenge.day10;

import com.challenge.library.geometry.model.Int2DCoord;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class AsteroidTest {

    @Test
    public void testGetIntermediatePositionsTo() {
        calculateAndCheckIntermediatePositions(new Asteroid(1, 1), new Asteroid(4, 4),
                createPositionsListFrom2DArray(new int[][] {{2, 2}, {3, 3}}));
        calculateAndCheckIntermediatePositions(new Asteroid(2, 1), new Asteroid(6, 3),
                createPositionsListFrom2DArray(new int[][] {{4, 2}}));
        calculateAndCheckIntermediatePositions(new Asteroid(6, 3), new Asteroid(2, 1),
                createPositionsListFrom2DArray(new int[][] {{4, 2}}));
        calculateAndCheckIntermediatePositions(new Asteroid(0, 0), new Asteroid(1, 0),
                createPositionsListFrom2DArray(new int[][] {}));
        calculateAndCheckIntermediatePositions(new Asteroid(0, 0), new Asteroid(5, 0),
                createPositionsListFrom2DArray(new int[][] {{1, 0}, {2, 0}, {3, 0}, {4, 0},}));
        calculateAndCheckIntermediatePositions(new Asteroid(0, 5), new Asteroid(0, 0),
                createPositionsListFrom2DArray(new int[][] {{0, 4}, {0, 3}, {0, 2}, {0, 1}}));
        calculateAndCheckIntermediatePositions(new Asteroid(5, 0), new Asteroid(0, 5),
                createPositionsListFrom2DArray(new int[][] {{4, 1}, {3, 2}, {2, 3}, {1, 4}}));
    }

    private void calculateAndCheckIntermediatePositions(Asteroid origin, Asteroid destination, List<Int2DCoord> expectedResult) {
        System.out.print(String.format("Intermediate position from %s to %s: ", origin.getPosition(), destination.getPosition()));
        List<Int2DCoord> intermediatePositions = origin.getIntermediatePositionsTo(destination);
        System.out.println(intermediatePositions);

        assertEquals(intermediatePositions, expectedResult);
    }

    private List<Int2DCoord> createPositionsListFrom2DArray(int[][] positionArray) {
        List<Int2DCoord> positionsList = new ArrayList<>();

        for(int[] position : positionArray) {
            positionsList.add(new Int2DCoord(position[0], position[1]));
        }

        return positionsList;
    }
}