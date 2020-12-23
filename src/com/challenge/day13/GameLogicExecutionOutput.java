package com.challenge.day13;

public class GameLogicExecutionOutput {
    private final Tile tile;
    private final long currentScore;
    private final ExecutionOutputType executionOutputType;

    private GameLogicExecutionOutput(Tile tile) {
        this(tile, -1, ExecutionOutputType.TILE_CHANGED);
    }

    private GameLogicExecutionOutput(long score) {
        this(null, score, ExecutionOutputType.SCORE_UPDATED);
    }

    private GameLogicExecutionOutput(ExecutionOutputType executionOutputType) {
        this(null, -1, executionOutputType);
    }

    private GameLogicExecutionOutput(Tile tile, long currentScore, ExecutionOutputType executionOutputType) {
        this.tile = tile;
        this.currentScore = currentScore;
        this.executionOutputType = executionOutputType;
    }

    public static GameLogicExecutionOutput createTileChangedOutput(Tile tile) {
        return new GameLogicExecutionOutput(tile);
    }

    public static GameLogicExecutionOutput createScoreUpdatedOutput(long score) {
        return new GameLogicExecutionOutput(score);
    }

    public static GameLogicExecutionOutput createGameOverOutput() {
        return new GameLogicExecutionOutput(ExecutionOutputType.GAME_OVER);
    }

    public static GameLogicExecutionOutput createInputNeededOutput() {
        return new GameLogicExecutionOutput(ExecutionOutputType.INPUT_NEEDED);
    }

    public ExecutionOutputType getOutputType() {
        return executionOutputType;
    }

    public Tile getTile() {
        return tile;
    }

    public long getCurrentScore() {
        return currentScore;
    }

    public enum ExecutionOutputType {
        TILE_CHANGED, SCORE_UPDATED, GAME_OVER, INPUT_NEEDED
    }

    @Override
    public String toString() {
        return "{ " + executionOutputType +
                ": " + (switch(executionOutputType) {
                        case TILE_CHANGED -> tile;
                        case SCORE_UPDATED -> Long.toString(currentScore);
                        default -> "";
                    }) +
                '}';
    }
}
