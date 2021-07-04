package com.challenge.day15;

import com.challenge.day15.exception.DroidEngineException;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.exception.IntComputerException;
import static com.challenge.day15.Droid.Builder.createDroid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.testng.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DroidTest {
    private static final Logger logger = LogManager.getLogger();

    @Mock
    private IntCodeComputer intCodeComputer;

    private AutoCloseable autoCloseable;
    private Random random;
    private Droid droid;

    @BeforeMethod
    public void openMocks() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        random = new Random();
        droid = createDroid().withEngine(intCodeComputer).build();
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test (dataProvider = "testMoveData")
    public void testMove(MovementResult movementResult) throws DroidEngineException, IntComputerException {
        logger.info("Performing test...");

        stubEngineResult(movementResult);
        performMovementAndCheck(movementResult);

        logger.info("Test OK");
    }

    @Test (expectedExceptions = DroidEngineException.class,
            expectedExceptionsMessageRegExp = ".*Unexpected error.*")
    public void testMove_EngineError() throws IntComputerException, DroidEngineException {
        logger.info("Performing test...");

        when(intCodeComputer.executeCode()).thenThrow(new IntComputerException("An error with the engine"));

        try {
            droid.move(getRandomMovement());
        } catch (DroidEngineException e) {
            assertThat(e).as("Checking exception cause...").hasCauseInstanceOf(IntComputerException.class);
            logger.info("Exception cause: " + e.getCause());
            logger.info("Test OK");
            throw e;
        }
    }

    @Test (expectedExceptions = DroidEngineException.class,
    expectedExceptionsMessageRegExp = ".*Unexpected movement.*")
    public void testMove_InvalidMovementResult() throws IntComputerException, DroidEngineException {
        logger.info("Performing test...");

        stubEngineInvalidResult();
        try {
            performMovementWithoutChecking();
        } catch (DroidEngineException e) {
            assertThat(e).as("Checking exception has no cause...").hasNoCause();
            logger.info("Exception:" + e);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test (expectedExceptions = DroidEngineException.class,
    expectedExceptionsMessageRegExp = ".*Expecting output.*")
    public void testMove_NoMovementResult() throws IntComputerException, DroidEngineException {
        logger.info("Performing test...");

        stubEngineNoResult();
        try {
            performMovementWithoutChecking();
        } catch (DroidEngineException e) {
            assertThat(e).as("Checking exception has no cause...").hasNoCause();
            logger.info("Exception:" + e);
            logger.info("Test OK");
            throw e;
        }
    }

    @DataProvider(name = "testMoveData")
    private Iterator<Object> createTestMoveData() {
        return Arrays.<Object>asList(MovementResult.WALL, MovementResult.MOVED, MovementResult.OXYGEN_SYSTEM).iterator();
    }

    private void stubEngineResult(MovementResult result) throws IntComputerException {
        IntCodeComputer.ExecutionResult executionResult = IntCodeComputer.ExecutionResult.Builder.createExecutionResult()
                .withResultType(IntCodeComputer.ExecutionResult.ResultType.NEXT_OUTPUT)
                .addOutputValue(BigInteger.valueOf(result.getId()))
                .build();
        when(intCodeComputer.executeCode()).thenReturn(executionResult);
    }

    private void stubEngineInvalidResult() throws IntComputerException {
        IntCodeComputer.ExecutionResult executionResult = IntCodeComputer.ExecutionResult.Builder.createExecutionResult()
                .withResultType(IntCodeComputer.ExecutionResult.ResultType.NEXT_OUTPUT)
                .addOutputValue(BigInteger.valueOf(99))
                .build();
        when(intCodeComputer.executeCode()).thenReturn(executionResult);
    }

    private void stubEngineNoResult() throws IntComputerException {
        IntCodeComputer.ExecutionResult executionResult = IntCodeComputer.ExecutionResult.Builder.createExecutionResult()
                .withResultType(IntCodeComputer.ExecutionResult.ResultType.INPUT_NEEDED)
                .build();
        when(intCodeComputer.executeCode()).thenReturn(executionResult);
    }

    private void performMovementAndCheck(MovementResult expected) throws DroidEngineException {
        MovementResult actual = droid.move(getRandomMovement());
        assertEquals(actual, expected);
    }

    private void performMovementWithoutChecking() throws DroidEngineException {
        droid.move(getRandomMovement());
    }


    private DroidDirection getRandomMovement() {
        return DroidDirection.values()[random.nextInt(DroidDirection.getNumValues())];
    }
}