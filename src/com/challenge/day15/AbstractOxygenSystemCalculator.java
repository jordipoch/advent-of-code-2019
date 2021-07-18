package com.challenge.day15;

import com.challenge.day15.exception.DroidEngineException;
import com.challenge.day15.exception.DroidMoveException;
import com.challenge.day15.exception.MaxMovementsExceededException;
import com.challenge.day15.exception.OxygenSystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.challenge.day15.CalculationResult.Builder.createResultFound;
import static com.challenge.day15.CalculationResult.Builder.createResultFoundNotFound;

public abstract class AbstractOxygenSystemCalculator implements OxygenSystemCalculator {
    private static final Logger logger = LogManager.getLogger();

    protected final DroidController droidController;
    protected int numMovements;
    protected int maxMovements;
    protected boolean newPositionFound;

    protected AbstractOxygenSystemCalculator(DroidController droidController) {
        this.droidController = droidController;
    }

    @Override
    public CalculationResult calculateMinDistanceToOxygen(int maxDepth, int maxMovements) throws OxygenSystemException {
        logger.traceEntry("Calculating min distance to oxygen, with max depth = {}", maxDepth);

        numMovements = 0;
        this.maxMovements = maxMovements;
        var currentDepth = 1;
        var processFinished = false;
        ExploreResult exploreResult;

        try {
            do {
                exploreResult = exploreInDepth(0, currentDepth);
                logger.debug("Explored space at depth {}:{}{}", currentDepth, System.lineSeparator(), droidController.getExploredSpaceAsString());

                newPositionFound = exploreResult.isNewPositionFound();
                processFinished = isProcessFinished(maxDepth, currentDepth, exploreResult);
                if (!processFinished) currentDepth++;
            } while (!processFinished);
        } catch (DroidEngineException | DroidMoveException e) {
            throw new OxygenSystemException("Error while moving the droid.", e);
        } catch (MaxMovementsExceededException e) {
            logger.info(e.getMessage());
            return logger.traceExit(createResultFoundNotFound(numMovements).withCauseMaxMovementsExceeded(maxMovements, currentDepth).build());
        }

        var result = getCalculationResult(maxDepth, currentDepth, exploreResult);
        logger.info("PROCESS FINISHED. Result: {}", result);
        logger.info("Explored space:{}{}", System.lineSeparator(), droidController.getExploredSpaceAsString());

        return logger.traceExit(result);
    }

    private ExploreResult exploreInDepth(int currentDepth, int maxDepth) throws DroidEngineException, DroidMoveException, MaxMovementsExceededException {
        logger.traceEntry("Exploring in depth... currentDepth={}, maxDepth={}", currentDepth, maxDepth);

        if (currentDepth + 1 == maxDepth) {
            return exploreNewPositions(currentDepth);
        } else { // currentDepth + 1 < maxDepth
            return exploreKnownPositions(currentDepth, maxDepth);
        }
    }

    private ExploreResult exploreNewPositions(int currentDepth) throws DroidEngineException, DroidMoveException, MaxMovementsExceededException {
        var possibleDirections = droidController.getDirectionsToUnknownPositions();
        var exploreResult = new ExploreResult(false, false);
        for (DroidDirection direction : possibleDirections) {
            var newExploreResult= tryMoveDroid(direction, currentDepth + 1);
            exploreResult.merge(newExploreResult);
            if (exploreResult.isOxygenFound()) return exploreResult;
        }

        if (possibleDirections.isEmpty()) {
            trackSpaceExplored();
        }
        return logger.traceExit(exploreResult);
    }

    private ExploreResult exploreKnownPositions(int currentDepth, int maxDepth) throws DroidEngineException, DroidMoveException, MaxMovementsExceededException {
        var possibleDirections = droidController.getDirectionsToEmptyPositions();
        var exploreResult = new ExploreResult(false, false);
        for (DroidDirection direction : possibleDirections) {
            var newExploreResult = moveDroidAndExplore(direction, currentDepth, maxDepth);
            exploreResult.merge(newExploreResult);
            if (exploreResult.isOxygenFound()) return exploreResult;
        }

        if (droidController.getDirectionsToEmptyPositions().size() <= 1) {
            trackSpaceExplored();
        }
        return logger.traceExit(exploreResult);
    }

