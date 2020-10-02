package com.challenge.day10;

import com.challenge.library.geometry.model.Int2DPoint;
import com.challenge.library.geometry.model.Int2DVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.challenge.library.geometry.utils.IntegerGeometryUtils.calculateIntermediatePositionPoints;

public class Asteroid {
    private final Int2DPoint position;
    private int numAsteroidsVisible;

    private double angle;
    private int distanceToLaser;

    public Asteroid(int xPos, int yPos) {
        this(xPos, yPos, 0);
    }

    public Asteroid(int xPos, int yPos, int numAsteroidsVisible) {
        this.position = new Int2DPoint(xPos, yPos);
        this.numAsteroidsVisible = numAsteroidsVisible;
    }

    public Int2DPoint getPosition() {
        return position;
    }

    public int getNumAsteroidsVisible() {
        return numAsteroidsVisible;
    }

    public void setNumAsteroidsVisible(int numAsteroidsVisible) {
        this.numAsteroidsVisible = numAsteroidsVisible;
    }

    public double getAngle() {
        return angle;
    }

    public int getDistanceToLaser() {
        return distanceToLaser;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setDistanceToLaser(int distanceToLaser) {
        this.distanceToLaser = distanceToLaser;
    }

    public List<Int2DPoint> getIntermediatePositionsTo(Asteroid otherAsteroid) {

        if (position.equals(otherAsteroid.getPosition())) {
            return new ArrayList<>();
        }

        return calculateIntermediatePositionPoints(position, otherAsteroid.getPosition(),
                new Int2DVector(position, otherAsteroid.getPosition()).normalize());
    }

    @Override
    public String toString() {
        return position.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asteroid asteroid = (Asteroid) o;
        return position.equals(asteroid.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
