package com.challenge.day15.exception;

import com.challenge.day15.GridCell;

public class GridException extends RuntimeException {
    protected final transient GridCell currentGridCell;

    public GridException(GridCell currentGridCell) {
        this.currentGridCell = currentGridCell;
    }
}
