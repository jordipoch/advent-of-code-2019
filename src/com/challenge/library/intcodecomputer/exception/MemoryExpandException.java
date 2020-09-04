package com.challenge.library.intcodecomputer.exception;

public class MemoryExpandException extends Exception{
    public MemoryExpandException(int newSize, int maxSize) {
        super(String.format("Can't extend memory to size %d. Max permitted size: %d", newSize, maxSize));
    }
}
