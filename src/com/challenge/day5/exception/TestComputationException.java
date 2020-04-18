package com.challenge.day5.exception;

public class TestComputationException extends Exception {
    public TestComputationException(String message, Throwable innerException) {
        super(message, innerException);
    }
}
