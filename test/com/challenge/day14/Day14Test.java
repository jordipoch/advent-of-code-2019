package com.challenge.day14;

import com.challenge.day14.exception.NanofactoryException;
import com.challenge.day14.exception.ReactionReaderException;
import static com.challenge.day14.Day14.runDay14Part1;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class Day14Test {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void testPart1() throws ReactionReaderException, NanofactoryException {
        logger.info("Performing test...");

        final var cost = runDay14Part1();
        final var expectedCost = 870_051L;

        logger.debug("Cost = {}", cost);
        assertThat(cost).as("Checking calculated cost...").isEqualTo(expectedCost);
        logger.info("Test OK");
    }
}
