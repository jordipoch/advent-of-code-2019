package com.challenge.day15.exception;

public class MaxMovementsExceededException extends Exception {
    public MaxMovementsExceededException(int maxMovements) {
        super(String.format("Maximum number of allowed movements reached (%d). Exploration halted", maxMovements));
    }
}
