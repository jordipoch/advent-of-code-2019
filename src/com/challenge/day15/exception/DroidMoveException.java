package com.challenge.day15.exception;

import com.challenge.day15.CellType;
import com.challenge.day15.DroidDirection;
import com.challenge.day15.MovementResult;
import com.challenge.library.geometry.model.Int2DPoint;

import java.util.Arrays;

public class DroidMoveException extends Exception {
    private static final String BASE_MESSAGE = "Error while trying to move through a known path. ";
    private final ErrorType errorType;

    public DroidMoveException(ErrorType errorType, Object... params) {
        super(BASE_MESSAGE + errorType.getMessage(params));
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public enum ErrorType {
        NOT_EMPTY_CELL {
            @Override
            public String getMessage(Object... params) {
                var cellType = getParamN(1, CellType.class, params);
                var position = getParamN(2, Int2DPoint.class, params);
                return String.format("The cell position %s was expected to be empty, but instead is of this type: %s", position, cellType);
            }
        },
        UNEXPECTED_ENGINE_RESULT {
            @Override
            public String getMessage(Object... params) {
                var movementResult = getParamN(1, MovementResult.class, params);
                var direction = getParamN(2, DroidDirection.class, params);
                var position = getParamN(3, Int2DPoint.class, params);
                return String.format("Unexpected result from Droid while moving from %s direction %s: expecting %s, but got %s.", position, direction, MovementResult.MOVED, movementResult);
            }

        };

        public abstract String getMessage(Object... params);

        protected <T> T getParamN(int n, Class<T> classType, Object... params) {
            var i = n - 1;
            if (params.length >= n && classType.isInstance(params[i])) {
                return (T) params[i];
            }
            throw new IllegalArgumentException(String.format("Invalid argument #%d creating exception %s for ErrorType = %s (params = %s).", n, DroidMoveException.class.getName(), this, Arrays.toString(params)));
        }
    }
}
