package com.challenge.library.intcodecomputer.exception;

public class NoMoreInputValuesException extends Exception {
    public NoMoreInputValuesException() {
        super("No more input values left for the input instruction");
    }
}
