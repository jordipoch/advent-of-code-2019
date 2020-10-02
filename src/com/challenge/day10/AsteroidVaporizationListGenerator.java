package com.challenge.day10;

import com.challenge.library.geometry.model.Int2DVector;
import static com.challenge.library.utils.NumberUtils.format;

import java.util.*;

public class AsteroidVaporizationListGenerator {
    private Asteroid vaporizationLaser;
    private List<Asteroid> asteroidList;

    private List<AsteroidListAtAngle> vaporizationBuilderList = new ArrayList<>();
    private Map<String, AsteroidListAtAngle> vaporizationMap = new HashMap<>();

    private AsteroidVaporizationListGenerator(Asteroid vaporizationLaser, List<Asteroid> asteroidList) {
        this.vaporizationLaser = vaporizationLaser;
        this.asteroidList = asteroidList;
    }

    public List<Asteroid> generate() {
        asteroidList.stream().forEach(asteroid -> addAsteroid(asteroid));

        vaporizationBuilderList.sort(Comparator.comparingDouble(AsteroidListAtAngle::getAngle));
        vaporizationBuilderList.stream().forEach( a -> a.sortListByDistance());

        return buildVaporizationList();
    }

    private void addAsteroid(Asteroid asteroid) {
        if (asteroid.equals(vaporizationLaser)) return;

        asteroid.setAngle(Int2DVector.UP.angleWith(new Int2DVector(vaporizationLaser.getPosition(), asteroid.getPosition())));
        asteroid.setDistanceToLaser(vaporizationLaser.getPosition().normalizedDistanceWith(asteroid.getPosition()));

        if (vaporizationMap.containsKey(format(asteroid.getAngle()))) {
            AsteroidListAtAngle asteroidListAtAngle = vaporizationMap.get(format(asteroid.getAngle()));
            asteroidListAtAngle.addAsteroid(asteroid);
        } else {
            AsteroidListAtAngle asteroidListAtAngle = new AsteroidListAtAngle(asteroid.getAngle(), asteroid);
            vaporizationMap.put(format(asteroid.getAngle()), asteroidListAtAngle);
            vaporizationBuilderList.add(asteroidListAtAngle);
        }
    }

    private List<Asteroid> buildVaporizationList() {
        List<Asteroid> vaporizationList = new ArrayList<>();

        int numRotations = 0;
        final int totalAsteroidsToVaporize = asteroidList.size();
        int totalAsteroidsVaporized = 0;

        while (totalAsteroidsVaporized < totalAsteroidsToVaporize) {
            for(AsteroidListAtAngle asteroidListAtAngle : vaporizationBuilderList) {
                if (asteroidListAtAngle.getNumAsteroids() > numRotations) {
                    vaporizationList.add(asteroidListAtAngle.getAsteroidAtPos(numRotations));
                    totalAsteroidsVaporized++;
                }
            }
            numRotations++;
        }

        return vaporizationList;
    }

    public static class Builder {
        private Asteroid vaporizationLaser;
        private List<Asteroid> asteroidList;

        private Builder(Asteroid vaporizationLaser, List<Asteroid> asteroidList) {
            this.vaporizationLaser = vaporizationLaser;
            this.asteroidList = asteroidList;
        }

        public static Builder createAsteroidVaporizationListGenerator(Asteroid vaporizationLaser, List<Asteroid> asteroidList) {
            List<Asteroid> listWithoutLaser = new ArrayList<>();
            listWithoutLaser.addAll(asteroidList);
            listWithoutLaser.remove(vaporizationLaser);
            return new Builder(vaporizationLaser, listWithoutLaser);
        }

        public AsteroidVaporizationListGenerator build() {
            return new AsteroidVaporizationListGenerator(vaporizationLaser, asteroidList);
        }
    }
}
