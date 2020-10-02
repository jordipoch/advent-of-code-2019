package com.challenge.day10.util;

import com.challenge.day10.Asteroid;
import com.challenge.day10.exception.AsteroidBeltBuildException;
import com.challenge.day10.util.exception.AsteroidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsteroidUtil {
    private static final char ASTEROID = '#';
    private static final char VOID = '.';

    public static void checkAsteroidCharsMap(List<String> asteroidCharsMap) throws AsteroidException {
        Objects.requireNonNull(asteroidCharsMap);

        if (asteroidCharsMap.isEmpty())
            throw new AsteroidException("The asteroid chars map can't be an empty list");

        int previousRowLength = -1;
        for (String mapRow : asteroidCharsMap) {
            if (mapRow.length() == 0)
                throw new AsteroidException("The rows in the asteroid chars map can't be an empty string");

            if (previousRowLength != -1 && mapRow.length() != previousRowLength)
                throw new AsteroidException("All the rows in the asteroid chars map must be the same size");

            previousRowLength = mapRow.length();
        }
    }

    public static Asteroid[][] createAsteroidArrayFromAsteroidCharsMap(List<String> asteroidCharsMap) throws AsteroidException {
        Asteroid[][] asteroidGrid = new Asteroid[asteroidCharsMap.get(0).length()][asteroidCharsMap.size()];

        for(int y = 0; y < asteroidCharsMap.size(); y++) {
            char[] row = asteroidCharsMap.get(y).toCharArray();
            for (int x = 0; x < row.length; x++) {
                switch (row[x]) {
                    case ASTEROID:
                        asteroidGrid[x][y] = new Asteroid(x, y);
                        break;
                    case VOID:
                        break;
                    default:
                        throw new AsteroidException(row[x], x, y);
                }
            }
        }

        return asteroidGrid;
    }

    public static List<Asteroid> createAsteroidListFromGrid(Asteroid[][] asteroidGrid) {
        List<Asteroid> asteroidList = new ArrayList<>();
        final int gridWidth = asteroidGrid.length;
        final int gridHeight = asteroidGrid[0].length;

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (asteroidGrid[x][y] != null) {
                    asteroidList.add(asteroidGrid[x][y]);
                }
            }
        }

        return asteroidList;
    }
}
