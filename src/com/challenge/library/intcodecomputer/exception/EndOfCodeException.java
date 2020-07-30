package com.challenge.library.intcodecomputer.exception;

public class EndOfCodeException extends Exception {
    public EndOfCodeException(long pos, int arrayLength) {
        super(String.format("Error attempting to read instruction beyond in a position (%d) beyond the end of code position (%d)", pos, arrayLength-1));
    }
}
