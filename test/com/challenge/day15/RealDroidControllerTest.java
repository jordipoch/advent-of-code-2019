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

import static com.challenge.day15.DroidDirection.EAST;
import static com.challenge.day15.DroidDirection.NORTH;
import static com.challenge.day15.DroidDirection.SOUTH;
import static com.challenge.day15.DroidDirection.WEST;
import static com.challenge.day15.MovementResult.MOVED;
import static com.challenge.day15.MovementResult.OXYGEN_SYSTEM;
import static com.challenge.day15.MovementResult.WALL;
import static com.challenge.day15.RealDroidControllerTest.MovementType.MOVE;
import static com.challenge.day15.RealDroidControllerTest.MovementType.TRY_MOVE;
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

    @Test (dataProvider = "directions",
            expectedExceptions = DroidMoveException.class)
    public void testMove_errorMoveNonEmptyCell(DroidDirection direction) throws DroidEngineException, DroidMoveException {
        logger.info("Performing test...");

        var expectedNewPos = direction.moveDirection(ORIGIN);
        grid.putCell(CellType.WALL, expectedNewPos);

        when(droid.move(direction)).thenReturn(WALL);

        try {
            droidController.moveDroid(direction);
        } catch (DroidMoveException e) {
            assertEquals(e.getErrorType(), NOT_EMPTY_CELL);
            logger.info("Got exception: {}", e.getMessage());
            logger.info("Test OK");
            throw e;
        }
    }

    @Test (dataProvider = "directions",
            expectedExceptions = DroidMoveException.class)
    public void testMove_errorUnexpectedMovementResult(DroidDirection direction) throws DroidEngineException, DroidMoveException {
        logger.info("Performing test...");

        var expectedNewPos = direction.moveDirection(ORIGIN);
        grid.putCell(CellType.EMPTY, expectedNewPos);

        when(droid.move(direction)).thenReturn(WALL);

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

    @Test (dataProvider = "testGetDirectionsToUnknownPositionsData")
    public void testGetDirectionsToUnknownPosition(DroidDirection[] directions, MovementResult[] movementResults, MovementType[] movementTypes, DroidDirection[] expectedDirections) throws DroidEngineException, DroidMoveException {
        logger.info("Performing test...");

        mockDroidMovements(movementResults);
        move(directions, movementResults, movementTypes);

        var actualDirections = droidController.getDirectionsToUnknownPositions();

        assertThat(actualDirections).as("Checking directions list...").isEqualTo(Arrays.asList(expectedDirections));

        logger.info("Test OK");
    }

    @Test (dataProvider = "testGetDirectionsToEmptyPositionsData")
    public void testGetDirectionsToEmptyPosition(DroidDirection[] directions, MovementResult[] movementResults, MovementType[] movementTypes, DroidDirection[] expectedDirections) throws DroidEngineException, DroidMoveException {
        logger.info("Performing test...");

        mockDroidMovements(movementResults);
        move(directions, movementResults, movementTypes);

        var actualDirections = droidController.getDirectionsToPositionsToMoveTo();

        assertThat(actualDirections).as("Checking directions list...").isEqualTo(Arrays.asList(expectedDirections));

        logger.info("Test OK");
    }

    private void move(DroidDirection[] directions, MovementResult[] movementResults, MovementType[] movementTypes) throws DroidEngineException, DroidMoveException {
        for (int i = 0; i < directions.length; i++) {
            if (movementTypes[i] == TRY_MOVE) {
                var result = droidController.tryMoveDroid(directions[i]);
                assertThat(result.getMovementResult()).as("Checking try move result").isEqualTo(movementResults[i]);
            } else {
                droidController.moveDroid(directions[i]);
            }
        }
    }

    private void mockDroidMovements(MovementResult[] movementResults) throws DroidEngineException {
        if(movementResults.length == 0) return;

        var ongoingStubbing = when(droid.move(any())).thenReturn(movementResults[0]);
        for (int i = 1; i < movementResults.length; i++) {
            ongoingStubbing = ongoingStubbing.thenReturn(movementResults[i]);
        }
    }

    @DataProvider(name = "tryMoveSimpleCombinations")
    private Iterator<Object[]> createTryMoveSimpleTestCombinations() {
        return Arrays.asList(
                new Object[] {NORTH, MOVED, new Int2DPoint(0, 1)},
                new Object[] {NORTH, WALL, ORIGIN},
                new Object[] {NORTH, OXYGEN_SYSTEM, new Int2DPoint(0, 1)},
                new Object[] {SOUTH, MOVED, new Int2DPoint(0, -1)},
                new Object[] {SOUTH, WALL, ORIGIN},
                new Object[] {SOUTH, OXYGEN_SYSTEM, new Int2DPoint(0, -1)},
                new Object[] {EAST, MOVED, new Int2DPoint(1, 0)},
                new Object[] {EAST, WALL, ORIGIN},
                new Object[] {EAST, OXYGEN_SYSTEM, new Int2DPoint(1, 0)},
                new Object[] {WEST, MOVED, new Int2DPoint(-1, 0)},
                new Object[] {WEST, WALL, ORIGIN},
                new Object[] {WEST, OXYGEN_SYSTEM, new Int2DPoint(-1, 0)}
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

    @DataProvider(name = "directions")
    private Iterator<Object> getDirections() {
        return Arrays.<Object>asList(NORTH, SOUTH, EAST, WEST)
                .iterator();
    }

    @DataProvider(name = "correctCompoundMovements")
    private Iterator<Object[]> createCorrectCompoundMovements() {
        return Arrays.asList(
                new Object[]{new DroidDirection[] {NORTH, SOUTH},
                            new MovementResult[] {MOVED, MOVED},
                            new MovementType[] {TRY_MOVE, MOVE},
                            ORIGIN},
                new Object[]{new DroidDirection[] {NORTH, EAST, SOUTH, WEST},
                            new MovementResult[] {MOVED, MOVED, MOVED, MOVED},
                            new MovementType[] {TRY_MOVE, TRY_MOVE, TRY_MOVE, MOVE},
                            ORIGIN},
                new Object[]{new DroidDirection[] {NORTH, NORTH},
                            new MovementResult[] {MOVED, MOVED},
                            new MovementType[] {TRY_MOVE, TRY_MOVE},
                            new Int2DPoint(0, 2)},
                new Object[]{new DroidDirection[] {NORTH, EAST, SOUTH},
                            new MovementResult[] {MOVED, WALL, MOVED},
                            new MovementType[] {TRY_MOVE, TRY_MOVE, MOVE},
                            ORIGIN},
                new Object[]{new DroidDirection[] {NORTH, EAST, SOUTH, WEST, SOUTH},
                            new MovementResult[] {MOVED, WALL, MOVED, MOVED, OXYGEN_SYSTEM},
                            new MovementType[] {TRY_MOVE, TRY_MOVE, TRY_MOVE, TRY_MOVE, TRY_MOVE},
                            new Int2DPoint(-1, -1)}
        ).iterator();
    }

    @DataProvider(name = "testGetDirectionsToUnknownPositionsData")
    private Iterator<Object[]> createTestGetDirectionsToUnknownPositionsData() {
        return Arrays.asList(
                new Object[] {
                        new DroidDirection[] {},
                        new MovementResult[] {},
                        new MovementType[] {},
                        new DroidDirection[] {NORTH, SOUTH, WEST, EAST}},
                new Object[] {
                        new DroidDirection[] {NORTH},
                        new MovementResult[] {MOVED},
                        new MovementType[] {TRY_MOVE},
                        new DroidDirection[] {NORTH, WEST, EAST}},
                new Object[] {
                        new DroidDirection[] {NORTH, EAST, SOUTH},
                        new MovementResult[] {MOVED, MOVED, MOVED},
                        new MovementType[] {TRY_MOVE, TRY_MOVE, TRY_MOVE},
                        new DroidDirection[] {SOUTH, EAST}}
        ).iterator();
    }

    @DataProvider(name = "testGetDirectionsToEmptyPositionsData")
    private Iterator<Object[]> createTestGetDirectionsToEmptyPositionsData() {
        return Arrays.asList(
                new Object[] {
                        new DroidDirection[] {},
                        new MovementResult[] {},
                        new MovementType[] {},
                        new DroidDirection[] {}},
                new Object[] {
                        new DroidDirection[] {NORTH},
                        new MovementResult[] {MOVED},
                        new MovementType[] {TRY_MOVE},
                        new DroidDirection[] {SOUTH}},
                new Object[] {
                        new DroidDirection[] {NORTH, EAST, SOUTH},
                        new MovementResult[] {MOVED, MOVED, MOVED},
                        new MovementType[] {TRY_MOVE, TRY_MOVE, TRY_MOVE},
                        new DroidDirection[] {NORTH, WEST}},
                new Object[] {
                        new DroidDirection[] {NORTH, SOUTH, SOUTH, WEST, EAST, EAST, WEST},
                        new MovementResult[] {MOVED, MOVED, WALL, MOVED, MOVED, OXYGEN_SYSTEM, MOVED},
                        new MovementType[] {TRY_MOVE, MOVE, TRY_MOVE, TRY_MOVE, MOVE, TRY_MOVE, MOVE},
                        new DroidDirection[] {NORTH, WEST, EAST}}
        ).iterator();
    }

    public enum MovementType {
        TRY_MOVE, MOVE
    }
}