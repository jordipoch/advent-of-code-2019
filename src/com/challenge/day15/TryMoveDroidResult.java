package com.challenge.day15;

public class TryMoveDroidResult {
    private final MovementResult movementResult;
    private final boolean isPositionNew;

    private TryMoveDroidResult(MovementResult movementResult, boolean isPositionNew) {
        this.movementResult = movementResult;
        this.isPositionNew = isPositionNew;
    }

    public static TryMoveDroidResult ofNewPosition(MovementResult result) {
        return new TryMoveDroidResult(result, true);
    }

    public static TryMoveDroidResult ofKnownPosition(MovementResult result) {
        return new TryMoveDroidResult(result, false);
    }

    public MovementResult getMovementResult() {
        return movementResult;
    }

    public boolean isPositionNew() {
        return isPositionNew;
    }
}
