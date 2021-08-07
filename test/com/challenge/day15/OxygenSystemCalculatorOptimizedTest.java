package com.challenge.day15;

import com.challenge.day15.exception.OxygenSystemException;
import com.challenge.day15.mocks.DroidControllerTestFactory;
import com.challenge.day15.mocks.exception.DroidControllerMockCreationException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

import static com.challenge.day15.OxygenSystemCalculatorFactory.Optimizations.DISCARD_EXPLORED_PATHS;
import static com.challenge.day15.OxygenSystemCalculatorFactory.Optimizations.NO_GO_BACK_ALLOWED;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class OxygenSystemCalculatorOptimizedTest {
    @Test (dataProvider = "simpleTests")
    public void PerformSimpleTestsWithNoGoBackAllowedOptimization(String gridFileName, int expectedDistance, int maxDepth, int maxMovements) throws OxygenSystemException, DroidControllerMockCreationException {
        var result = performTest(gridFileName, maxDepth, maxMovements, NO_GO_BACK_ALLOWED);
        checkResultsFound(result, expectedDistance);
    }

    @Test (dataProvider = "simpleTests")
    public void PerformSimpleTestsWithNoGoBackAllowedAndDiscardExploredPathsOptimizations(String gridFileName, int expectedDistance, int maxDepth, int maxMovements) throws OxygenSystemException, DroidControllerMockCreationException {
        var result = performTest(gridFileName, maxDepth, maxMovements, NO_GO_BACK_ALLOWED, DISCARD_EXPLORED_PATHS);
        checkResultsFound(result, expectedDistance);
    }

    //    Oxygen system found at distance 6! (Num movements: 602).
    //    Oxygen system found at distance 15! (Num movements: 6180).
    @Test (dataProvider = "midComplexTests")
    public void PerformMidComplexTestsWithNoGoBackAllowedOptimization(String gridFileName, int expectedDistance, int maxDepth, int maxMovements) throws OxygenSystemException, DroidControllerMockCreationException {
        var result = performTest(gridFileName, maxDepth, maxMovements, NO_GO_BACK_ALLOWED);
        checkResultsFound(result, expectedDistance);
    }

    //    Oxygen system found at distance 6! (Num movements: 358).
    //    Oxygen system found at distance 15! (Num movements: 1278).
    @Test (dataProvider = "midComplexTests")
    public void PerformMidComplexTestsWithNoGoBackAllowedAndDiscardExploredPathsOptimizations(String gridFileName, int expectedDistance, int maxDepth, int maxMovements) throws OxygenSystemException, DroidControllerMockCreationException {
        var result = performTest(gridFileName, maxDepth, maxMovements, NO_GO_BACK_ALLOWED, DISCARD_EXPLORED_PATHS);
        checkResultsFound(result, expectedDistance);
    }

    @Test
    public void testOxygenNotFoundMaxDepth() throws OxygenSystemException, DroidControllerMockCreationException {
        var maxDepth = 4;
        var result = performTest("MidComplexGridMap.txt", maxDepth, 1000, NO_GO_BACK_ALLOWED);
        checkResultsNotFoundMaxDepth(result, maxDepth);
    }

    @Test
    public void checkResultsNotFoundAllSpaceExplored() throws OxygenSystemException, DroidControllerMockCreationException {
        var result = performTest("SimpleGridMapNoOxygen.txt", 4, 500, NO_GO_BACK_ALLOWED);
        checkResultsNotFoundAllSpaceExplored(result);
    }

    @Test
    public void testExploreAllSpace() throws DroidControllerMockCreationException, OxygenSystemException {
        var droidControllerMock = DroidControllerTestFactory.getInstance().createDroidController("SimpleGridMap3.txt");
        var oxygenSystemCalculator = OxygenSystemCalculatorFactory.getInstance().createOptimizedCalculator(droidControllerMock, NO_GO_BACK_ALLOWED, DISCARD_EXPLORED_PATHS);

        CalculationResult result = oxygenSystemCalculator.calculateMinDistanceToOxygen(15, 1000, false);
        System.out.println(result);

        assertTrue(result.isFound());
        assertEquals(result.getMinDistanceToOxygen(), 4);
        assertEquals(result.getMaxDistanceExplored(), 6);
        assertEquals(result.getOxygenPosition(), new Int2DPoint(-2, 0));
    }

    private CalculationResult performTest(String gridFileName, int maxDepth, int maxMovements, OxygenSystemCalculatorFactory.Optimizations... optimizations) throws OxygenSystemException, DroidControllerMockCreationException {
        var droidControllerMock = DroidControllerTestFactory.getInstance().createDroidController(gridFileName);
        var oxygenSystemCalculator = OxygenSystemCalculatorFactory.getInstance().createOptimizedCalculator(droidControllerMock, optimizations);

        CalculationResult result = oxygenSystemCalculator.calculateMinDistanceToOxygen(maxDepth, maxMovements);
        System.out.println(result);

        return result;
    }

    private void checkResultsFound(CalculationResult result, int expectedDistance) {
        assertTrue(result.isFound());
        assertEquals(result.getMinDistanceToOxygen(), expectedDistance);
    }

    private void checkResultsNotFoundMaxDepth(CalculationResult result, int maxDepth) {
        assertFalse(result.isFound());
        assertEquals(result.getNotFoundCause(), CalculationResult.NotFoundCause.MAX_DEPTH_EXCEEDED);
        assertEquals(result.getMaxDepth(), maxDepth);
    }

    private void checkResultsNotFoundAllSpaceExplored(CalculationResult result) {
        assertFalse(result.isFound());
        assertEquals(result.getNotFoundCause(), CalculationResult.NotFoundCause.ALL_SPACE_EXPLORED);
    }

    @DataProvider(name = "simpleTests")
    private Iterator<Object[]> createSimpleTests() {
        return Arrays.asList(
                new Object[] {"SimpleGridMap.txt", 1, 2, 1000},
                new Object[] {"SimpleGridMap2.txt", 6, 6, 1000}
        ).iterator();
    }

    @DataProvider(name = "midComplexTests")
    private Iterator<Object[]> createMidComplexTests() {
        return Arrays.asList(
                new Object[] {"MidComplexGridMap.txt", 6, 10, 1000},
                new Object[] {"MidComplexGridMap2.txt", 15, 20, 10000}
        ).iterator();
    }
}