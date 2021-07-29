package com.challenge.day15;

import com.challenge.day15.exception.OxygenSystemException;

public interface OxygenSystemCalculator {
    CalculationResult calculateMinDistanceToOxygen(int maxDepth, int maxMovements) throws OxygenSystemException;
    CalculationResult calculateMinDistanceToOxygen(int maxDepth, int maxMovements, boolean stopOnOxygenFound) throws OxygenSystemException;
}
