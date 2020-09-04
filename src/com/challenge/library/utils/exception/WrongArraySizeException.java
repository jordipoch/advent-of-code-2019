package com.challenge.library.utils.exception;

public class WrongArraySizeException extends Exception {
    public WrongArraySizeException(int imageSize, int layerSize) {
        super(String.format("The current image size (%d) and layer size (%d) causes the last layer to be only filled with %d pixels", imageSize, layerSize, imageSize % layerSize));
    }
}
