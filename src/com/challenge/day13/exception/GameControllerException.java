package com.challenge.day13.exception;

public class GameControllerException extends Exception {
    public GameControllerException(String message, Throwable t) {
        super(message, t);
    }
}
