package com.challenge.day15;

import com.challenge.day15.exception.DroidEngineException;
import com.challenge.day15.exception.DroidMoveException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
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
import static com.challenge.day15.DroidDirection.EAST;
import static com.challenge.day15.DroidDirection.NORTH;
import static com.challenge.day15.DroidDirection.SOUTH;
import static com.challenge.day15.DroidDirection.WEST;
import static com.challenge.day15.MovementResult.MOVED;
import static com.challenge.day15.MovementResult.OXYGEN_SYSTEM;
import static com.challenge.day15.MovementResult.WALL;
import static com.challenge.day15.exception.DroidMoveException.ErrorType.NOT_EMPTY_CELL;
import static com.challenge.day15.exception.DroidMoveException.ErrorType.UNEXPECTED_ENGINE_RESULT;
import static com.challenge.library.geometry.model.Int2DPoint.ORIGIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class RealDroidControllerTest {
    private static final Logger logger = LogManager.getLogger();

    @Mock
    private Droid droid;

    private Grid grid;

    private DroidController droidController;
    private AutoCloseable autoCloseable;
    private final DroidControllerFactory factory = DroidControllerFactory.getInstance();

    @BeforeMethod
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        grid = new Grid();
        droidController = factory.createDroidController(droid, grid);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test (dataProvider = "tryMoveSimpleCombinations")
    public void testTryMove_simpleMove(DroidDirection direction, MovementResult movementResult, Int2DPoint expectedNewPos) throws DroidEngineException {
        logger.info("Performing test...");

        when(droid.move(direction)).thenReturn(movementResult);

        var actualMovementResult = droidController.tryMoveDroid(direction);

        assertEquals(actualMovementResult.getMovementResult(), movementResult);
        assertEquals(droidController.getDroidPosition(), expectedNewPos);

        logger.info("Test OK");
    }

    @Test (dataProvider = "moveSimpleCombinations")
    public void testMove_simpleMove(DroidDirection direction, Int2DPoint expectedNewPos) throws DroidEngineException, DroidMoveException {
        logger.info("Performing test...");

        grid.putCell(CellType.EMPTY, expectedNewPos);
        when(droid.move(direction)).thenReturn(MOVED);

        droidController.moveDroid(direction);

        assertEquals(droidController.getDroidPosition(), expectedNewPos);

        logger.info("Test OK");
    }

    @Test (dataProvider = "moveErrorNonEmptyCell",
            expectedExceptions = DroidMoveException.class)
    public void testMove_errorMoveNonEmptyCell(DroidDirection direction, CellType cellType) throws DroidEngineException, DroidMoveException {
        logger.info("Performing test...");

        var expectedNewPos = direction.moveDirection(ORIGIN);
        if (cellType != UNKNOWN && cellType != EXPLORED) {
            grid.putCell(cellType, expectedNewPos);
        }
        try {
            droidController.moveDroid(direction);
        } catch (DroidMoveException e) {
            assertEquals(e.getErrorType(), NOT_EMPTY_CELL);
            logger.info("Got exception: {}", e.getMessage());
            logger.info("Test OK");
            throw e;
        }
    }

    @Test (dataProvider = "moveErrorUnexpectedMovementResult",
            expectedExceptions = DroidMoveException.class)
    public void testMove_errorUnexpectedMovementResult(DroidDirection direction, MovementResult movementResult) throws DroidEngineException, DroidMoveException {
        logger.info("Performing test...");

        var expectedNewPos = direction.moveDirection(ORIGIN);
        grid.putCell(CellType.EMPTY, expectedNewPos);

        when(droid.move(direction)).thenReturn(movementResult);

        try {
            droidController.moveDroid(direction);
        } catch (DroidMoveException e) {
            assertEquals(e.getErrorType(), UNEXPECTED_ENGINE_RESULT);
            logger.info("Got exception: {}", e.getMessage());
            logger.info("Test OK");
            throw e;
        }

        logger.info("Test OK");
    }

    @Test (dataProvider = "correctCompoundMovements")
    public void testCompoundMovements(DroidDirection[] directions, MovementResult[] movementResults, MovementType[] movementTypes, Int2DPoint finalPosition) throws DroidEngineException, DroidMoveException {
        logger.info("Performing test...");

        mockDroidMovements(movementResults);

        move(directions, movementResults, movementTypes);

        assertThat(droidController.getDroidPosition()).as("Checking final Droid position").isEqualTo(finalPosition);

        logger.info("Test OK");
    }

    @Test
    public void testGetNextMovementsFromCurrentPosition_fromInitialPosition() {
        logger.info("Performing test...");

        var nextMovements = droidController.getNextMovementsFromCurrentPosition();

        assertThat(nextMovements).as("Checking list size...").hasSize(4);
        assertThat(getNumCellsOfType(nextMovements, UNKNOWN)).as("Checking num of adjacent unknown blocks...").isEqualTo(4);

        logger.info("Test OK");
    }

    @Test
    public void testGetNextMovementsFromCurrentPosition_fromInitialPositionAfterExploringAdjacentCells() throws DroidEngineException, DroidMoveException {
        logger.info("Performing test...");

        var directions = new DroidDirection[] {NORTH, SOUTH, SOUTH, WEST, EAST, EAST, WEST};
        var movementResults = new MovementResult[] {MOVED, MOVED, WALL, MOVED, MOVED, OXYGEN_SYSTEM, MOVED};
        var movementTypes = new MovementType[] {MovementType.TRY_MOVE, MovementType.MOVE, MovementType.TRY_MOVE,
                                            MovementType.TRY_MOVE, MovementType.MOVE, MovementType.TRY_MOVE, MovementType.MOVE};

        mockDroidMovements(movementResults);

        move(directions, movementResults, movementTypes);

        var nextMovements = droidController.getNextMovementsFromCurrentPosition();

        assertThat(nextMovements).as("Checking list size...").hasSize(4);
        assertThat(droidController.getDroidPosition()).as("Checking droid position...").isEqualTo(ORIGIN);
        assertThat(getNumCellsOfType(nextMovements, UNKNOWN)).as("Checking num of adjacent unknown blocks...").isEqualTo(0);
        assertThat(getNumCellsOfType(nextMovements, EMPTY)).as("Checking num of adjacent empty blocks...").isEqualTo(2);
        assertThat(getNumCellsOfType(nextMovements, CellType.WALL)).as("Checking num of adjacent wall blocks...").isEqualTo(1);
        assertThat(getNumCellsOfType(nextMovements, OXYGEN)).as("Checking num of adjacent oxygen system blocks...").isEqualTo(1);

        logger.info("Test OK");
    }

    private long getNumCellsOfType(final List<NextMovement> nextMovements, final CellType cellType) {
        return nextMovements.stream().filter(nm -> nm.getCellType() == cellType).count();
    }

    private void move(DroidDirection[] directions, MovementResult[] movementResults, MovementType[] movementTypes) throws DroidEngineException, DroidMoveException {
        for (int i = 0; i < directions.length; i++) {
            if (movementTypes[i] == MovementType.TRY_MOVE) {
                var result = droidController.tryMoveDroid(directions[i]);
                assertThat(result.getMovementResult()).as("Checking try move result").isEqualTo(movementResults[i]);
            } else {
                droidController.moveDroid(directions[i]);
            }
        }
    }

    private void mockDroidMovements(MovementResult[] movementResults) throws DroidEngineException {
        var ongoingStubbing = when(droid.move(any())).thenReturn(movementResults[0]);
        for (int i = 1; i < movementResults.length; i++) {
            ongoingStubbing = ongoingStubbing.thenReturn(movementResults[i]);
        }
    }

    @DataProvider(name = "tryMoveSimpleCombinations")
    private Iterator<Object[]> createTryMoveSimpleTestCombinations() {
        return Arrays.asList(
                new Object[] {NORTH, MOVED, new Int2DPoint(0, 1)},
                new Object[] {NORTH, MovementResult.WALL, ORIGIN},
                new Object[] {NORTH, MovementResult.OXYGEN_SYSTEM, new Int2DPoint(0, 1)},
                new Object[] {SOUTH, MOVED, new Int2DPoint(0, -1)},
                new Object[] {SOUTH, MovementResult.WALL, ORIGIN},
                new Object[] {SOUTH, MovementResult.OXYGEN_SYSTEM, new Int2DPoint(0, -1)},
                new Object[] {EAST, MOVED, new Int2DPoint(1, 0)},
                new Object[] {EAST, MovementResult.WALL, ORIGIN},
                new Object[] {EAST, MovementResult.OXYGEN_SYSTEM, new Int2DPoint(1, 0)},
                new Object[] {DroidDirection.WEST, MOVED, new Int2DPoint(-1, 0)},
                new Object[] {DroidDirection.WEST, MovementResult.WALL, ORIGIN},
                new Object[] {DroidDirection.WEST, MovementResult.OXYGEN_SYSTEM, new Int2DPoint(-1, 0)}
        ).iterator();
    }

    @DataProvider(name = "moveSimpleCombinations")
    private Iterator<Object[]> createMoveSimpleTestCombinations() {
        return Arrays.asList(
                new Object[] {NORTH, new Int2DPoint(0, 1)},
                new Object[] {SOUTH, new Int2DPoint(0, -1)},
                new Object[] {EAST, new Int2DPoint(1, 0)},
                new Object[] {DroidDirection.WEST, new Int2DPoint(-1, 0)}
        ).iterator();
    }

    @DataProvider(name = "moveErrorNonEmptyCell")
    private Iterator<Object[]> createMoveTestErrorNonEmptyCell() {
        return Arrays.asList(
                new Object[] {NORTH, CellType.WALL},
                new Object[] {NORTH, OXYGEN},
                new Object[] {NORTH, UNKNOWN},
                new Object[] {NORTH, EXPLORED},
                new Object[] {SOUTH, CellType.WALL},
                new Object[] {SOUTH, OXYGEN},
                new Object[] {SOUTH, UNKNOWN},
                new Object[] {SOUTH, EXPLORED},
                new Object[] {EAST, CellType.WALL},
                new Object[] {EAST, OXYGEN},
                new Object[] {EAST, UNKNOWN},
                new Object[] {EAST, EXPLORED},
                new Object[] {WEST, CellType.WALL},
                new Object[] {WEST, OXYGEN},
                new Object[] {WEST, UNKNOWN},
                new Object[] {WEST, EXPLORED}
        ).iterator();
    }

    @DataProvider(name = "moveErrorUnexpectedMovementResult")
    private Iterator<Object[]> createMoveTestErrorUnexpectedMovementResult() {
        return Arrays.asList(
                new Object[] {NORTH, MovementResult.WALL},
                new Object[] {NORTH, MovementResult.OXYGEN_SYSTEM},
                new Object[] {SOUTH, MovementResult.WALL},
                new Object[] {SOUTH, MovementResult.OXYGEN_SYSTEM},
                new Object[] {EAST, MovementResult.WALL},
                new Object[] {EAST, MovementResult.OXYGEN_SYSTEM},
                new Object[] {DroidDirection.WEST, MovementResult.WALL},
                new Object[] {DroidDirection.WEST, MovementResult.OXYGEN_SYSTEM}
        ).iterator();
    }

    @DataProvider(name = "correctCompoundMovements")
    private Iterator<Object[]> createCorrectCompoundMovements() {
        return Arrays.asList(
                new Object[]{new DroidDirection[] {NORTH, SOUTH},
                            new MovementResult[] {MOVED, MOVED},
                            new MovementType[] {MovementType.TRY_MOVE, MovementType.MOVE},
                            ORIGIN},
                new Object[]{new DroidDirection[] {NORTH, EAST, SOUTH, WEST},
                            new MovementResult[] {MOVED, MOVED, MOVED, MOVED},
                            new MovementType[] {MovementType.TRY_MOVE, MovementType.TRY_MOVE, MovementType.TRY_MOVE, MovementType.MOVE},
                            ORIGIN},
                new Object[]{new DroidDirection[] {NORTH, NORTH},
                            new MovementResult[] {MOVED, MOVED},
                            new MovementType[] {MovementType.TRY_MOVE, MovementType.TRY_MOVE},
                            new Int2DPoint(0, 2)},
                new Object[]{new DroidDirection[] {NORTH, EAST, SOUTH},
                            new MovementResult[] {MOVED, WALL, MOVED},
                            new MovementType[] {MovementType.TRY_MOVE, MovementType.TRY_MOVE, MovementType.MOVE},
                            ORIGIN},
                new Object[]{new DroidDirection[] {NORTH, EAST, SOUTH, WEST, SOUTH},
                            new MovementResult[] {MOVED, WALL, MOVED, MOVED, OXYGEN_SYSTEM},
                            new MovementType[] {MovementType.TRY_MOVE, MovementType.TRY_MOVE, MovementType.TRY_MOVE, MovementType.TRY_MOVE, MovementType.TRY_MOVE},
                            new Int2DPoint(-1, -1)}
        ).iterator();
    }

    private enum MovementType {
        TRY_MOVE, MOVE;
    }
}