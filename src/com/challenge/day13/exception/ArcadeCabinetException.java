package com.challenge.day13.exception;

public class ArcadeCabinetException extends Exception {
    public ArcadeCabinetException(String message) {
        super (message);
    }

    public ArcadeCabinetException(String message, Throwable cause) {
        super (message, cause);
    }
}
