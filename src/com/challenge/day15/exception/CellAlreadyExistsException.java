package com.challenge.day15.exception;

import com.challenge.day15.CellType;
import com.challenge.day15.GridCell;

public class CellAlreadyExistsException extends RuntimeException {
    private final GridCell currentGridCell;
    private final CellType newCellType;

    public CellAlreadyExistsException(GridCell currentGridCell, CellType newCellType) {
        this.currentGridCell = currentGridCell;
        this.newCellType = newCellType;
    }

    @Override
    public String getMessage() {
        return String.format("Cell with type %s already exists at position %s. Cannot be replaced by cell type %s", currentGridCell.getCellType(), currentGridCell.getPosition(), newCellType);
    }
}
