package com.challenge.day15;

import java.util.Objects;

public class CalculationResult {
    private boolean found;
    private NotFoundCause notFoundCause;
    private int minDistanceToOxygen;
    private int numMovements;

    private int maxDepth;
    private int currentDepth;
    private int maxMovements;


    private CalculationResult(boolean found, int minDistanceToOxygen, int numMovements, NotFoundCause notFoundCause,
                             int maxDepth, int maxMovements, int currentDepth) {
        this.found = found;
        this.minDistanceToOxygen = minDistanceToOxygen;
        this.numMovements = numMovements;
        this.notFoundCause = notFoundCause;
        this.maxDepth = maxDepth;
        this.maxMovements = maxMovements;
        this.currentDepth = currentDepth;
    }

    public boolean isFound() {
        return found;
    }

    public int getMinDistanceToOxygen() {
        return minDistanceToOxygen;
    }

    public int getNumMovements() {
        return numMovements;
    }

    public NotFoundCause getNotFoundCause() {
        return notFoundCause;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMaxMovements() {
        return maxMovements;
    }

    public enum NotFoundCause {
        MAX_DEPTH_EXCEEDED, MAX_MOVEMENTS_REACHED, ALL_SPACE_EXPLORED
    }

    @Override
    public String toString() {
        if (found) {
            return String.format("Oxygen system found at distance %d! (Num movements: %d).", minDistanceToOxygen, numMovements);
        } else {
            return switch (notFoundCause) {
                case MAX_DEPTH_EXCEEDED -> String.format("Oxygen system not found! Max depth %d reached (Num movements: %d).", maxDepth, numMovements);
                case MAX_MOVEMENTS_REACHED -> String.format("Oxygen system not found! Max movements %d exceeded (current depth: %d).", maxMovements, currentDepth);
                case ALL_SPACE_EXPLORED -> String.format("Oxygen system not found! All space explored after %d movements (current depth: %d).", numMovements, currentDepth);
            };
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculationResult that = (CalculationResult) o;
        return found == that.found && minDistanceToOxygen == that.minDistanceToOxygen && numMovements == that.numMovements && maxDepth == that.maxDepth && maxMovements == that.maxMovements && notFoundCause == that.notFoundCause;
    }

    @Override
    public int hashCode() {
        return Objects.hash(found, notFoundCause, minDistanceToOxygen, numMovements, maxDepth, maxMovements);
    }

    public static class Builder {
        private final boolean found;
        private int minDistanceToOxygen;
        private final int numMovements;
        private int currentDepth;
        private NotFoundCause notFoundCause;
        private int maxDepth;
        private int maxMovements;

        public Builder(int numMovements) {
            this.numMovements = numMovements;
            this.found = false;
        }

        public Builder(int minDistanceToOxygen, int numMovements) {
            this.minDistanceToOxygen = minDistanceToOxygen;
            this.numMovements = numMovements;
            this.found = true;
        }

        public static Builder createResultFound(int minDistanceToOxygen, int numMovements) {
            return new Builder(minDistanceToOxygen, numMovements);
        }

        public static Builder createResultFoundNotFound(int numMovements) {
            return new Builder(numMovements);
        }

        public Builder withCauseMaxDepthExceeded(int maxDepth) {
            checkNotFound();

            notFoundCause = NotFoundCause.MAX_DEPTH_EXCEEDED;
            this.maxDepth = maxDepth;
            return this;
        }

        public Builder withCauseMaxMovementsExceeded(int maxMovements, int currentDepth) {
            checkNotFound();

            this.notFoundCause = NotFoundCause.MAX_MOVEMENTS_REACHED;
            this.maxMovements = maxMovements;
            this.currentDepth = currentDepth;
            return this;
        }

        public Builder withCauseAllSpaceExplored(int currentDepth) {
            checkNotFound();

            notFoundCause = NotFoundCause.ALL_SPACE_EXPLORED;
            this.currentDepth = currentDepth;

            return this;
        }

        private void checkNotFound() {
            if (found) {
                throw new IllegalStateException("This method should not be called when result is found");
            }
        }

        public CalculationResult build() {
            return new CalculationResult(found, minDistanceToOxygen, numMovements, notFoundCause, maxDepth, maxMovements, currentDepth);
        }
    }
}
