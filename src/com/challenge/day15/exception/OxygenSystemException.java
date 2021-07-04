package com.challenge.day15.exception;

public class OxygenSystemException extends Exception {
    public OxygenSystemException(String message) {
        super(message);
    }

    public OxygenSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
