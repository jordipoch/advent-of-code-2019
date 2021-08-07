package com.challenge.day15;

import com.challenge.day15.exception.CellAlreadyExistsException;
import com.challenge.day15.exception.InvalidCellTypeException;
import com.challenge.library.geometry.model.Int2DPoint;

import static com.challenge.day15.CellType.EMPTY;
import static com.challenge.day15.CellType.EXPLORED;
import static org.apache.commons.lang3.math.NumberUtils.min;
import static org.apache.commons.lang3.math.NumberUtils.max;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.challenge.day15.CellType.UNKNOWN;

public class Grid {
    private Map<Int2DPoint, GridCell> gridCellMap = new HashMap<>();

    public Grid() {
        putCell(EMPTY, Int2DPoint.ORIGIN);
    }

    private Grid(Map<Int2DPoint, GridCell> gridCellMap) {
        this.gridCellMap = gridCellMap;
    }

    public void putCell(CellType cellType, Int2DPoint position) {
        checkCellToPut(cellType, position);

        final var newGridCell = GridCell.of(cellType, position);
        gridCellMap.put(newGridCell.getPosition(), newGridCell);
    }

    public GridCell getCell(Int2DPoint position) {
        var gridCell = gridCellMap.get(position);
        if (gridCell == null) {
            gridCell = GridCell.of(UNKNOWN, position);
        }

        return gridCell;
    }

    public int getNumCells() {
        return gridCellMap.size();
    }

    public Collection<GridCell> getGridCells() {
        return gridCellMap.values();
    }

    @Override
    public String toString() {
        return gridToString();
    }

    public Grid createExploredGrid() {
        return new Grid(gridCellMap
                        .entrySet()
                        .stream()
                        .collect(Collectors.<Map.Entry<Int2DPoint, GridCell>, Int2DPoint, GridCell>toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().getCellType() == EXPLORED ? GridCell.of(EMPTY, e.getKey()) : e.getValue())));
    }

    private void checkCellToPut(CellType cellType, Int2DPoint position) {
        final var currentCell = gridCellMap.get(position);
        if (currentCell == null) {
            if (cellType == UNKNOWN || cellType == EXPLORED) {
                throw new InvalidCellTypeException(position, cellType);
            }
        } else if (!(currentCell.getCellType() == EMPTY && cellType == EXPLORED)) {
            throw new CellAlreadyExistsException(currentCell, cellType);
        }
    }

    private String gridToString() {
        var cellTypeArray = createCellTypeArray();
        return cellTypeArrayToString(cellTypeArray);
    }

    private CellType[][] createCellTypeArray() {
        var minX = 0;
        var maxX = 0;
        var minY = 0;
        var maxY = 0;

        for (var cellPosition : gridCellMap.keySet()) {
            minX = min(minX, cellPosition.getX());
            maxX = max(maxX, cellPosition.getX());

            minY = min(minY, cellPosition.getY());
            maxY = max(maxY, cellPosition.getY());
        }

        var arrayXSize = maxX - minX + 1;
        var arrayYSize = maxY - minY + 1;

        var cellTypeArray = new CellType[arrayXSize][arrayYSize];
        for (var i = 0; i < arrayYSize; i++)
            for (var j = 0; j < arrayXSize; j++) {
                var gridPosition = new Int2DPoint(j + minX, maxY - i);
                CellType cellType;
                if (gridPosition.equals(Int2DPoint.ORIGIN)) {
                    cellType = CellType.INITIAL;
                } else {
                    cellType = getCell(gridPosition).getCellType();
                }
                cellTypeArray[j][i] = cellType;
            }

        return cellTypeArray;
    }

    private String cellTypeArrayToString(CellType[][] cellTypeArray) {
        var arrayXSize = cellTypeArray.length;
        var arrayYSize = cellTypeArray[0].length;

        var sb = new StringBuilder();
        for (var i = 0; i < arrayYSize; i++) {
            for (var j = 0; j < arrayXSize; j++) {
                sb.append(cellTypeArray[j][i].getCharCode());
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
