package com.challenge.day15;

import com.challenge.day15.exception.DroidEngineException;
import com.challenge.day15.exception.DroidMoveException;
import com.challenge.library.geometry.model.Int2DPoint;

import java.util.List;

public interface DroidController {
    void moveDroid(DroidDirection direction) throws DroidMoveException, DroidEngineException;
    TryMoveDroidResult tryMoveDroid(DroidDirection direction) throws DroidEngineException;
    Int2DPoint getDroidPosition();
    List<DroidDirection> getDirectionsToPositionsToMoveTo();
    List<DroidDirection> getDirectionsToUnknownPositions();
    String getExploredSpaceAsString();
    Grid getCopyOfExploredGrid();
    void markCurrentPositionAsExplored();
}
