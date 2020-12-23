package com.challenge.day13;

import static com.challenge.day13.GameLogicExecutionOutput.*;
import static com.challenge.day13.GameMovementResult.Builder.createNewGameMovementResult;
import static com.challenge.day13.Screen.Builder.createScreen;

import com.challenge.day13.GameLogicExecutionOutput.ExecutionOutputType;
import com.challenge.day13.GameMovementResult.MovementType;

import com.challenge.day13.exception.*;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult;
import com.challenge.library.intcodecomputer.exception.IntComputerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class GameController {
    private static final Logger logger = LogManager.getLogger();

    private final IntCodeComputer intCodeComputer;
    private final Screen screen;

    private GameController(IntCodeComputer intCodeComputer, Screen screen) {
        this.intCodeComputer = intCodeComputer;
        this.screen = screen;
    }

    public GameMovementResult moveGame(JoystickInput joystickInput) throws GameControllerException {
        logger.traceEntry("Joystick input: {}", joystickInput);
        try {
            if (joystickInput != null) {
                this.intCodeComputer.addInputValue(joystickInput.getId());
            }
            GameLogicExecutionOutput output = runNextExecutionLogic();
            logger.debug("Execution output: {}", output);

            TileChangeDetail tileChangeDetail = null;
            if (output.getOutputType() == ExecutionOutputType.TILE_CHANGED) {
                tileChangeDetail = screen.putTile(output.getTile());
                logger.debug("Tile change detail: {}", tileChangeDetail);
            }

            return logger.traceExit(buildGameMovementResult(output, tileChangeDetail));
        } catch (IntComputerException | UnexpectedFinishExecutionException | WrongTileException | ScreenException e ) {
            throw new GameControllerException("Error moving the game", e);
        }
    }

    public String getScreenOutput() {
        return screen.toString();
    }

    private GameLogicExecutionOutput runNextExecutionLogic() throws IntComputerException, UnexpectedFinishExecutionException, WrongTileException {
        ExecutionResult executionResult = getNextExecutionResult();
        if (executionResult.isExecutionFinished()) {
            return createGameOverOutput();
        }

        if (executionResult.isInputNeeded()) {
            return createInputNeededOutput();
        }

        int xPos = (int) getOutputFromExecutionResultOrThrowException(executionResult);


        int yPos = (int) getOutputFromExecutionResultOrThrowException(getNextExecutionResult());

        if (xPos == -1 && yPos == 0) {
            long score = getOutputFromExecutionResultOrThrowException(getNextExecutionResult());
            return createScoreUpdatedOutput(score);
        }

        int tileId = (int) getOutputFromExecutionResultOrThrowException(getNextExecutionResult());
        return createTileChangedOutput(Tile.of(xPos, yPos, tileId));
    }

    private ExecutionResult getNextExecutionResult() throws IntComputerException {
        return intCodeComputer.executeCode();
    }

    private long getOutputFromExecutionResultOrThrowException(ExecutionResult executionResult) throws UnexpectedFinishExecutionException {
        if (executionResult.hasAnyOutput()) {
            List<BigInteger> output = executionResult.getOutput();
            if (output.size() > 1) {
                logger.warn("Got {} output values while only expecting 1: {}", output.size(), output);
            }
            return output.get(0).longValue();
        } else {
            throw new UnexpectedFinishExecutionException("Expecting more output from Int Code Computer.");
        }

    }

    private GameMovementResult buildGameMovementResult(GameLogicExecutionOutput executionOutput, TileChangeDetail tileChangeDetail) {
        final GameMovementResult.Builder gameMovementResultBuilder = createNewGameMovementResult();

        switch (executionOutput.getOutputType()) {
            case TILE_CHANGED -> addTileChangeInformation(gameMovementResultBuilder, executionOutput, tileChangeDetail);
            case SCORE_UPDATED -> gameMovementResultBuilder.withMovementType(MovementType.SCORE_UPDATED)
                    .withNewScore(executionOutput.getCurrentScore());
            case GAME_OVER -> gameMovementResultBuilder.withMovementType(MovementType.GAME_OVER);
            case INPUT_NEEDED -> gameMovementResultBuilder.withMovementType(MovementType.INPUT_NEEDED);
        }

        return gameMovementResultBuilder.build();
    }

    private void addTileChangeInformation(GameMovementResult.Builder gameMovementResultBuilder, GameLogicExecutionOutput executionOutput, TileChangeDetail tileChangeDetail) {
        switch (tileChangeDetail.getChangeType()) {
            case TILE_ADDED -> addTileAdditionInformation(gameMovementResultBuilder, executionOutput, tileChangeDetail);
            case TILE_REMOVED -> addTileRemovalInformation(gameMovementResultBuilder, tileChangeDetail);
            case TILE_REPLACED -> addTileReplaceInformation(gameMovementResultBuilder, executionOutput, tileChangeDetail);
            case NO_CHANGE -> gameMovementResultBuilder.withMovementType(MovementType.NO_CHANGE);
        }
    }

    private void addTileAdditionInformation(GameMovementResult.Builder gameMovementResultBuilder, GameLogicExecutionOutput executionOutput, TileChangeDetail tileChangeDetail) {
        switch (tileChangeDetail.getTileAdded()) {
            case BLOCK, WALL -> gameMovementResultBuilder.withMovementType(MovementType.SCENARIO_BUILD)
                    .withNumBlocksLeft((int) screen.countNumTilesOfType(TileType.BLOCK));
            case BALL -> gameMovementResultBuilder.withMovementType(MovementType.BALL_MOVED)
                    .withNewPosition(executionOutput.getTile().getPosition())
                    .withWarning(() -> checkMultipleInstancesOfTileType(tileChangeDetail.getTileAdded()));
            case HORIZ_PADDLE -> gameMovementResultBuilder.withMovementType(MovementType.PADDLE_MOVED)
                    .withNewPosition(executionOutput.getTile().getPosition())
                    .withWarning(() -> checkMultipleInstancesOfTileType(tileChangeDetail.getTileAdded()));
            default -> gameMovementResultBuilder.withMovementType(MovementType.UNKNOWN)
                    .withWarning(String.format("Unexpected tile type added: %s", executionOutput.getTile().getTileType()));
        }
    }

    private void addTileRemovalInformation(GameMovementResult.Builder gameMovementResultBuilder, TileChangeDetail tileChangeDetail) {
        switch (tileChangeDetail.getTileRemoved()) {
            case BALL, HORIZ_PADDLE -> gameMovementResultBuilder.withMovementType(MovementType.BALL_OR_PADDLE_CLEAN_UP);
            case BLOCK -> gameMovementResultBuilder.withMovementType(MovementType.BLOCK_BROKEN)
                    .withNumBlocksLeft((int) screen.countNumTilesOfType(TileType.BLOCK));
            default -> gameMovementResultBuilder.withMovementType(MovementType.UNKNOWN)
                    .withWarning(String.format("Unexpected tile type removed: %s", tileChangeDetail.getTileRemoved()));
        }
    }

    private void addTileReplaceInformation(GameMovementResult.Builder gameMovementResultBuilder, GameLogicExecutionOutput executionOutput, TileChangeDetail tileChangeDetail) {
        switch (tileChangeDetail.getTileAdded()) {
            case BALL -> addBallReplaceInformation(gameMovementResultBuilder, executionOutput, tileChangeDetail);
            case HORIZ_PADDLE -> gameMovementResultBuilder.withMovementType(MovementType.PADDLE_MOVED)
                    .withNewPosition(executionOutput.getTile().getPosition())
                    .withWarning(String.format("It was not expected the paddle to replace another tile. Tile replaced: %s", tileChangeDetail.getTileRemoved()))
                    .withWarning(() -> checkMultipleInstancesOfTileType(tileChangeDetail.getTileAdded()));
            case BLOCK, WALL -> gameMovementResultBuilder.withMovementType(MovementType.SCENARIO_BUILD)
                    .withNumBlocksLeft((int) screen.countNumTilesOfType(TileType.BLOCK))
                    .withWarning(String.format("Unexpected replacement: %s", tileChangeDetail));
            default -> gameMovementResultBuilder.withMovementType(MovementType.UNKNOWN)
                    .withWarning(String.format("Unexpected replacement: %s", tileChangeDetail));
        }
    }

    private void addBallReplaceInformation(GameMovementResult.Builder gameMovementResultBuilder, GameLogicExecutionOutput executionOutput, TileChangeDetail tileChangeDetail) {
        if (tileChangeDetail.getTileRemoved() == TileType.BLOCK) {
            gameMovementResultBuilder.withMovementType(MovementType.BALL_MOVED_AND_BLOCK_BROKEN)
                    .withNewPosition(executionOutput.getTile().getPosition())
                    .withNumBlocksLeft((int) screen.countNumTilesOfType(TileType.BLOCK));
        } else {
            gameMovementResultBuilder.withMovementType(MovementType.BALL_MOVED)
                    .withNewPosition(executionOutput.getTile().getPosition())
                    .withWarning(String.format("Ball moved and replaced unexpected tile: %s", tileChangeDetail.getTileRemoved()));
        }
        gameMovementResultBuilder.withWarning(() -> checkMultipleInstancesOfTileType(tileChangeDetail.getTileAdded()));
    }

    private Optional<String> checkMultipleInstancesOfTileType(TileType tileType) {
        long num = screen.countNumTilesOfType(tileType);
        return num > 1 ? Optional.of(String.format("%d tiles of type %s has been detected on the screen", num, tileType)) : Optional.empty();
    }

    public static class Builder {
        private IntCodeComputer intCodeComputer;
        private final Screen.Builder screenBuilder = createScreen();

        private Builder() {}

        private Builder(IntCodeComputer intCodeComputer) {
            this.intCodeComputer = intCodeComputer;
        }

        public static Builder createGameController() {
            return new Builder();
        }

        public static Builder createGameController(IntCodeComputer intCodeComputer) {
            return new Builder(intCodeComputer);
        }

        public Builder withIntCodeComputer(IntCodeComputer intCodeComputer) {
            this.intCodeComputer = intCodeComputer;
            return this;
        }

        public Builder withScreenSize(int screenWidth, int screenHeight) {
            screenBuilder.withSize(screenWidth, screenHeight);
            return this;
        }

        public GameController build() {
            return new GameController(intCodeComputer, screenBuilder.build());
        }


    }
}
