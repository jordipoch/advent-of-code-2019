package com.challenge.day15.mocks.exception;

import com.challenge.library.geometry.model.Int2DPoint;

public class TestGridMapOutOfBoundsException extends RuntimeException {
    Int2DPoint position;
    int arrayXPos;
    int arrayYPos;
    int arrayXSize;
    int arrayYSize;

    public TestGridMapOutOfBoundsException(Int2DPoint position, int arrayXPos, int arrayYPos, int arrayXSize, int arrayYSize) {
        this.position = position;
        this.arrayXPos = arrayXPos;
        this.arrayYPos = arrayYPos;
        this.arrayXSize = arrayXSize;
        this.arrayYSize = arrayYSize;
    }

    @Override
    public String getMessage() {
        return String.format("Error trying to get position %s. Invalid array position [%d][%d] (Max X = %d, Max Y = %d)"
        , position, arrayXPos, arrayYPos, arrayXSize-1, arrayYSize-1);
    }
}
