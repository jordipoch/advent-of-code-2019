package com.challenge.day5.exception;

public class InvalidInstructionException extends Exception {
    public InvalidInstructionException(long instruction, long pos, Throwable cause) {
        super(String.format("Invalid instruction \"%d\" found at position %d", instruction, pos), cause);
    }

    public InvalidInstructionException(long instruction, long pos, String message) {
        super(String.format("Invalid instruction \"%d\" found at position %d: %s", instruction, pos, message));
    }
}
