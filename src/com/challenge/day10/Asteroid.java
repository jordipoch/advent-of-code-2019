package com.challenge.day10;

import com.challenge.library.geometry.model.Int2DCoord;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.challenge.library.geometry.utils.IntegerGeometryUtils.getAbsoluteDistance;
import static com.challenge.library.utils.NumberUtils.mcd;


public class Asteroid {
    private final Int2DCoord position;
    private int numAsteroidsVisible;

    public Asteroid(int xPos, int yPos) {
        this(xPos, yPos, 0);
    }

    public Asteroid(int xPos, int yPos, int numAsteroidsVisible) {
        this.position = new Int2DCoord(xPos, yPos);
        this.numAsteroidsVisible = numAsteroidsVisible;
    }

    public Int2DCoord getPosition() {
        return position;
    }

    public int getNumAsteroidsVisible() {
        return numAsteroidsVisible;
    }

    public void setNumAsteroidsVisible(int numAsteroidsVisible) {
        this.numAsteroidsVisible = numAsteroidsVisible;
    }

    public int getXPos() {
        return position.getX();
    }

    public int getYPos() {
        return position.getY();
    }

    public List<Int2DCoord> getIntermediatePositionsTo(Asteroid otherAsteroid) {

        if (position.equals(otherAsteroid.getPosition())) {
            return new ArrayList<>();
        }

        return calculateIntermediatePositionPoints(position, otherAsteroid.getPosition(),
                getIncrementFromP1toP2(position, otherAsteroid.getPosition()));
    }

    private Int2DCoord getIncrementFromP1toP2(Int2DCoord p1, Int2DCoord p2) {
        Int2DCoord absoluteDistance = getAbsoluteDistance(p1, p2);

        int xIncrement = 0, yIncrement = 0;
        if (absoluteDistance.getX() == 0 || absoluteDistance.getY() == 0) {
            if (absoluteDistance.getX() == 0) {
                yIncrement = 1;
            } else {
                xIncrement = 1;
            }
        } else {
            int mcd = mcd(absoluteDistance.getX(), absoluteDistance.getY());
            xIncrement = absoluteDistance.getX() / mcd;
            yIncrement = absoluteDistance.getY() / mcd;
        }

        if (p1.getX() > p2.getX()) {
            xIncrement = -xIncrement;
        }
        if (p1.getY() > p2.getY()) {
            yIncrement = -yIncrement;
        }

        return new Int2DCoord(xIncrement, yIncrement);
    }

    private List<Int2DCoord> calculateIntermediatePositionPoints(Int2DCoord p1, Int2DCoord p2, Int2DCoord increment) {
        List<Int2DCoord> intermediatePositionsList = new ArrayList<>();

        Int2DCoord position = p1;
        while (!position.equals(p2)) {
            position = position.add(increment);
            if (!position.equals(p2)) {
                intermediatePositionsList.add(position);
            }
        }

        return intermediatePositionsList;
    }

    @Override
    public String toString() {
        return "Asteroid{" +
                "position=" + position +
                ", numAsteroidsVisible=" + numAsteroidsVisible +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asteroid asteroid = (Asteroid) o;
        return numAsteroidsVisible == asteroid.numAsteroidsVisible &&
                position.equals(asteroid.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, numAsteroidsVisible);
    }
}
