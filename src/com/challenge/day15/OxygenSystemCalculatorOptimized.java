package com.challenge.day15;

import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class OxygenSystemCalculatorOptimized extends AbstractOxygenSystemCalculator {
    private static final Logger logger = LogManager.getLogger();
    private final Map<Int2DPoint, Integer> minDistancesMap = new HashMap<>();
    private final OptimizationOptions optimizationOptions;


    public OxygenSystemCalculatorOptimized(DroidController droidController, OptimizationOptions optimizationOptions) {
        super(droidController);
        this.optimizationOptions = optimizationOptions;
    }

    @Override
    protected void trackCurrentPosition(int currentDepth) {
        logger.traceEntry("Current depth = {}", currentDepth);

        if (!optimizationOptions.isNotGoBackAllowed()) {
            logger.traceExit();
            return;
        }

        var currentPosition = droidController.getDroidPosition();

        minDistancesMap.compute(currentPosition, (k, v) -> {
            if (v == null || v > currentDepth) {
                logger.debug("Added new min distance {} at position {}", currentDepth, currentPosition);
                return currentDepth;
            } else return v;
        });

        logger.traceExit();
    }

    @Override
    protected boolean checkPathCanContinue(int currentDepth, int maxDepth) {
        logger.traceEntry();

        if (!optimizationOptions.isNotGoBackAllowed()) {
            return logger.traceExit(true);
        }

        var currentPosition = droidController.getDroidPosition();
        if (currentPosition.equals(Int2DPoint.ORIGIN)) return logger.traceExit(true);

        if (minDistancesMap.containsKey(currentPosition) && minDistancesMap.get(currentPosition) >= currentDepth) {
            return logger.traceExit(true);
        }

        if (!minDistancesMap.containsKey(currentPosition)) {
            logger.warn("No min distance found at position {}", currentPosition);
        }

        var minDistance = minDistancesMap.get(currentPosition);
        logger.debug("Wrong path found at position {}. Min distance is {} while the currentDistance is {}. Max depth = {}", currentPosition, minDistance, currentDepth, maxDepth);

        return logger.traceExit(false);
    }

    @Override
    protected void trackSpaceExplored() {
        logger.traceEntry();

        if (optimizationOptions.isDiscardExploredPaths()) {
            droidController.markCurrentPositionAsExplored();
        }

        logger.traceExit();
    }

    public static class OptimizationOptions {
        private final boolean notGoBackAllowed;
        private final boolean discardExploredPaths;

        private OptimizationOptions(boolean notGoBackAllowed, boolean discardExploredPaths) {
            this.notGoBackAllowed = notGoBackAllowed;
            this.discardExploredPaths = discardExploredPaths;
        }

        public boolean isNotGoBackAllowed() {
            return notGoBackAllowed;
        }

        public boolean isDiscardExploredPaths() {
            return discardExploredPaths;
        }

        public static class Builder {
            private boolean notGoBackAllowed;
            private boolean discardExploredPaths;

            public static Builder createOptimizationOptions() {
                return new Builder();
            }

            public Builder withNotGoBackAllowed() {
                this.notGoBackAllowed = true;
                return this;
            }

            public Builder withDiscardExploredPaths() {
                this.discardExploredPaths = true;
                return this;
            }

            public OptimizationOptions build() {
                if (!notGoBackAllowed && !discardExploredPaths) {
                    throw new IllegalStateException("No optimizations have been set!");
                }

                return new OptimizationOptions(notGoBackAllowed, discardExploredPaths);
            }
        }
    }
}
