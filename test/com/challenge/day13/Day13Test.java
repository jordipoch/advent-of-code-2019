package com.challenge.day13;

import static com.challenge.day13.Day13.runDay13Part1;

import com.challenge.day13.exception.ArcadeCabinetException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import static com.challenge.day13.Day13.runDay13Part2;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;


public class Day13Test {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void testRunDay13Part1() throws ArcadeCabinetException, IOException {
        long numBlocks = runDay13Part1();

        assertThat(numBlocks).as("Checking number of blocks...").isEqualTo(306);

        logger.info("Number of blocks: {}", numBlocks);
    }

    @Test
    public void testRunDay13Part2() throws ArcadeCabinetException, IOException {
        long score = runDay13Part2();

        assertThat(score).as("Checking score after breaking all the blocks...").isEqualTo(15_328L);

        logger.info("Score: {}", score);
    }
}