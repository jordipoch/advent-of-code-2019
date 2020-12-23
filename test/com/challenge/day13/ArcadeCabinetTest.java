package com.challenge.day13;

import com.challenge.day13.exception.ArcadeCabinetException;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult;
import static com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult.Builder.createExecutionResult;
import com.challenge.library.intcodecomputer.exception.IntComputerException;
import static com.challenge.day13.ArcadeCabinet.Builder.createArcadeCabinetDemo;

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

        when(mockedComputer.executeCode()).thenReturn(createExecutionResultFromOutputList(outputList));

        final ArcadeCabinet arcadeCabinet = createArcadeCabinetDemo()
                .withIntCodeComputer(mockedComputer)
                .build();

        return arcadeCabinet.runGame();
    }

    private ExecutionResult createExecutionResultFromOutputList(List<BigInteger> outputList) {
        ExecutionResult.Builder builder = createExecutionResult()
                .withResultType(ExecutionResult.ResultType.EXECUTION_FINISHED);

        for (BigInteger output: outputList) {
            builder.addOutputValue(output);
        }

        return builder.build();
    }
}