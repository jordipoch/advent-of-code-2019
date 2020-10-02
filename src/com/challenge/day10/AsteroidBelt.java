package com.challenge.day10;

import com.challenge.day10.exception.AsteroidBeltBuildException;
import com.challenge.day10.util.exception.AsteroidException;
import com.challenge.library.geometry.model.Int2DPoint;

import static com.challenge.day10.AsteroidVaporizationListGenerator.Builder.createAsteroidVaporizationListGenerator;
import static com.challenge.day10.util.AsteroidUtil.checkAsteroidCharsMap;
import static com.challenge.day10.util.AsteroidUtil.createAsteroidArrayFromAsteroidCharsMap;
import static com.challenge.day10.util.AsteroidUtil.createAsteroidListFromGrid;

import static org.apache.commons.lang3.math.NumberUtils.compare;

import java.util.List;
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

        asteroidList = createAsteroidListFromGrid(asteroidGrid);
    }

    public Asteroid calculateBestAsteroidLocationForMonitoring() {
        PriorityQueue<Asteroid> asteroidPriorityQueue = new PriorityQueue<>((Asteroid a1, Asteroid a2) -> compare(a2.getNumAsteroidsVisible(), a1.getNumAsteroidsVisible()));

        for (Asteroid asteroid : asteroidList) {
            asteroid.setNumAsteroidsVisible(calculateNumVisibleAsteroidsFrom(asteroid));
            asteroidPriorityQueue.offer(asteroid);
        }

        return asteroidPriorityQueue.peek();
    }

    public List<Asteroid> createAsteroidVaporizationOrderList(Asteroid laserPosition) {
        AsteroidVaporizationListGenerator vaporizationListGenerator = createAsteroidVaporizationListGenerator(laserPosition, asteroidList).build();
        return vaporizationListGenerator.generate();
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
        for (Int2DPoint coord : asteroid1.getIntermediatePositionsTo(asteroid2)) {
            if (asteroidGrid[coord.getX()][coord.getY()] != null) {
                return false;
            }
        }

        return true;
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
            try {
                checkAsteroidCharsMap(asteroidCharsMap);
            } catch (AsteroidException e) {
                throw new AsteroidBeltBuildException("Error validation the asteroid chars map", e);
            }
            return new Builder(asteroidCharsMap);
        }

        public AsteroidBelt build() throws AsteroidBeltBuildException {
            try {
                return new AsteroidBelt(createAsteroidArrayFromAsteroidCharsMap(asteroidCharsMap));
            } catch (AsteroidException e) {
                throw new AsteroidBeltBuildException("Error building the asteroid belt", e);
            }
        }
    }
}
