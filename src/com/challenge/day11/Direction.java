package com.challenge.day11;

import com.challenge.library.geometry.model.Int2DVector;

public enum Direction {
    // Directions in clockwise order
    UP(Int2DVector.UP), RIGHT(Int2DVector.RIGHT), DOWN(Int2DVector.DOWN), LEFT(Int2DVector.LEFT);

    private Int2DVector directionVector;

    Direction(Int2DVector directionVector) {
        this.directionVector = directionVector;
    }

    public Int2DVector getDirectionVector() {
        return directionVector;
    }

    public Direction turn(Turn turn) {
        return(values()[(ordinal() + values().length + (turn == Turn.RIGHT ? 1 : -1)) % values().length]);
    }
}
