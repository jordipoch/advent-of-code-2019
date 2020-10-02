package com.challenge.day10;

import com.challenge.day10.exception.AsteroidListNotSortedException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AsteroidListAtAngle {
    private double angle;
    private List<Asteroid> asteroidList = new ArrayList<>();
    private boolean sorted = false;

    public AsteroidListAtAngle(double angle, Asteroid first) {
        this.angle = angle;
        asteroidList.add(first);
    }

    public void addAsteroid(Asteroid asteroid) {
        asteroidList.add(asteroid);
        sorted = false;
    }

    public void sortListByDistance() {
        asteroidList.sort(Comparator.comparingInt(Asteroid::getDistanceToLaser));
        sorted = true;
    }

    public double getAngle() {
        return angle;
    }

    public Asteroid getAsteroidAtPos(int pos) {
        if (!sorted) {
            throw new AsteroidListNotSortedException();
        }

        return asteroidList.get(pos);
    }

    public int getNumAsteroids() {
        return asteroidList.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsteroidListAtAngle that = (AsteroidListAtAngle) o;
        return Double.compare(that.angle, angle) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(angle);
    }
}
