package com.challenge.day15;

import com.challenge.day15.exception.DroidEngineException;
import com.challenge.day15.exception.DroidMoveException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractDroidController implements DroidController {
    private static final Logger logger = LogManager.getLogger();

    protected Grid grid;
    protected Int2DPoint position = Int2DPoint.ORIGIN;

    protected AbstractDroidController(Grid grid) {
        this.grid = grid;
    }

    @Override
    public void moveDroid(DroidDirection direction) throws DroidMoveException, DroidEngineException {
        logger.traceEntry("Move to direction {} from position {}.", direction, position);

        var intendedPosition = direction.moveDirection(position);

        var cellType = grid.getCell(intendedPosition).getCellType();
        if (cellType != CellType.EMPTY) {
            throw new DroidMoveException(DroidMoveException.ErrorType.NOT_EMPTY_CELL, cellType, intendedPosition);
        }

        var result = doMoveDroid(direction);
        if (result != MovementResult.MOVED) {
            throw new DroidMoveException(DroidMoveException.ErrorType.UNEXPECTED_ENGINE_RESULT, result, direction, position);
        }

        position = intendedPosition;

        logger.traceExit();
    }

    @Override
    public TryMoveDroidResult tryMoveDroid(DroidDirection direction) throws DroidEngineException {
        logger.traceEntry("Try move to direction {} from position {}.", direction, position);

        var intendedPosition = direction.moveDirection(position);
        var result = doMoveDroid(direction);

        if (result != MovementResult.WALL) {
            position = intendedPosition;
        }

        var cellType = grid.getCell(intendedPosition).getCellType();
        if (cellType == CellType.UNKNOWN) {
            grid.putCell(result.getCellType(), intendedPosition);
            return logger.traceExit(TryMoveDroidResult.ofNewPosition(result));
        } else {
            logger.warn("Trying to move to a known position. Movement: {} -> {}. Cell type at destination: {}", position, intendedPosition, cellType);
            return logger.traceExit(TryMoveDroidResult.ofKnownPosition(result));
        }
    }

    @Override
    public void markCurrentPositionAsExplored() {
        if (!position.equals(Int2DPoint.ORIGIN)) {
            grid.putCell(CellType.EXPLORED, position);
            logger.debug("Marked position {} as explored", position);
        }
    }

    protected abstract MovementResult doMoveDroid(DroidDirection direction) throws DroidEngineException;

    @Override
    public Int2DPoint getDroidPosition() {
        return position;
    }

    @Override
    public List<DroidDirection> getDirectionsToEmptyPositions() {
        logger.traceEntry("Getting directions to empty positions (current position = {}).", getDroidPosition());

        List<NextMovement> nextMovements = getNextMovementsFromCurrentPosition();

        return logger.traceExit(nextMovements.stream()
                .peek(nextMovement -> {
                    if (nextMovement.getCellType() == CellType.UNKNOWN)
                        logger.warn("Not expecting to find UNKNOWN cell type at position {}", nextMovement.getDirection().moveDirection(getDroidPosition()));
                })
                .filter(nextMovement -> nextMovement.getCellType() == CellType.EMPTY)
                .map(NextMovement::getDirection)
                .collect(Collectors.toList())
        );
    }

    @Override
    public List<DroidDirection> getDirectionsToUnknownPositions() {
        logger.traceEntry("Getting directions to unknown positions (current position = {}).", getDroidPosition());

        List<NextMovement> nextMovements = getNextMovementsFromCurrentPosition();
        return logger.traceExit(nextMovements.stream()
                .filter(nextMovement -> nextMovement.getCellType() == CellType.UNKNOWN)
                .map(NextMovement::getDirection)
                .collect(Collectors.toList())
        );
    }

    private List<NextMovement> getNextMovementsFromCurrentPosition() {
        return DroidDirection.getAllDirections().stream()
                .map(d -> NextMovement.of(d, grid.getCell(d.moveDirection(position)).getCellType()))
                .collect(Collectors.toList());
    }

    @Override
    public String getExploredSpaceAsString() {
        return grid.toString();
    }
}
