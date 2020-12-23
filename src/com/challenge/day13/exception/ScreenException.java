package com.challenge.day13.exception;

public class ScreenException extends Exception {
    public ScreenException(String message) {
        super(message);
    }

    public ScreenException(String message, Throwable t) {
        super(message, t);
    }
}
