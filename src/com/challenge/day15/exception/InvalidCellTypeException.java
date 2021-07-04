package com.challenge.day15.exception;

import com.challenge.day15.CellType;
import com.challenge.library.geometry.model.Int2DPoint;

public class InvalidCellTypeException extends RuntimeException {

    private final Int2DPoint position;
    private final CellType cellType;
    public InvalidCellTypeException(Int2DPoint position, CellType cellType) {
        this.position = position;
        this.cellType = cellType;
    }

    @Override
    public String getMessage() {
        return String.format("Invalid cell type %s for empty position %s", cellType, position);
    }
}
