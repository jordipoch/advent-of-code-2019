package com.challenge.library.intcodecomputer.exception;

public class IntComputerException extends Exception {
    public IntComputerException(String message) {
        super(message);
    }
    public IntComputerException(String message, Throwable t) {
        super(message, t);
    }
}
