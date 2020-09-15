package com.challenge.day10;

import com.challenge.day10.exception.AsteroidBeltBuildException;
import com.challenge.library.geometry.model.Int2DCoord;
import static org.apache.commons.lang3.math.NumberUtils.compare;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;



public class AsteroidBelt {
    private static final char ASTEROID = '#';
    private static final char VOID = '.';

    private final Asteroid[][] asteroidGrid; // [X][Y]
    private final int gridWidth, gridHeight;
    private final List<Asteroid> asteroidList;

    public AsteroidBelt(Asteroid[][] asteroidGrid) {
        this.asteroidGrid = asteroidGrid;
        gridWidth = asteroidGrid.length;
        gridHeight = asteroidGrid[0].length;

        asteroidList = createAsteroidListFromGrid();
    }

    public Asteroid calculateBestAsteroidLocationForMonitoring() {
        PriorityQueue<Asteroid> asteroidPriorityQueue = new PriorityQueue<>((Asteroid a1, Asteroid a2) -> compare(a2.getNumAsteroidsVisible(), a1.getNumAsteroidsVisible()));

        for (Asteroid asteroid : asteroidList) {
            asteroid.setNumAsteroidsVisible(calculateNumVisibleAsteroidsFrom(asteroid));
            asteroidPriorityQueue.offer(asteroid);
        }

        return asteroidPriorityQueue.peek();
    }

    private int calculateNumVisibleAsteroidsFrom(Asteroid origin) {
        int numVisibleAsteroids = 0;
        for (Asteroid asteroid : asteroidList) {
            if (asteroid != origin) {
                if (areMutuallyVisible(origin, asteroid)) {
                    numVisibleAsteroids++;
                }
            }
        }
        return numVisibleAsteroids;
    }

    private boolean areMutuallyVisible(Asteroid asteroid1, Asteroid asteroid2) {
        for (Int2DCoord coord : asteroid1.getIntermediatePositionsTo(asteroid2)) {
            if (asteroidGrid[coord.getX()][coord.getY()] != null) {
                return false;
            }
        }

        return true;
    }


    private List<Asteroid> createAsteroidListFromGrid() {
        List<Asteroid> asteroidList = new ArrayList<>();

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (asteroidGrid[x][y] != null) {
                    asteroidList.add(asteroidGrid[x][y]);
                }
            }
        }

        return asteroidList;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                stringBuilder.append(asteroidGrid[x][y] != null ? ASTEROID : VOID);
            }
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    public static class Builder {
        // [Y][X]
        private List<String> asteroidCharsMap;

        private Builder(List<String> asteroidCharsMap) {
            this.asteroidCharsMap = asteroidCharsMap;
        }

        public static Builder createAsteroidBelt(List<String> asteroidCharsMap) throws AsteroidBeltBuildException {
            checkAsteroidCharsMap(asteroidCharsMap);
            return new Builder(asteroidCharsMap);
        }

        public AsteroidBelt build() throws AsteroidBeltBuildException {
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
                            throw new AsteroidBeltBuildException(row[x], x, y);
                    }
                }
            }

            return new AsteroidBelt(asteroidGrid);
        }

        private static void checkAsteroidCharsMap(List<String> asteroidCharsMap) throws AsteroidBeltBuildException {
            Objects.requireNonNull(asteroidCharsMap);

            if (asteroidCharsMap.isEmpty())
                throw new AsteroidBeltBuildException("The asteroid chars map can't be an empty list");

            int previousRowLength = -1;
            for (String mapRow : asteroidCharsMap) {
                if (mapRow.length() == 0)
                    throw new AsteroidBeltBuildException("The rows in the asteroid chars map can't be an empty string");

                if (previousRowLength != -1 && mapRow.length() != previousRowLength)
                    throw new AsteroidBeltBuildException("All the rows in the asteroid chars map must be the same size");

                previousRowLength = mapRow.length();
            }
        }
    }
}
