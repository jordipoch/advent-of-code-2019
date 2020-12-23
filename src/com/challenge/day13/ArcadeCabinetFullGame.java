package com.challenge.day13;

import com.challenge.day13.exception.ArcadeCabinetException;
import com.challenge.day13.exception.GameControllerException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArcadeCabinetFullGame implements ArcadeCabinet {
    private static final Logger logger = LogManager.getLogger();

    private final GameController gameController;
    private final ConfigurationInfo configurationInfo;

    private int moveNum;
    private int numBlocks;

    private Int2DPoint ballPosition;
    private Int2DPoint paddlePosition;
    private long score;

    protected ArcadeCabinetFullGame(GameController gameController, ConfigurationInfo configurationInfo) {
        this.gameController= gameController;
        this.configurationInfo = configurationInfo;
    }

    public long runGame() throws ArcadeCabinetException {
        logger.traceEntry();

        try {
            JoystickInput nextInput = null;
            boolean gameOver = false;
            while (!gameOver) {
                logger.debug("***** MOVE NUM {}, input: {} *****", moveNum, nextInput);

                GameMovementResult movementResult = gameController.moveGame(nextInput);
                updateGameStatus(movementResult);
                nextInput = calculateNextInput(movementResult);

                if (isPrintScreen(movementResult)) {
                    logger.debug("Screen output: {}{}", System::lineSeparator, gameController::getScreenOutput);
                }

                gameOver = movementResult.isGameOver();
                moveNum++;
            }

            return logger.traceExit(score);
        } catch (GameControllerException e) {
            throw new ArcadeCabinetException(String.format("Error executing arcade cabinet full game at step %d", moveNum), e);
        }
    }

    private void updateGameStatus(GameMovementResult movementResult) {
        logger.traceEntry();

        if (movementResult.isNumBlocksLeftUpdated()) {
            numBlocks = movementResult.getNumBlocksLeft();
        }

        if (movementResult.isBallMoved()) {
            ballPosition = movementResult.getNewPosition();
        }
        if (movementResult.isPaddleMoved()) {
            paddlePosition = movementResult.getNewPosition();
        }

        if (movementResult.isScoreUpdated()) {
            score = movementResult.getScore();
        }

        if (logger.isWarnEnabled() && movementResult.hasWarnings()) {
            logger.warn("Warnings found in movement {} result: {}", moveNum , movementResult);
        } else {
            logger.debug("Movement {} result: {}", moveNum, movementResult);
        }
        logger.debug("Game status: { ball:{}, paddle:{}, score:{}, num blocks:{}}",
                ballPosition != null ? ballPosition : "(no ball)",
                paddlePosition != null ? paddlePosition : "(no paddle)",
                score, numBlocks);

        logger.traceExit();
    }

    private JoystickInput calculateNextInput(GameMovementResult movementResult) {
        logger.traceEntry();

        JoystickInput input = null;

        if (movementResult.isInputNeeded()) {
            if (paddlePosition.getX() < ballPosition.getX()) {
                input = JoystickInput.MOVE_RIGHT;
            } else if (paddlePosition.getX() > ballPosition.getX()) {
                input = JoystickInput.MOVE_LEFT;
            } else {
                input = JoystickInput.NO_MOVE;
            }
        }

        return logger.traceExit(input);
    }

    private boolean isPrintScreen(GameMovementResult movementResult) {
        return logger.isDebugEnabled() &&
                configurationInfo.isLogEnabled() &&
                (moveNum % configurationInfo.getEveryNMoves() == 0 ||
                        movementResult.isScoreUpdated());
    }
}