    private boolean isProcessFinished(int maxDepth, int currentDepth, ExploreResult exploreResult) {
        boolean processFinished;
        processFinished = exploreResult.isOxygenFound() || !exploreResult.isNewPositionFound() || currentDepth >= maxDepth;
        return processFinished;
    }


    protected boolean checkPathCanContinue(int currentDepth, int maxDepth) {
        return true;
    }

    protected void trackCurrentPosition(int currentDepth) {
    }

    protected void trackSpaceExplored() {
    }

    private CalculationResult getCalculationResult(int maxDepth, int currentDepth, ExploreResult exploreResult) {
        if (exploreResult.isOxygenFound()) {
            return createResultFound(currentDepth, numMovements).build();
        } else {
            if (!exploreResult.isNewPositionFound()) {
                return createResultFoundNotFound(numMovements).withCauseAllSpaceExplored(currentDepth).build();
            } else {
                return createResultFoundNotFound(numMovements).withCauseMaxDepthExceeded(maxDepth).build();
            }
        }
    }

    private ExploreResult tryMoveDroid(DroidDirection direction, int currentDepth) throws DroidEngineException, DroidMoveException, MaxMovementsExceededException {
        if (numMovements == maxMovements) {
            throw new MaxMovementsExceededException(maxMovements);
        }

        var oxygenFound = false;
        var result = droidController.tryMoveDroid(direction);

        switch (result.getMovementResult()) {
            case OXYGEN_SYSTEM -> {
                numMovements++;
                logger.info("Tried and moved {} direction to new position {}. Movement num {}. OXYGEN SYSTEM FOUND!!", direction, droidController.getDroidPosition(), numMovements);
                oxygenFound = true;
                moveDroid(direction.getReverseDirection());
            }
            case MOVED -> {
                numMovements++;
                logger.debug("Tried and moved {} direction to {} new position {}. Movement num {}.", direction, result.isPositionNew() ? "new" : "known", droidController.getDroidPosition(), numMovements);
                trackCurrentPosition(currentDepth);
                moveDroid(direction.getReverseDirection());
            }
            case WALL -> logger.debug("Tried moving {} direction from position {} but found a WALL", direction, droidController.getDroidPosition());
            default -> logger.warn("Unknown result found ({})!", result.getMovementResult());
        }

        return new ExploreResult(oxygenFound, result.isPositionNew());
    }

    private ExploreResult moveDroidAndExplore(DroidDirection direction, int currentDepth, int maxDepth) throws DroidEngineException, DroidMoveException, MaxMovementsExceededException {
        moveDroid(direction);

        ExploreResult exploreResult;
        if (checkPathCanContinue(currentDepth, maxDepth)) {
            exploreResult = exploreInDepth(currentDepth + 1, maxDepth);
        } else {
            logger.debug("Halting exploration at depth {}. Current depth = {}", currentDepth, maxDepth);
            exploreResult = new ExploreResult(false);
        }

        moveDroid(direction.getReverseDirection());

        return exploreResult;
    }

    private void moveDroid(DroidDirection direction) throws DroidEngineException, DroidMoveException, MaxMovementsExceededException {
        if (numMovements == maxMovements) {
            throw new MaxMovementsExceededException(maxMovements);
        }

        droidController.moveDroid(direction);
        numMovements++;

        logger.debug("Moved {} direction to known position {}. Movement num {}.", direction, droidController.getDroidPosition(), numMovements);
    }

    private static class ExploreResult {
        private boolean oxygenFound;
        private boolean newPositionFound;

        public ExploreResult(boolean oxygenFound) {
            this.oxygenFound = oxygenFound;
        }

        public ExploreResult(boolean oxygenFound, boolean newPositionFound) {
            this.oxygenFound = oxygenFound;
            this.newPositionFound = newPositionFound;
        }

        public boolean isOxygenFound() {
            return oxygenFound;
        }

        public boolean isNewPositionFound() {
            return newPositionFound;
        }

        public void merge(ExploreResult newExploreResult) {
            oxygenFound |= newExploreResult.isOxygenFound();
            newPositionFound |= newExploreResult.isNewPositionFound();
        }
    }
}
