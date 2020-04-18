package com.challenge.day5.exception;

public class InvalidParameterModesException extends Exception {
    public InvalidParameterModesException(String message) {
        super(message);
    }

    public InvalidParameterModesException(long instruction, String message) {
        super(String.format("Invalid parameter modes found for instruction %d: %s", instruction, message));
    }
}
