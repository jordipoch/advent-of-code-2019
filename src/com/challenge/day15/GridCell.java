package com.challenge.day15;

import com.challenge.library.geometry.model.Int2DPoint;

public class GridCell {
    private final CellType cellType;
    private final Int2DPoint position;

    private GridCell(CellType cellType, Int2DPoint position) {
        this.cellType = cellType;
        this.position = position;
    }

    public static GridCell of(CellType cellType, Int2DPoint position) {
        return new GridCell(cellType, position);
    }

    public CellType getCellType() {
        return cellType;
    }

    public Int2DPoint getPosition() {
        return position;
    }

}
