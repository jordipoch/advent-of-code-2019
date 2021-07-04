package com.challenge.day15;

import com.challenge.day15.exception.CellAlreadyExistsException;
import com.challenge.day15.exception.InvalidCellTypeException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.challenge.day15.CellType.EMPTY;
import static com.challenge.day15.CellType.EXPLORED;
import static com.challenge.day15.CellType.UNKNOWN;
import static com.challenge.day15.CellType.WALL;
import static org.testng.Assert.*;

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

}