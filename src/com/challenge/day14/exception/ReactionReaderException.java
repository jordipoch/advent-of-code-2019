package com.challenge.day14.exception;

public class ReactionReaderException extends Exception {
    public ReactionReaderException(String message) {
        super(message);
    }

    public ReactionReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
