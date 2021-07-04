package com.challenge.day15;

import com.challenge.day15.exception.OxygenSystemException;
import com.challenge.day15.mocks.DroidControllerTestFactory;
import com.challenge.day15.mocks.exception.DroidControllerMockCreationException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.testng.Assert.*;

public class OxygenSystemCalculatorBruteForceTest {
    @Test (dataProvider = "simpleTests")
    public void PerformSimpleTests(String gridFileName, int expectedDistance, int maxDepth, int maxMovements) throws OxygenSystemException, DroidControllerMockCreationException {
        var result = performTest(gridFileName, maxDepth, maxMovements);
        checkResultsFound(result, expectedDistance);
    }

    @Test
    public void testOxygenNotFoundMaxDepth() throws OxygenSystemException, DroidControllerMockCreationException {
        var maxDepth = 4;
        var result = performTest("MidComplexGridMap.txt", maxDepth, 1000);
        checkResultsNotFoundMaxDepth(result, maxDepth);
    }

    @Test
    public void testOxygenNotFoundMaxMovements() throws OxygenSystemException, DroidControllerMockCreationException {
        var maxMovements = 1000;
        var result = performTest("MidComplexGridMap.txt", 6, maxMovements);
        checkResultsNotFoundMaxNumMovementsReached(result, maxMovements);
    }

    @Test
    public void checkResultsNotFoundAllSpaceExplored() throws OxygenSystemException, DroidControllerMockCreationException {
        var result = performTest("SimpleGridMapNoOxygen.txt", 4, 500);
        checkResultsNotFoundAllSpaceExplored(result);
    }

    private CalculationResult performTest(String gridFileName, int maxDepth, int maxMovements) throws OxygenSystemException, DroidControllerMockCreationException {
        var droidControllerMock = DroidControllerTestFactory.getInstance().createDroidController(gridFileName);
        var oxygenSystemCalculator = OxygenSystemCalculatorFactory.getInstance().createBruteForceCalculator(droidControllerMock);

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

    private void checkResultsNotFoundMaxNumMovementsReached(CalculationResult result, int maxMovements) {
        assertFalse(result.isFound());
        assertEquals(result.getNotFoundCause(), CalculationResult.NotFoundCause.MAX_MOVEMENTS_REACHED);
        assertEquals(result.getMaxMovements(), maxMovements);
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