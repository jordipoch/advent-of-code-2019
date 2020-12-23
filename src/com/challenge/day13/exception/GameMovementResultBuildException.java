package com.challenge.day13.exception;

public class GameMovementResultBuildException extends RuntimeException {
    public GameMovementResultBuildException(String message) {
        super("Invalid game movement result: " + message);
    }
}
