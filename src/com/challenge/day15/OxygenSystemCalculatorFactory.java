package com.challenge.day15;

import com.challenge.day15.OxygenSystemCalculatorOptimized.OptimizationOptions;
import com.challenge.day15.exception.DroidControllerCreationException;
import com.challenge.day15.exception.OxygenSystemException;

import java.nio.file.Path;

import static com.challenge.day15.OxygenSystemCalculatorOptimized.OptimizationOptions.Builder.createOptimizationOptions;

public class OxygenSystemCalculatorFactory {
    private static final OxygenSystemCalculatorFactory INSTANCE = new OxygenSystemCalculatorFactory();
    private static final Path BASE_PATH = Path.of("resources","com", "challenge", "day15");

    public static OxygenSystemCalculatorFactory getInstance() { return INSTANCE; }

    public OxygenSystemCalculator createBruteForceCalculator(DroidController droidController) {
        return new OxygenSystemCalculatorBruteForce(droidController);
    }

    public OxygenSystemCalculator createOptimizedCalculator(DroidController droidController, Optimizations... optimizations) {
        final var optimizationOptions = buildOptimizationOptions(optimizations);
        return new OxygenSystemCalculatorOptimized(droidController, optimizationOptions);
    }

    public OxygenSystemCalculator createOptimizedCalculator(String file, Optimizations... optimizations) throws OxygenSystemException {
        final var optimizationOptions = buildOptimizationOptions(optimizations);
        try {
            var droidController = DroidControllerFactory.getInstance().createDroidController(BASE_PATH, file);
            return new OxygenSystemCalculatorOptimized(droidController, optimizationOptions);
        } catch (DroidControllerCreationException e) {
            throw new OxygenSystemException("Error creating the Droid controller", e);
        }
    }

    private OptimizationOptions buildOptimizationOptions(Optimizations... optimizations) {
        final var optimizationOptionsBuilder = createOptimizationOptions();

        for (var optimization : optimizations) {
            switch (optimization) {
                case NO_GO_BACK_ALLOWED -> optimizationOptionsBuilder.withNotGoBackAllowed();
                case DISCARD_EXPLORED_PATHS -> optimizationOptionsBuilder.withDiscardExploredPaths();
            }
        }

        return optimizationOptionsBuilder.build();
    }

    public enum Optimizations {
        NO_GO_BACK_ALLOWED, DISCARD_EXPLORED_PATHS
    }
}
