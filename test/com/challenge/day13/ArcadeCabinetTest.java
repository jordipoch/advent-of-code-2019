package com.challenge.day13;

import com.challenge.day13.exception.ArcadeCabinetException;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.exception.IntComputerException;
import static com.challenge.day13.ArcadeCabinet.Builder.createArcadeCabinet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

public class ArcadeCabinetTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void testRunGame_simpleExecution() throws IntComputerException, ArcadeCabinetException {
        testRunGame(new int[] {0, 0, 1, 1, 0, 2}, 1);
    }

    @Test
    public void testRunGame_MoreComplexExecution() throws IntComputerException, ArcadeCabinetException {
        testRunGame(new int[] { 1, 0, 0,
                                0, 0, 1,
                                0, 2, 4,
                                1, 1, 2,
                                0, 1, 3,
                                2, 1, 1,
                                1, 0, 2,
                                2, 1, 2,
                                1, 0, 0
        },                      2);
    }

    @Test(expectedExceptions = {ArcadeCabinetException.class})
    public void testRunGame_emptyOutputValues() throws IntComputerException, ArcadeCabinetException {
        testRunGameWithException(new int[] {});
    }

    @Test(expectedExceptions = {ArcadeCabinetException.class})
    public void testRunGame_outputWithWrongNumberOfValues() throws IntComputerException, ArcadeCabinetException {
        testRunGameWithException(new int[] {1, 2, 0, 2});
    }

    @Test(expectedExceptions = {ArcadeCabinetException.class})
    public void testRunGame_outputInvalidTileId() throws IntComputerException, ArcadeCabinetException {
        testRunGameWithException(new int[] {1, 2, 5});
    }

    private void testRunGame(int[] outputValues, long expectedNumBlocks) throws IntComputerException, ArcadeCabinetException {
        long numBlocks = createArcadeCabinetAndRunGame((outputValues));

        assertThat(numBlocks).as("Checking number of blocks...").isEqualTo(expectedNumBlocks);

        logger.info("Number of block tiles = {}", numBlocks);
    }

    private void testRunGameWithException(int[] outputValues) throws IntComputerException, ArcadeCabinetException {
        try {
            createArcadeCabinetAndRunGame((outputValues));
        } catch (ArcadeCabinetException e) {
            logger.info("Got exception {}", e.toString());
            throw e;
        }

    }

    private long createArcadeCabinetAndRunGame(int... values) throws IntComputerException, ArcadeCabinetException {
        logger.info("Int code computer output: {}", Arrays.toString(values));

        List<BigInteger> outputList = IntStream.of(values).mapToObj(BigInteger::valueOf).collect(Collectors.toList());

        IntCodeComputer mockedComputer = mock(IntCodeComputer.class);

        when(mockedComputer.executeCode()).thenReturn(outputList);

        final ArcadeCabinet arcadeCabinet = createArcadeCabinet()
                .withIntCodeComputer(mockedComputer)
                .build();

        return arcadeCabinet.runGame();
    }
}