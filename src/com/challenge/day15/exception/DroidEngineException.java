package com.challenge.day15.exception;

public class DroidEngineException extends Exception {
    public DroidEngineException(String message) {
        super(message);
    }

    public DroidEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
