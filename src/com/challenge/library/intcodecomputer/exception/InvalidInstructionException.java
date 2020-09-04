package com.challenge.library.intcodecomputer.exception;

import java.math.BigInteger;

public class InvalidInstructionException extends Exception {
    public InvalidInstructionException(long instruction, int pos, Throwable cause) {
        super(String.format("Invalid instruction \"%d\" found at position %d", instruction, pos), cause);
    }

    public InvalidInstructionException(long instruction, int pos, String message) {
        super(String.format("Invalid instruction \"%d\" found at position %d: %s", instruction, pos, message));
    }
}
