package com.challenge.library.intcodecomputer.exception;

import java.math.BigInteger;

public class InvalidPositionException extends Exception {
    public InvalidPositionException(String message, long pos, int maxPos) {
        super(String.format("%s (position %d out of %d)", message, pos, maxPos-1));
    }

    public InvalidPositionException(String message, long pos, int maxPos, Throwable t) {
        super(String.format("%s (position %d out of %d)", message, pos, maxPos-1), t);
    }

    public InvalidPositionException(String message, BigInteger pos, int maxPos) {
        super(String.format("%s (position %s out of %d)", message, pos, maxPos-1));
    }

    public InvalidPositionException(long pos, String message) {
        super(String.format("Invalid memory position (%d): %s", pos, message));
    }

    public InvalidPositionException(String message) {
        super(String.format("Invalid memory position: %s", message));
    }
}
