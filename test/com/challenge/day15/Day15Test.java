package com.challenge.day15;

import com.challenge.day15.exception.OxygenSystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import static com.challenge.day15.Day15.runDay15Part1;
import static org.assertj.core.api.Assertions.assertThat;

public class Day15Test {
    private static final Logger logger = LogManager.getLogger();

    @Test(groups = {"heavy_tests"})
    public void testPart1() throws OxygenSystemException {
        logger.info("Performing test...");

        final var expectedDistance = 380L;

        var calculationResult = runDay15Part1();
        var actualDistance = calculationResult.getMinDistanceToOxygen();

        logger.debug("Distance = {}", actualDistance);
        assertThat(actualDistance).as("Checking min distance to oxygen...").isEqualTo(expectedDistance);
        logger.info("Test OK");
    }
}