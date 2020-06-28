package com.challenge.day6.exception;

import java.nio.file.Path;

public class LocalOrbitsReadingException extends Exception {
    public LocalOrbitsReadingException(Path filePath, Throwable cause) {
        super("Can't read file " + filePath.getFileName().toString(), cause);
    }

    public LocalOrbitsReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalOrbitsReadingException(Long nDuplicates) {
        super(String.format("An object BBB can only orbit around another object AAA. %d cases found.", nDuplicates));
    }
}
