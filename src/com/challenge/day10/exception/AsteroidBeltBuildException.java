package com.challenge.day10.exception;

public class AsteroidBeltBuildException extends Exception {
    public AsteroidBeltBuildException(String message) {
        super(message);
    }

    public AsteroidBeltBuildException(String message, Throwable t) {
        super(message, t);
    }
}
