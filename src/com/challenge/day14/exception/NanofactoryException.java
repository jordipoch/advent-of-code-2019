package com.challenge.day14.exception;

public class NanofactoryException extends Exception {
    public NanofactoryException(String message) {
        super(message);
    }

    public NanofactoryException(Throwable t) {
        super(t);
    }
}
