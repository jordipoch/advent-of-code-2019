package com.challenge.day15;

import com.challenge.day15.exception.GridCreationException;
import com.challenge.day15.exception.OxygenExpansionCalculatorException;
import com.challenge.day15.helper.GridCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class OxygenExpansionCalculatorTest {
    private static final Logger logger = LogManager.getLogger();

    @Test (dataProvider = "gridMapTestFiles")
    public void testCalculateNumMinutesToFillWithOxygen(String fileName, int expectedMinutes) throws GridCreationException, OxygenExpansionCalculatorException {
        logger.info("Performing test...");

        var grid = GridCreator.createExploredGridFromFile(fileName);
        var numMinutes = OxygenExpansionCalculator.getInstance().calculateNumMinutesToFillWithOxygen(grid);

        assertThat(numMinutes).as("Checking num minutes for oxygen expansion is what's expected...").isEqualTo(expectedMinutes);

        logger.info("Test OK");
    }

    @DataProvider(name = "gridMapTestFiles")
    private Iterator<Object[]> getGridMapTestFiles() {
        return Arrays.asList(new Object[][] {
                {"5x5ExploredGrid.txt", 4}
        }).iterator();
    }
}