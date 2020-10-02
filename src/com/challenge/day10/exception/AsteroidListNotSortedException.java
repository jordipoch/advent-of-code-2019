package com.challenge.day10.exception;

public class AsteroidListNotSortedException extends RuntimeException{
    public AsteroidListNotSortedException() {
        super("The asteroid list has not been sorted yet");
    }
}
