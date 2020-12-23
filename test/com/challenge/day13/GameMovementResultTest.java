package com.challenge.day13;

import com.challenge.day13.GameMovementResult.MovementType;
import static com.challenge.day13.GameMovementResult.Builder.createNewGameMovementResult;

import com.challenge.day13.exception.GameMovementResultBuildException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GameMovementResultTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void testCreateCorrectScenarioBuildResult() {
        logger.info("Performing test...");

        final int numBlocksLeft = 34;
        final MovementType movementType = MovementType.SCENARIO_BUILD;

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .withNumBlocksLeft(numBlocksLeft)
                .build();

        checkAndShowResult(gameMovementResult, movementType, null, numBlocksLeft, -1, 0);
    }

    @Test
    public void testCreateCorrectBlockBrokenResult() {
        logger.info("Performing test...");

        final int numBlocksLeft = 34;
        final MovementType movementType = MovementType.BLOCK_BROKEN;

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .withNumBlocksLeft(numBlocksLeft)
                .build();

        checkAndShowResult(gameMovementResult, movementType, null, numBlocksLeft, -1, 0);
    }

    @Test
    public void testCreateCorrectBallMovedResult() {
        logger.info("Performing test...");

        final Int2DPoint newPosition = new Int2DPoint(6, 10);
        final MovementType movementType = MovementType.BALL_MOVED;

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .withNewPosition(newPosition)
                .build();

        checkAndShowResult(gameMovementResult, movementType, newPosition, -1, -1, 0);
    }

    @Test
    public void testCreateCorrectPaddleMovedResult() {
        logger.info("Performing test...");

        final Int2DPoint newPosition = new Int2DPoint(6, 10);
        final MovementType movementType = MovementType.PADDLE_MOVED;

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .withNewPosition(newPosition)
                .build();

        checkAndShowResult(gameMovementResult, movementType, newPosition, -1, -1, 0);
    }

    @Test
    public void testCreateCorrectBallMovedAndBlockBrokenResult() {
        logger.info("Performing test...");

        final Int2DPoint newPosition = new Int2DPoint(6, 10);
        final int numBlocsLeft = 10;
        final MovementType movementType = MovementType.BALL_MOVED_AND_BLOCK_BROKEN;

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .withNewPosition(newPosition)
                .withNumBlocksLeft(numBlocsLeft)
                .build();

        checkAndShowResult(gameMovementResult, movementType, newPosition, numBlocsLeft, -1, 0);
    }

    @Test
    public void testCreateCorrectBallOrPaddleCleanUpResult() {
        logger.info("Performing test...");

        final MovementType movementType = MovementType.BALL_OR_PADDLE_CLEAN_UP;

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .build();

        checkAndShowResult(gameMovementResult, movementType, null, -1, -1, 0);
    }

    @Test
    public void testCreateCorrectScoreUpdatedResult() {
        logger.info("Performing test...");

        final MovementType movementType = MovementType.SCORE_UPDATED;
        final long newScore = 1225;

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .withNewScore(newScore)
                .build();

        checkAndShowResult(gameMovementResult, movementType, null, -1, newScore, 0);
    }

    @Test
    public void testCreateCorrectNoChangeResult() {
        logger.info("Performing test...");

        final MovementType movementType = MovementType.NO_CHANGE;

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .build();

        checkAndShowResult(gameMovementResult, movementType, null, -1, -1, 0);
    }

    @Test
    public void testCreateCorrectUnknownResult() {
        logger.info("Performing test...");

        final MovementType movementType = MovementType.UNKNOWN;

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .withWarning("Warning message")
                .build();

        checkAndShowResult(gameMovementResult, movementType, null, -1, -1, 1);
    }

    @Test
    public void testSupplierProvidedWarning() {
        logger.info("Performing test...");

        final MovementType movementType = MovementType.BALL_MOVED;
        final Int2DPoint newPosition = new Int2DPoint(6, 10);

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .withNewPosition(newPosition)
                .withWarning(() -> Optional.of(String.format("2 tiles of type %s has been detected on the screen", movementType)))
                .build();

        checkAndShowResult(gameMovementResult, movementType, newPosition, -1, -1, 1);
    }

    @Test
    public void testSupplierProvidedEmptyWarning() {
        logger.info("Performing test...");

        final MovementType movementType = MovementType.BALL_MOVED;
        final Int2DPoint newPosition = new Int2DPoint(6, 10);

        GameMovementResult gameMovementResult = createNewGameMovementResult()
                .withMovementType(movementType)
                .withNewPosition(newPosition)
                .withWarning(Optional::empty)
                .build();

        checkAndShowResult(gameMovementResult, movementType, newPosition, -1, -1, 0);
    }

    @Test(expectedExceptions = {GameMovementResultBuildException.class})
    public void testCheckNoMovementType() {
        logger.info("Performing test...");

        try {
            createNewGameMovementResult()
                    .withNewPosition(new Int2DPoint(0, 0));
        } catch (GameMovementResultBuildException e) {
            logger.info("Exception: " + e);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test(expectedExceptions = {GameMovementResultBuildException.class})
    public void testCheckNewPositionMissing() {
        logger.info("Performing test...");

        try {
            createNewGameMovementResult()
                    .withMovementType(MovementType.BALL_MOVED)
                    .build();
        } catch (GameMovementResultBuildException e) {
            logger.info("Exception: " + e);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test(expectedExceptions = {GameMovementResultBuildException.class})
    public void testCheckNumBlocksLeftMissing() {
        logger.info("Performing test...");

        try {
            createNewGameMovementResult()
                    .withMovementType(MovementType.BLOCK_BROKEN)
                    .build();
        } catch (GameMovementResultBuildException e) {
            logger.info("Exception: " + e);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test(expectedExceptions = {GameMovementResultBuildException.class})
    public void testCheckNewScoreMissing() {
        logger.info("Performing test...");

        try {
            createNewGameMovementResult()
                    .withMovementType(MovementType.SCORE_UPDATED)
                    .build();
        } catch (GameMovementResultBuildException e) {
            logger.info("Exception: " + e);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test(expectedExceptions = {GameMovementResultBuildException.class})
    public void testCheckWarningsMissing() {
        logger.info("Performing test...");

        try {
            createNewGameMovementResult()
                    .withMovementType(MovementType.UNKNOWN)
                    .build();
        } catch (GameMovementResultBuildException e) {
            logger.info("Exception: " + e);
            logger.info("Test OK");
            throw e;
        }
    }

    private void checkAndShowResult(GameMovementResult result, MovementType expectedMovementType, Int2DPoint expectedPosition,
                                    int expectedNumBlocks, long expectedScore, int expectedNumWarnings) {

        logger.info("Result: " + result);

        assertThat(result.getMovementType()).as("Checking movement type...").isEqualTo(expectedMovementType);
        assertThat(result.getNewPosition()).as("Checking new position...").isEqualTo(expectedPosition);
        assertThat(result.getNumBlocksLeft()).as("Checking num blocks left...").isEqualTo(expectedNumBlocks);
        assertThat(result.getScore()).as("Checking new score...").isEqualTo(expectedScore);
        assertThat(result.getWarnings().size()).as("Checking number of warnings...").isEqualTo(expectedNumWarnings);

        logger.info("Test OK");
    }
}