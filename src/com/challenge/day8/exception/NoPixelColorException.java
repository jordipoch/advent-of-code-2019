package com.challenge.day8.exception;

public class NoPixelColorException extends Exception {
    public NoPixelColorException(int x, int y) {
        super(String.format("No pixel color found for pixel in pos (%d, %d)", x, y));
    }
}
