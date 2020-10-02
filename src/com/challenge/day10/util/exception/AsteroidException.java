package com.challenge.day10.util.exception;

public class AsteroidException extends Exception {
    public AsteroidException(char asteroidChar, int xPos, int yPos) {
        super(String.format("Unknown character '%c' at position (%d, %d)", asteroidChar, xPos, yPos));
    }

    public AsteroidException(String message) {
        super(message);
    }
}
