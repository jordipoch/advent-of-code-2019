package com.challenge.day15;

import com.challenge.library.geometry.model.Int2DPoint;

import java.util.Objects;

public class CalculationResult {
    private final boolean found;
    private final NotFoundCause notFoundCause;
    private final int minDistanceToOxygen;
    private final Int2DPoint oxygenPosition;
    private final int numMovementsToOxygen;
    private final int numMovements;

    private final int maxDepth;
    private final int maxDistanceExplored;
    private final int maxMovements;

    private final Grid exploredGrid;


    private CalculationResult(Builder builder) {
        this.found = builder.isFound();
        this.minDistanceToOxygen = builder.getMinDistanceToOxygen();
        this.oxygenPosition = builder.getOxygenPosition();
        this.numMovements = builder.getTotalNumMovements();
        this.numMovementsToOxygen = builder.getNumMovementsToOxygen();
        this.notFoundCause = builder.getNotFoundCause();
        this.maxDepth = builder.getMaxDepth();
        this.maxMovements = builder.getMaxMovements();
        this.maxDistanceExplored = builder.getMaxDistanceExplored();
        this.exploredGrid = builder.getExploredGrid();
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

    public Grid getExploredGrid() {
        return exploredGrid;
    }

    public int getMaxDistanceExplored() {
        return maxDistanceExplored;
    }

    public Int2DPoint getOxygenPosition() {
        return oxygenPosition;
    }

    public enum NotFoundCause {
        MAX_DEPTH_EXCEEDED, MAX_MOVEMENTS_REACHED, ALL_SPACE_EXPLORED
    }

    @Override
    public String toString() {
        if (found) {
            return String.format("Oxygen system found at distance %d, position %s! (Num movements: %d). Max distance: %d, Total num movements: %d", minDistanceToOxygen, oxygenPosition, numMovementsToOxygen, maxDistanceExplored, numMovements);
        } else {
            return switch (notFoundCause) {
                case MAX_DEPTH_EXCEEDED -> String.format("Oxygen system not found! Max depth %d reached (Num movements: %d).", maxDepth, numMovements);
                case MAX_MOVEMENTS_REACHED -> String.format("Oxygen system not found! Max movements %d exceeded (current depth: %d).", maxMovements, maxDistanceExplored);
                case ALL_SPACE_EXPLORED -> String.format("Oxygen system not found! All space explored after %d movements (current depth: %d).", numMovements, maxDistanceExplored);
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
        private int totalNumMovements;
        private int minDistanceToOxygen;
        private int maxDistanceExplored;
        private NotFoundCause notFoundCause;
        private int maxDepth;
        private int maxMovements;
        private int numMovementsToOxygen;
        private Grid exploredGrid;
        private Int2DPoint oxygenPosition;

        public Builder(int totalNumMovements) {
            this.totalNumMovements = totalNumMovements;
            this.found = false;
        }

        public Builder(int minDistanceToOxygen, int numMovementsToOxygen, Int2DPoint oxygenPosition) {
            this.minDistanceToOxygen = minDistanceToOxygen;
            this.numMovementsToOxygen = numMovementsToOxygen;
            this.oxygenPosition = oxygenPosition;
            this.found = true;
        }

        public static Builder createResultFound(int minDistanceToOxygen, int numMovementsToOxygen, Int2DPoint oxygenPosition) {
            return new Builder(minDistanceToOxygen, numMovementsToOxygen, oxygenPosition);
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
            this.maxDistanceExplored = currentDepth;
            return this;
        }

        public Builder withCauseAllSpaceExplored(int maxDistanceExplored) {
            checkNotFound();

            notFoundCause = NotFoundCause.ALL_SPACE_EXPLORED;
            this.maxDistanceExplored = maxDistanceExplored;

            return this;
        }

        public Builder withExploredGrid(Grid exploredGrid) {
            this.exploredGrid = exploredGrid;
            return this;
        }

        public Builder withMaxDistanceExplored(int maxDistanceExplored) {
            this.maxDistanceExplored = maxDistanceExplored;
            return this;
        }

        public Builder withTotalNumMovements(int totalNumMovements) {
            this.totalNumMovements = totalNumMovements;
            return this;
        }

        private void checkNotFound() {
            if (found) {
                throw new IllegalStateException("This method should not be called when result is found");
            }
        }



        public CalculationResult build() {
            return new CalculationResult(this);
        }

        public boolean isFound() {
            return found;
        }

        public int getMinDistanceToOxygen() {
            return minDistanceToOxygen;
        }

        public Int2DPoint getOxygenPosition() {
            return oxygenPosition;
        }

        public int getTotalNumMovements() {
            return totalNumMovements;
        }

        public int getMaxDistanceExplored() {
            return maxDistanceExplored;
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

        public int getNumMovementsToOxygen() {
            return numMovementsToOxygen;
        }

        public Grid getExploredGrid() {
            return exploredGrid;
        }
    }
}
