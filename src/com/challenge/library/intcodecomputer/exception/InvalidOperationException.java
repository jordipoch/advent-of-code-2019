package com.challenge.library.intcodecomputer.exception;

public class InvalidOperationException extends Exception {


    public InvalidOperationException(long operation) {
        super(String.format("Invalid operation found (%d)", operation));
    }
}
