package com.challenge.day15;

import com.challenge.day15.exception.DroidEngineException;
import com.challenge.day15.exception.DroidMoveException;
import com.challenge.day15.exception.MaxMovementsExceededException;
import com.challenge.day15.exception.OxygenSystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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
        var oxygenFound = false;
        var processFinished = false;

        try {
            do {
                newPositionFound = false;
                var exploreResult = exploreInDepth(0, currentDepth);
                logger.debug("Explored space at depth {}:{}{}", currentDepth, System.lineSeparator(), droidController.getExploredSpaceAsString());

                oxygenFound = exploreResult.isOxygenFound();
                processFinished = oxygenFound || !newPositionFound || currentDepth >= maxDepth;
                if (!processFinished) currentDepth++;
            } while (!processFinished);
        } catch (DroidEngineException | DroidMoveException e) {
            throw new OxygenSystemException("Error while moving the droid.", e);
        } catch (MaxMovementsExceededException e) {
            logger.info(e.getMessage());
            return logger.traceExit(createResultFoundNotFound(numMovements).withCauseMaxMovementsExceeded(maxMovements, currentDepth).build());
        }

        var result = getCalculationResult(maxDepth, currentDepth, oxygenFound);
        logger.info("PROCESS FINISHED. Result: {}", result);

        return logger.traceExit(result);
    }

    protected ExploreResult exploreInDepth(int currentDepth, int maxDepth) throws DroidEngineException, DroidMoveException, MaxMovementsExceededException {
        logger.traceEntry("Exploring in depth... currentDepth={}, maxDepth={}", currentDepth, maxDepth);

        if (currentDepth + 1 == maxDepth) {
            var possibleDirections = getDirectionsToUnknownPositions();
            var oxygenFound = false;
            for (DroidDirection direction : possibleDirections) {
                oxygenFound = tryMoveDroid(direction, currentDepth + 1);
                if (oxygenFound) break;
            }
            var spaceExplored = false;
            if (possibleDirections.isEmpty()) {
               spaceExplored = trackSpaceExplored();
            }
            return logger.traceExit(new ExploreResult(oxygenFound, spaceExplored));
        } else { // currentDepth + 1 < maxDepth
            var possibleDirections = getDirectionsToEmptyPositions(currentDepth, maxDepth);
            var exploreResult = new ExploreResult(false, false);
            for (DroidDirection direction : possibleDirections) {
                var newExploreResult = moveDroidAndExplore(direction, currentDepth, maxDepth);
                exploreResult.merge(newExploreResult);
                if (exploreResult.isOxygenFound()) return exploreResult;
            }
            if (possibleDirections.size() <= 1) {
                exploreResult.setAllSpaceExplored(trackSpaceExplored());
            }
            return logger.traceExit(exploreResult);
        }
    }


    protected boolean checkPathCanContinue(int currentDepth, int maxDepth) {
        return true;
    }


    protected void trackCurrentPosition(int currentDepth) {
    }

    protected boolean trackSpaceExplored() {
        return false;
    }

    private CalculationResult getCalculationResult(int maxDepth, int currentDepth, boolean oxygenFound) {
        if (oxygenFound) {
            return createResultFound(currentDepth, numMovements).build();
        } else {
            if (!newPositionFound) {
                return createResultFoundNotFound(numMovements).withCauseAllSpaceExplored(currentDepth).build();
            } else {
                return createResultFoundNotFound(numMovements).withCauseMaxDepthExceeded(maxDepth).build();
            }
        }
    }

    private boolean tryMoveDroid(DroidDirection direction, int currentDepth) throws DroidEngineException, DroidMoveException, MaxMovementsExceededException {
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

        newPositionFound |= result.isPositionNew();

        return oxygenFound;
    }

    private ExploreResult moveDroidAndExplore(DroidDirection direction, int currentDepth, int maxDepth) throws DroidEngineException, DroidMoveException, MaxMovementsExceededException {
        moveDroid(direction);

        ExploreResult exploreResult;
        if (checkPathCanContinue(currentDepth, maxDepth)) {
            exploreResult = exploreInDepth(currentDepth + 1, maxDepth);
        } else {
            logger.debug("Halting exploration at depth {}. Current depth = {}", currentDepth, maxDepth);
            exploreResult = new ExploreResult(false, false);
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

    private List<DroidDirection> getDirectionsToEmptyPositions(int currentDepth, int maxDepth) {
        logger.traceEntry("Getting directions to empty positions (current position = {}, current depth = {}, max depth = {}).", droidController.getDroidPosition(), currentDepth, maxDepth);

        List<NextMovement> nextMovements = droidController.getNextMovementsFromCurrentPosition();
        List<DroidDirection> directions = new ArrayList<>();
        for (NextMovement nextMovement : nextMovements) {
            if (nextMovement.getCellType() == CellType.EMPTY) {
                directions.add(nextMovement.getDirection());
            } else if (nextMovement.getCellType() == CellType.UNKNOWN) {
                logger.warn("Not expecting to find UNKNOWN cell type at position {}. Current depth = {}, Max depth = {}.", droidController.getDroidPosition(), currentDepth, maxDepth);
            }
        }

        return logger.traceExit("Got directions: ", directions);
    }

    private List<DroidDirection> getDirectionsToUnknownPositions() {
        List<NextMovement> nextMovements = droidController.getNextMovementsFromCurrentPosition();
        List<DroidDirection> directions = new ArrayList<>();
        for (NextMovement nextMovement : nextMovements) {
            if (nextMovement.getCellType() == CellType.UNKNOWN) {
                directions.add(nextMovement.getDirection());
            }
        }

        return directions;
    }

    private static class ExploreResult {
        private boolean oxygenFound;
        private boolean allSpaceExplored;

        public ExploreResult(boolean oxygenFound, boolean allSpaceExplored) {
            this.oxygenFound = oxygenFound;
            this.allSpaceExplored = allSpaceExplored;
        }

        public boolean isOxygenFound() {
            return oxygenFound;
        }

        public boolean isAllSpaceExplored() {
            return allSpaceExplored;
        }

        public void setAllSpaceExplored(boolean allSpaceExplored) {
            this.allSpaceExplored = allSpaceExplored;
        }

        public void merge(ExploreResult newExploreResult) {
            oxygenFound |= newExploreResult.isOxygenFound();
            allSpaceExplored &= newExploreResult.allSpaceExplored;
        }
    }
}
