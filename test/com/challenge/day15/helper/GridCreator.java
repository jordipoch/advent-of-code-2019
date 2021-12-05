package com.challenge.day15.helper;

import com.challenge.day15.CellType;
import com.challenge.day15.Grid;
import com.challenge.day15.exception.GridCreationException;
import com.challenge.day15.mocks.TestGridMap;
import com.challenge.day15.mocks.exception.TestGridMapCreationException;
import com.challenge.library.geometry.model.Int2DPoint;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.challenge.day15.mocks.TestGridMap.createTestGridMap;

public class GridCreator {
    public static final Path BASE_PATH = Paths.get("resources", "com", "challenge", "day15");

    public static Grid createExploredGridFromFile(String fileName) throws GridCreationException {
        try {
            var gridArray = createTestGridMap(fileName);
            return createGridFromGridArray(gridArray);
        } catch (TestGridMapCreationException e) {
            throw new GridCreationException(e);
        }
    }

    private static Grid createGridFromGridArray(TestGridMap gridArray) {
        var grid = new Grid();
        for (int i = 0; i < gridArray.getYSize(); i++) {
            for (int j = 0; j < gridArray.getXSize(); j++) {
                var position = gridArray.getPositionFromInternalArrayCoordinates(j, i);
                var cellType = gridArray.getCell(position);
                if (!position.equals(Int2DPoint.ORIGIN) && !CellType.UNKNOWN.equals(cellType)) {
                    grid.putCell(cellType, position);
                }
            }
        }

        return grid;
    }
}
