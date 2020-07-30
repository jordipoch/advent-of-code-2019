package com.challenge.day7.exception;

public class NoMoreSequencesException extends RuntimeException {
    public NoMoreSequencesException() {
        super("No more sequences left!");
    }
}
