package com.challenge.day15;

public class NextMovement {
    private DroidDirection direction;
    private CellType cellType;

    private NextMovement(DroidDirection direction, CellType cellType) {
        this.direction = direction;
        this.cellType = cellType;
    }

    public static NextMovement of(DroidDirection direction, CellType cellType) {
        return new NextMovement(direction, cellType);
    }

    public DroidDirection getDirection() {
        return direction;
    }

    public CellType getCellType() {
        return cellType;
    }
}
