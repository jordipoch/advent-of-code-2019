package com.challenge.day15;

import com.challenge.day15.exception.OxygenSystemException;

import static com.challenge.day15.OxygenSystemCalculatorFactory.Optimizations.DISCARD_EXPLORED_PATHS;
import static com.challenge.day15.OxygenSystemCalculatorFactory.Optimizations.NO_GO_BACK_ALLOWED;

public class Day15 {
    public static void main(String[] args) {
        try {
            // 466050 movements with NO_GO_BACK_ALLOWED only
            // 381502 movements with NO_GO_BACK_ALLOWED and DISCARD_EXPLORED_PATHS
            var calculationResult = runDay15Part1(); // Distance: 380
            System.out.println(calculationResult);
        } catch (OxygenSystemException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public static CalculationResult runDay15Part1() throws OxygenSystemException {
        var oxygenSystemCalculator = OxygenSystemCalculatorFactory.getInstance().createOptimizedCalculator("Input.txt", NO_GO_BACK_ALLOWED, DISCARD_EXPLORED_PATHS);
        return oxygenSystemCalculator.calculateMinDistanceToOxygen(1000, 1_000_000);
    }
}
