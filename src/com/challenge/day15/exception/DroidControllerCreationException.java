package com.challenge.day15.exception;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DroidControllerCreationException extends Exception {
    public DroidControllerCreationException(Path basePath, String file, Throwable cause) {
        super(String.format("Cannot read droid engine code from file %s", basePath.resolve(Paths.get(file))), cause);
    }
}
