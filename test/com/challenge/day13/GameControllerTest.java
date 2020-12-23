package com.challenge.day13;

import com.challenge.day13.exception.GameControllerException;
import com.challenge.day13.exception.ScreenException;
import com.challenge.day13.exception.UnexpectedFinishExecutionException;
import com.challenge.day13.exception.WrongTileException;
import com.challenge.library.geometry.model.Int2DPoint;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult;
import com.challenge.library.intcodecomputer.exception.IntComputerException;
import com.challenge.day13.GameMovementResult.MovementType;
import static com.challenge.day13.GameController.Builder.createGameController;
import static com.challenge.day13.GameMovementResult.Builder.createNewGameMovementResult;
import static com.challenge.day13.GameLogicExecutionOutput.createTileChangedOutput;
import static com.challenge.day13.GameLogicExecutionOutput.createScoreUpdatedOutput;
import static com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult.Builder.createExecutionResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTest {
    private static final Logger logger = LogManager.getLogger();

    @Mock private IntCodeComputer intCodeComputer;

    private AutoCloseable autoCloseable;

    @BeforeMethod
    public void openMocks() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    private Int2DPoint ballPosition;
    private Int2DPoint paddlePosition;

    @Test
    public void moveGameTestPutFirstBlock() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.SCENARIO_BUILD)
                .withNumBlocksLeft(1)
                .build();

        simulateGameLogicAndCheckResult(Collections.singletonList(
                createTileChangedOutput(Tile.of(0, 0, TileType.BLOCK))),
                expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestPutBall() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var newPos = new Int2DPoint(5, 10);

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.BALL_MOVED)
                .withNewPosition(newPos)
                .build();

        simulateGameLogicAndCheckResult(Collections.singletonList(
                createTileChangedOutput(Tile.of(newPos.getX(), newPos.getY(), TileType.BALL))),
                expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestSimpleScenarioBuild() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = addHorizontalRowOfTiles(3, 3, TileType.WALL, 10);
        gameLogicOutput.addAll(addHorizontalRowOfTiles(3, 4, TileType.BLOCK, 10));

        logger.debug("Game logic output: " + gameLogicOutput);

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.SCENARIO_BUILD)
                .withNumBlocksLeft(10)
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestCompleteScenarioBuild() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = addScenarioWallBoundaries(100, 30);
        gameLogicOutput.addAll(addSquareOfTiles(5, 5, TileType.BLOCK, 90, 10));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(50, 28, TileType.BALL)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(50, 29, TileType.HORIZ_PADDLE)));

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.PADDLE_MOVED)
                .withNewPosition(new Int2DPoint(50, 29))
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestBallCleanUp() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = createSimpleScenario(30, 10, 20, 2);

        gameLogicOutput.add(createTileChangedOutput(Tile.of(ballPosition, TileType.EMPTY)));

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.BALL_OR_PADDLE_CLEAN_UP)
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestBlockBroken() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = createSimpleScenario(30, 10, 20, 2);

        final var blockPos = new Int2DPoint(15, 5);

        gameLogicOutput.add(createTileChangedOutput(Tile.of(blockPos, TileType.BLOCK)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(ballPosition, TileType.EMPTY)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(15, 6, TileType.BALL)));

        gameLogicOutput.add(createTileChangedOutput(Tile.of(blockPos, TileType.EMPTY)));


        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.BLOCK_BROKEN)
                .withNumBlocksLeft(40)
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestBallMovedAndBlockBroken() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = createSimpleScenario(30, 10, 20, 2);

        final var blockPos = new Int2DPoint(15, 5);

        gameLogicOutput.add(createTileChangedOutput(Tile.of(blockPos, TileType.BLOCK)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(ballPosition, TileType.EMPTY)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(blockPos, TileType.BALL)));



        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.BALL_MOVED_AND_BLOCK_BROKEN)
                .withNumBlocksLeft(40)
                .withNewPosition(blockPos)
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestDuplicatedBall() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = createSimpleScenario(30, 10, 20, 2);

        gameLogicOutput.add(createTileChangedOutput(Tile.of(15, 5, TileType.BALL)));

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.BALL_MOVED)
                .withNewPosition(new Int2DPoint(15, 5))
                .withWarning("Ball duplicated!")
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestScoreUpdated() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = createSimpleScenario(30, 10, 20, 2);

        final var newScore = 1_000L;
        gameLogicOutput.add(createScoreUpdatedOutput(newScore));

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.SCORE_UPDATED)
                .withNewScore(newScore)
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestNoChange() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = createSimpleScenario(30, 10, 20, 2);

        gameLogicOutput.add(createTileChangedOutput(Tile.of(15, 8, TileType.EMPTY)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(15, 4, TileType.BALL)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(15, 4, TileType.BALL)));

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.NO_CHANGE)
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestUnknownChange() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = createSimpleScenario(30, 10, 20, 2);

        gameLogicOutput.add(createTileChangedOutput(Tile.of(10, 6, TileType.WALL)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(10, 6, TileType.EMPTY)));

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.UNKNOWN)
                .withWarning("Wall removed!")
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test
    public void moveGameTestMoveBallWithTwoWarnings() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        final var gameLogicOutput = createSimpleScenario(30, 10, 20, 2);

        gameLogicOutput.add(createTileChangedOutput(Tile.of(10, 6, TileType.WALL)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(10, 6, TileType.BALL)));

        GameMovementResult expectedResult = createNewGameMovementResult()
                .withMovementType(MovementType.BALL_MOVED)
                .withNewPosition(new Int2DPoint(10, 6))
                .withWarning("Wall removed!")
                .withWarning("Ball duplicated")
                .build();

        simulateGameLogicAndCheckResult(gameLogicOutput, expectedResult);

        logger.info("Test OK");
    }

    @Test (expectedExceptions = {GameControllerException.class})
    public void moveGameTestUnexpectedFinishExecutionException() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        var gameController = createGameController(intCodeComputer)
                .build();

        when(intCodeComputer.executeCode())
                .thenReturn(longToNextOutputExecutionResult(4))
                .thenReturn(longToNextOutputExecutionResult(5))
                .thenReturn(createExecutionResult().withResultType(ExecutionResult.ResultType.EXECUTION_FINISHED).build());

        try {
            gameController.moveGame(null);
        } catch (GameControllerException e) {
            Throwable cause = e.getCause();
            logger.debug(cause);
            assertThat(cause).as("Check exception is of type UnexpectedFinishExecutionException...")
                    .isInstanceOf(UnexpectedFinishExecutionException.class);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test (expectedExceptions = {GameControllerException.class})
    public void moveGameTestIntComputerException() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        var gameController = createGameController(intCodeComputer)
                .build();

        when(intCodeComputer.executeCode())
                .thenReturn(longToNextOutputExecutionResult(4))
                .thenReturn(longToNextOutputExecutionResult(5))
                .thenThrow(new IntComputerException("Error executing the Int Code Computer"));

        try {
            gameController.moveGame(null);
        } catch (GameControllerException e) {
            Throwable cause = e.getCause();
            logger.debug(cause);
            assertThat(cause).as("Check exception is of type IntComputerException...")
                    .isInstanceOf(IntComputerException.class);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test (expectedExceptions = {GameControllerException.class})
    public void moveGameTestScreenException() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        var gameController = createGameController(intCodeComputer)
                .withScreenSize(10, 10)
                .build();

        when(intCodeComputer.executeCode())
                .thenReturn(longToNextOutputExecutionResult(12))
                .thenReturn(longToNextOutputExecutionResult(5))
                .thenReturn(longToNextOutputExecutionResult(2));

        try {
            gameController.moveGame(null);
        } catch (GameControllerException e) {
            Throwable cause = e.getCause();
            logger.debug(cause);
            assertThat(cause).as("Check exception is of type ScreenException...")
                    .isInstanceOf(ScreenException.class);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test (expectedExceptions = {GameControllerException.class})
    public void moveGameTestWrongTileException() throws GameControllerException, IntComputerException {
        logger.info("Performing test...");

        var gameController = createGameController(intCodeComputer)
                .withScreenSize(10, 10)
                .build();

        when(intCodeComputer.executeCode())
                .thenReturn(longToNextOutputExecutionResult(2))
                .thenReturn(longToNextOutputExecutionResult(5))
                .thenReturn(longToNextOutputExecutionResult(8));

        try {
            gameController.moveGame(null);
        } catch (GameControllerException e) {
            Throwable cause = e.getCause();
            logger.debug(cause);
            assertThat(cause).as("Check exception is of type WrongTileException...")
                    .isInstanceOf(WrongTileException.class);
            logger.info("Test OK");
            throw e;
        }
    }


    private List<GameLogicExecutionOutput> createSimpleScenario(int width, int height, int numHorizBlocks, int numVertBlocks) {
        ballPosition = new Int2DPoint(width/2, height-2);
        paddlePosition = new Int2DPoint(width/2, height-1);

        final var gameLogicOutput = addScenarioWallBoundaries(width, height);
        gameLogicOutput.addAll(addSquareOfTiles((width-numHorizBlocks)/2, height/3, TileType.BLOCK, numHorizBlocks, numVertBlocks));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(ballPosition.getX(), ballPosition.getY(), TileType.BALL)));
        gameLogicOutput.add(createTileChangedOutput(Tile.of(paddlePosition.getX(), paddlePosition.getY(), TileType.HORIZ_PADDLE)));

        return gameLogicOutput;
    }

    private List<GameLogicExecutionOutput> addScenarioWallBoundaries(int width, int height) {
        final var gameLogicOutput = addHorizontalRowOfTiles(0, 0, TileType.WALL, width);
        gameLogicOutput.addAll(addVerticalRowOfTiles(0, 1, TileType.WALL, height - 1));
        gameLogicOutput.addAll(addVerticalRowOfTiles(width - 1, 1, TileType.WALL, height - 1));

        return gameLogicOutput;
    }

    private List<GameLogicExecutionOutput> addSquareOfTiles(int x, int y, TileType tileType, int width, int height) {
        final var gameLogicOutput = new ArrayList<GameLogicExecutionOutput>();
        for (int i = 0; i < height; i++) {
            gameLogicOutput.addAll(addHorizontalRowOfTiles(x, y + i, tileType, width));
        }

        return gameLogicOutput;
    }

    private List<GameLogicExecutionOutput> addHorizontalRowOfTiles(int x, int y, TileType tileType, int numTiles) {
        return Stream.iterate(Tile.of(x, y, tileType),
                tile -> Tile.of(tile.getPosition().getX()+1, tile.getPosition().getY(), tile.getTileType()))
                .limit(numTiles)
                .map(GameLogicExecutionOutput::createTileChangedOutput)
                .collect(Collectors.toList());
    }

    private List<GameLogicExecutionOutput> addVerticalRowOfTiles(int x, int y, TileType tileType, int numTiles) {
        return Stream.iterate(Tile.of(x, y, tileType),
                tile -> Tile.of(tile.getPosition().getX(), tile.getPosition().getY()+1, tile.getTileType()))
                .limit(numTiles)
                .map(GameLogicExecutionOutput::createTileChangedOutput)
                .collect(Collectors.toList());
    }

    private void simulateGameLogicAndCheckResult(List<GameLogicExecutionOutput> outputList, GameMovementResult expectedResult) throws GameControllerException, IntComputerException {
        var gameController = createGameController(intCodeComputer)
                .withScreenSize(getScenarioSize(outputList, tile -> tile.getPosition().getX()),
                        getScenarioSize(outputList, tile -> tile.getPosition().getY()))
                .build();

        GameMovementResult gameMovementResult = null;
        for (GameLogicExecutionOutput output : outputList) {
            if (gameMovementResult != null) {
                logger.debug("Intermediate result: " + gameMovementResult);
            }
            stubGameLogicExecutionOutput(output);
            gameMovementResult = gameController.moveGame(null);
        }

        logger.info("Final result: " + gameMovementResult);

        logger.debug("Screen output: {}{}", System.lineSeparator(), gameController.getScreenOutput());

        assertThat(gameMovementResult).as("Checking game movement result...").isEqualTo(expectedResult);
    }

    private int getScenarioSize(List<GameLogicExecutionOutput> outputList, ToIntFunction<? super Tile> function) {
        return outputList.stream()
                .filter(output -> output.getOutputType() == GameLogicExecutionOutput.ExecutionOutputType.TILE_CHANGED)
                .map(GameLogicExecutionOutput::getTile)
                .mapToInt(function)
                .max().orElse(0) + 1;
    }

    private void stubGameLogicExecutionOutput(GameLogicExecutionOutput gameLogicExecutionOutput) throws IntComputerException {
        switch (gameLogicExecutionOutput.getOutputType()) {
            case TILE_CHANGED -> stubTileOutput(gameLogicExecutionOutput.getTile());
            case SCORE_UPDATED -> stubScoreOutput(gameLogicExecutionOutput.getCurrentScore());
        }
    }
    private void stubTileOutput(Tile tile) throws IntComputerException {
        when(intCodeComputer.executeCode())
            .thenReturn(longToNextOutputExecutionResult(tile.getPosition().getX()))
            .thenReturn(longToNextOutputExecutionResult(tile.getPosition().getY()))
            .thenReturn(longToNextOutputExecutionResult(tile.getTileType().getId()));
    }

    private void stubScoreOutput(long score) throws IntComputerException {
        when(intCodeComputer.executeCode())
            .thenReturn(longToNextOutputExecutionResult(-1L))
            .thenReturn(longToNextOutputExecutionResult(0L))
            .thenReturn(longToNextOutputExecutionResult(score));
    }

    private ExecutionResult longToNextOutputExecutionResult(long l) {
        return createExecutionResult()
                .withResultType(ExecutionResult.ResultType.NEXT_OUTPUT)
                .addOutputValue(BigInteger.valueOf(l))
                .build();
    }
}