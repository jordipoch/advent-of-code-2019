package com.challenge.day13;

import com.challenge.day13.exception.GameMovementResultBuildException;
import com.challenge.library.geometry.model.Int2DPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class GameMovementResult {
    private final MovementType movementType;
    private final Int2DPoint newPosition;
    private final int numBlocksLeft;
    private final long score;

    private final List<String> warnings;

    private GameMovementResult(MovementType movementType, Int2DPoint newPosition, int numBlocksLeft, long score, List<String> warnings) {
        this.movementType = movementType;
        this.newPosition = newPosition;
        this.numBlocksLeft = numBlocksLeft;
        this.score = score;
        this.warnings = warnings;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public Int2DPoint getNewPosition() {
        return newPosition;
    }

    public int getNumBlocksLeft() {
        return numBlocksLeft;
    }

    public long getScore() {
        return score;
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public boolean isBallMoved() {
        return movementType == MovementType.BALL_MOVED || movementType == MovementType.BALL_MOVED_AND_BLOCK_BROKEN;
    }

    public boolean isBlockBroken() {
        return movementType == MovementType.BLOCK_BROKEN || movementType == MovementType.BALL_MOVED_AND_BLOCK_BROKEN;
    }

    public boolean isPaddleMoved() {
        return movementType == MovementType.PADDLE_MOVED;
    }

    public boolean isScoreUpdated() {
        return movementType == MovementType.SCORE_UPDATED;
    }

    public boolean isNumBlocksLeftUpdated() {
        return numBlocksLeft != -1;
    }

    public boolean isInputNeeded() {
        return movementType == MovementType.INPUT_NEEDED;
    }

    public boolean isGameOver() { return movementType == MovementType.GAME_OVER; }

    public enum MovementType {
        SCENARIO_BUILD,
        BLOCK_BROKEN,
        BALL_MOVED,
        PADDLE_MOVED,
        BALL_MOVED_AND_BLOCK_BROKEN,
        BALL_OR_PADDLE_CLEAN_UP,
        SCORE_UPDATED,
        NO_CHANGE,
        UNKNOWN,
        INPUT_NEEDED,
        GAME_OVER
    }

    public static class Builder {
        private MovementType movementType;
        private Int2DPoint newPosition;
        private int numBlocksLeft = -1;
        private long newScore = -1;
        private final List<String> warnings = new ArrayList<>();

        private Builder() {}

        public static Builder createNewGameMovementResult() {
            return new Builder();
        }

        public Builder withMovementType(MovementType movementType) {
            this.movementType = movementType;
            return this;
        }

        public Builder withNewPosition(Int2DPoint newPosition) {
            checkMovementNotNull();
            if (movementType != MovementType.BALL_MOVED && movementType != MovementType.PADDLE_MOVED && movementType != MovementType.BALL_MOVED_AND_BLOCK_BROKEN) {
                throw new GameMovementResultBuildException(String.format("Can't assign new position with movement type = %s", movementType));
            }
            this.newPosition = newPosition;
            return this;
        }

        public Builder withNumBlocksLeft(int numBlocksLeft) {
            checkMovementNotNull();
            if (movementType != MovementType.BLOCK_BROKEN && movementType != MovementType.BALL_MOVED_AND_BLOCK_BROKEN && movementType != MovementType.SCENARIO_BUILD) {
                throw new GameMovementResultBuildException(String.format("Can't assign num blocks left with movement type = %s", movementType));
            }
            this.numBlocksLeft = numBlocksLeft;
            return this;
        }

        public Builder withNewScore(long newScore) {
            checkMovementNotNull();
            if (movementType != MovementType.SCORE_UPDATED) {
                throw new GameMovementResultBuildException(String.format("Only with movement type = %s can new score be assigned", MovementType.SCORE_UPDATED));
            }
            this.newScore = newScore;
            return this;
        }

        public Builder withWarning(String warning) {
            checkMovementNotNull();
            warnings.add(warning);
            return this;
        }

        public Builder withWarning(Supplier<Optional<String>> messageSupplier) {
            messageSupplier.get().ifPresent(warnings::add);
            return this;
        }

        public GameMovementResult build() {
            performChecks();
            return new GameMovementResult(movementType, newPosition, numBlocksLeft, newScore, warnings);
        }

        private void checkMovementNotNull() {
            if (movementType == null) {
                throw new GameMovementResultBuildException("Movement type not assigned yet. You must assign the movement type first.");
            }
        }

        private void performChecks() {
            if (movementType == MovementType.BALL_MOVED || movementType == MovementType.PADDLE_MOVED || movementType == MovementType.BALL_MOVED_AND_BLOCK_BROKEN) {
                if (newPosition == null) {
                    throw new GameMovementResultBuildException(String.format("Expecting a new position with movement type = %s", movementType));
                }
            }

            if (movementType == MovementType.BLOCK_BROKEN || movementType == MovementType.BALL_MOVED_AND_BLOCK_BROKEN || movementType == MovementType.SCENARIO_BUILD) {
                if (numBlocksLeft == -1) {
                    throw new GameMovementResultBuildException(String.format("Expecting num blocks left with movement type = %s", movementType));
                }
            }

            if (movementType == MovementType.SCORE_UPDATED && newScore == -1) {
                throw new GameMovementResultBuildException(String.format("Expecting new score with movement type = %s", movementType));
            }

            if (movementType == MovementType.UNKNOWN && warnings.isEmpty()) {
                throw new GameMovementResultBuildException(String.format("Expecting at least one warning with movement type = %s", movementType));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("{ %s", movementType));
        if (newPosition != null) {
            sb.append(String.format(", pos=%s", newPosition));
        }
        if (numBlocksLeft != -1) {
            sb.append(String.format(", blocks=%d", numBlocksLeft));
        }
        if (score != -1) {
            sb.append(String.format(", score=%d", score));
        }
        if (!hasWarnings()) {
            sb.append(", no warnings");
        } else {
            sb.append(String.format(", %d warnings: %s", warnings.size(), warnings));
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMovementResult that = (GameMovementResult) o;
        return numBlocksLeft == that.numBlocksLeft && score == that.score && movementType == that.movementType && Objects.equals(newPosition, that.newPosition)
                && warnings.size() == that.warnings.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementType, newPosition, numBlocksLeft, score);
    }
}
