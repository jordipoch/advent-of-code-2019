package com.challenge.day5.exception;

import com.challenge.day5.Instruction;

public class ExecutionException extends Exception {
    public ExecutionException(Instruction instruction, String message) {
        super(String.format("%s | instruction = %s", message, instruction));
    }

    public ExecutionException(Instruction instruction, String message, Throwable cause) {
        super(String.format("%s | instruction = %s", message, instruction), cause);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutionException(String message) {
        super(message);
    }
}
