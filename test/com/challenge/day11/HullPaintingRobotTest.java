package com.challenge.day11;

import static com.challenge.day11.HullPaintingRobot.createFromIntCodeComputer;

import com.challenge.day11.exception.HullPaintingRobotException;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult;
import static com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult.Builder.createExecutionResult;

import com.challenge.library.intcodecomputer.exception.IntComputerException;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HullPaintingRobotTest {

    @Test
    public void testSimplePaintingWithMockedComputer() throws IntComputerException, HullPaintingRobotException {

        IntCodeComputer mockedComputer = mock(IntCodeComputer.class);
        when(mockedComputer.executeCode())
                .thenReturn(createOutputExecutionResult(ONE)).thenReturn(createOutputExecutionResult(ZERO))
                .thenReturn(createOutputExecutionResult(ZERO)).thenReturn(createOutputExecutionResult(ZERO))
                .thenReturn(createOutputExecutionResult(ONE)).thenReturn(createOutputExecutionResult(ZERO))
                .thenReturn(createOutputExecutionResult(ONE)).thenReturn(createOutputExecutionResult(ZERO))
                .thenReturn(createOutputExecutionResult(ZERO)).thenReturn(createOutputExecutionResult(ONE))
                .thenReturn(createOutputExecutionResult(ONE)).thenReturn(createOutputExecutionResult(ZERO))
                .thenReturn(createOutputExecutionResult(ONE)).thenReturn(createOutputExecutionResult(ZERO))
                .thenReturn(createExecutionFinishedResult());

        HullPaintingRobot hullPaintingRobot = createFromIntCodeComputer(mockedComputer);
        int nPaintedPanels = hullPaintingRobot.paintSpaceShipHull();

        System.out.println("Number of painted panels: " + nPaintedPanels);

        assertThat(nPaintedPanels).as("Checking number of painted panels").isEqualTo(6);
    }

    @Test(expectedExceptions = {HullPaintingRobotException.class})
    public void testExceptionOddOutputValues() throws IntComputerException, HullPaintingRobotException {

        IntCodeComputer mockedComputer = mock(IntCodeComputer.class);
        when(mockedComputer.executeCode())
                .thenReturn(createOutputExecutionResult(ONE)).thenReturn(createOutputExecutionResult(ZERO))
                .thenReturn(createOutputExecutionResult(ZERO))
                .thenReturn(createExecutionFinishedResult());

        HullPaintingRobot hullPaintingRobot = createFromIntCodeComputer(mockedComputer);

        try {
            hullPaintingRobot.paintSpaceShipHull();
        } catch (HullPaintingRobotException e) {
            System.out.println("Expecting exception: " + e);
            assertThat(e).as("checking exception message...").hasMessage("Expecting an output value from Int Code Computer execution.");
            assertThat(e).as("checking exception has no cause...").hasNoCause();
            throw e;
        }
    }

    @Test
    public void testIntCodeComputerException() throws IntComputerException {

        IntCodeComputer mockedComputer = mock(IntCodeComputer.class);
        when(mockedComputer.executeCode()).thenThrow(new IntComputerException("Exception", new Throwable()));

        HullPaintingRobot hullPaintingRobot = createFromIntCodeComputer(mockedComputer);

        try {
            hullPaintingRobot.paintSpaceShipHull();
        } catch (HullPaintingRobotException e) {
            System.out.println("Expecting exception: " + e);
            assertThat(e).as("checking exception message...").hasMessage("An error has occurred executing the Int Code Machine");
            assertThat(e).as("Checking exception cause...").hasCauseExactlyInstanceOf(IntComputerException.class);
        }
    }

    private ExecutionResult createOutputExecutionResult(BigInteger value) {
        return createExecutionResult()
            .withResultType(ExecutionResult.ResultType.NEXT_OUTPUT)
            .addOutputValue(value)
            .build();
    }

    private ExecutionResult createExecutionFinishedResult() {
        return createExecutionResult()
                .withResultType(ExecutionResult.ResultType.EXECUTION_FINISHED)
                .build();
    }

}