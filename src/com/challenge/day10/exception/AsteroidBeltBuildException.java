package com.challenge.day10.exception;

public class AsteroidBeltBuildException extends Exception {
    public AsteroidBeltBuildException(char asteroidChar, int xPos, int yPos) {
        super(String.format("Unknown character '%c' at position (%d, %d)", asteroidChar, xPos, yPos));
    }

    public AsteroidBeltBuildException(String message) {
        super(message);
    }
}
