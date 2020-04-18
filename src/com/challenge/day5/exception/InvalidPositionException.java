package com.challenge.day5.exception;

public class InvalidPositionException extends Exception {
    public InvalidPositionException(String message, long pos, int arrayLength) {
        super(String.format("%s (potition %d out of %d)", message, pos, arrayLength-1));
    }
}
