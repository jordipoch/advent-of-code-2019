package com.challenge.day15.mocks;

import com.challenge.day15.AbstractDroidController;
import com.challenge.day15.DroidController;
import com.challenge.day15.DroidDirection;
import com.challenge.day15.Grid;
import com.challenge.day15.MovementResult;
import com.challenge.day15.exception.DroidEngineException;
import com.challenge.day15.mocks.exception.TestGridMapCreationException;

import java.nio.file.Paths;

import static com.challenge.day15.mocks.TestGridMap.createTestGridMap;

public class DroidControllerMock extends AbstractDroidController {
    private TestGridMap gridMap;

    private DroidControllerMock(Grid grid, TestGridMap gridMap) {
        super(grid);
        this.gridMap = gridMap;
    }

    @Override
    protected MovementResult doMoveDroid(DroidDirection direction) {
        var intendedPosition = direction.moveDirection(position);

        var cellType = gridMap.getCell(intendedPosition);
        return cellType.getRelatedMovementResult();
    }

    public static DroidController createDroidControllerMock(String testGridFileName) throws TestGridMapCreationException {
        return new DroidControllerMock(new Grid(), createTestGridMap(testGridFileName));
    }
}
