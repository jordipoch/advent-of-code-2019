package com.challenge.day15;

import com.challenge.day15.exception.CellAlreadyExistsException;
import com.challenge.day15.exception.InvalidCellTypeException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.challenge.day15.CellType.EMPTY;
import static com.challenge.day15.CellType.EXPLORED;
import static com.challenge.day15.CellType.OXYGEN;
import static com.challenge.day15.CellType.UNKNOWN;
import static com.challenge.day15.CellType.WALL;
import static org.testng.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GridTest {
    private static final Logger logger = LogManager.getLogger();
    private Grid grid;

    @BeforeMethod
    public void setUp() {
        grid = new Grid();
    }

    @Test
    public void testGetNonExistingCell() {
        final Int2DPoint position = new Int2DPoint(1, 0);

        final GridCell cell = grid.getCell(position);

        assertEquals(cell.getCellType(), UNKNOWN);
    }

    @Test
    public void testPutNonExistingCell() {
        final Int2DPoint position = new Int2DPoint(1, 0);
        grid.putCell(WALL, position);

        final GridCell cell = grid.getCell(position);

        assertEquals(cell.getCellType(), WALL);
        assertEquals(cell.getPosition(), position);
    }

    @Test
    public void testOverrideEmptyCellWithExplored() {
        final Int2DPoint position = new Int2DPoint(1, 0);
        grid.putCell(EMPTY, position);

        grid.putCell(EXPLORED, position);

        final GridCell cell = grid.getCell(position);

        assertEquals(cell.getCellType(), EXPLORED);
        assertEquals(cell.getPosition(), position);
    }

    @Test (expectedExceptions = CellAlreadyExistsException.class,
    expectedExceptionsMessageRegExp = ".*already exists.*")
    public void testPutExistingCell() {
        final Int2DPoint position = new Int2DPoint(1, 0);
        grid.putCell(WALL, position);

        try {
            grid.putCell(EMPTY, position);
        } catch (CellAlreadyExistsException e) {
            logger.info("Got expected error message: {}", e.getMessage());
            throw e;
        }
    }

    @Test (expectedExceptions = InvalidCellTypeException.class)
    public void testPutInvalidCellType() {
        final Int2DPoint position = new Int2DPoint(1, 0);

        try {
            grid.putCell(EXPLORED, position);
        } catch (InvalidCellTypeException e) {
            logger.info("Got expected error message: {}", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCreateResultGrid() {
        logger.info("Performing test...");

        var originalGrid = new Grid();
        originalGrid.putCell(WALL, new Int2DPoint(1, 0));
        originalGrid.putCell(EMPTY, new Int2DPoint(0, 1));
        originalGrid.putCell(EMPTY, new Int2DPoint(0, -1));
        originalGrid.putCell(EXPLORED, new Int2DPoint(0, -1));
        originalGrid.putCell(OXYGEN, new Int2DPoint(-1, 0));

        var resultGrid = originalGrid.createExploredGrid();

        logger.debug("Original grid: {}{}", System.lineSeparator(), originalGrid);
        logger.debug("Newly created grid: {}{}", System.lineSeparator(), resultGrid);

        assertNotNull(resultGrid);
        assertEquals(resultGrid.getNumCells(), originalGrid.getNumCells());

        for (var gridCell : originalGrid.getGridCells()) {
            if (gridCell.getCellType() == EXPLORED) {
                assertEquals(resultGrid.getCell(gridCell.getPosition()).getCellType(), EMPTY);
            } else {
                assertEquals(resultGrid.getCell(gridCell.getPosition()), gridCell);
            }

        }

        logger.info("Test OK");
    }

    @Test
    public void testGetOxygenPositionNoOxygen() {
        logger.info("Performing test...");

        var oxygenPosition = grid.getOxygenPosition();

        assertThat(oxygenPosition).as("Check oxygen not found...").isEmpty();

        logger.info("Test OK");
    }

    @Test
    public void testGetOxygenPosition() {
        logger.info("Performing test...");

        var expectedOxygenPosition = new Int2DPoint(1, 1);

        grid.putCell(EMPTY, new Int2DPoint(1, 0));
        grid.putCell(WALL, new Int2DPoint(2, 0));
        grid.putCell(OXYGEN, new Int2DPoint(1, 1));
        grid.putCell(EMPTY, new Int2DPoint(-1, 0));

        var oxygenPosition = grid.getOxygenPosition();

        assertThat(oxygenPosition).as("Check oxygen")
                .isNotEmpty()
                .contains(expectedOxygenPosition);

        logger.info("Test OK");
    }

    @Test (dataProvider = "adjacentPositionsScenarios")
    public void testGetAdjacentEmptyPositions(Int2DPoint from, List<GridCell> gridCells, List<Int2DPoint> expectedResult) {
        logger.info("Performing test...");

        for (var gridCell : gridCells) {
            grid.putCell(gridCell.getCellType(), gridCell.getPosition());
        }

        final var result = grid.getAdjacentEmptyPositions(from);
        System.out.println(result);

        assertThat(result).as("Check list of adjacent empty positions").isEqualTo(expectedResult);

        logger.info("Test OK");
    }

    @DataProvider(name = "adjacentPositionsScenarios")
    private Iterator<Object[]> getAdjacentPositionsScenarios() {
        return Arrays.asList(new Object[][] {
                {Int2DPoint.ORIGIN,
                        List.of(),
                        List.of()
                },
                {new Int2DPoint(1, 0),
                    List.of(GridCell.of(OXYGEN, new Int2DPoint(1, 0))),
                    List.of(Int2DPoint.ORIGIN)
                },
                {new Int2DPoint(1, 0),
                        List.of(GridCell.of(OXYGEN, new Int2DPoint(1, 0)), GridCell.of(EMPTY, new Int2DPoint(2, 0)),
                                GridCell.of(WALL, new Int2DPoint(1, 1)), GridCell.of(EMPTY, new Int2DPoint(1, -1))),
                        List.of(new Int2DPoint(1, -1), Int2DPoint.ORIGIN, new Int2DPoint(2, 0))
                },
                {new Int2DPoint(1, 0),
                        List.of(GridCell.of(OXYGEN, new Int2DPoint(1, 0)), GridCell.of(EMPTY, new Int2DPoint(2, 0)),
                                GridCell.of(WALL, new Int2DPoint(1, 1)), GridCell.of(WALL, new Int2DPoint(1, -1))),
                        List.of(Int2DPoint.ORIGIN, new Int2DPoint(2, 0))
                }
        }).iterator();
    }
}