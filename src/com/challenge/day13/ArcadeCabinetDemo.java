package com.challenge.day13;

import com.challenge.day13.exception.ArcadeCabinetException;
import com.challenge.day13.exception.ScreenException;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult;
import com.challenge.library.intcodecomputer.exception.IntComputerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.List;

public class ArcadeCabinetDemo implements ArcadeCabinet {
    private static final Logger logger = LogManager.getLogger();

    private final IntCodeComputer intCodeComputer;

    protected ArcadeCabinetDemo(IntCodeComputer intCodeComputer) {
        this.intCodeComputer = intCodeComputer;
    }

    public long runGame() throws ArcadeCabinetException {
        logger.traceEntry();
        try {
             ExecutionResult executionResult = intCodeComputer.executeCode();
            List<BigInteger> executionOutput = executionResult.getOutput();
            logger.debug("Int code computer execution output: {}", executionOutput);
            final Screen screen = createArcadeScreenFromExecutionOutput(executionOutput);
            return logger.traceExit(screen.countNumTilesOfType(TileType.BLOCK));
        } catch (IntComputerException e){
            throw new ArcadeCabinetException("Fatal error while executing the game code", e);
        }
    }

    private Screen createArcadeScreenFromExecutionOutput(List<BigInteger> executionOutput) throws ArcadeCabinetException {
        logger.traceEntry();

        checkExecutionOutput(executionOutput);

        final Screen screen = Screen.Builder.createScreen().build();

        try {
            for (int i = 0; i < executionOutput.size(); i += 3) {
                screen.putTile(executionOutput.get(i).intValue(),
                        executionOutput.get(i + 1).intValue(),
                        executionOutput.get(i + 2).intValue());
            }
        } catch (ScreenException e) {
            throw new ArcadeCabinetException("Error adding tiles to the screen", e);
        }

        return logger.traceExit(screen);
    }

    private void checkExecutionOutput(List<BigInteger> executionOutput) throws ArcadeCabinetException {
        logger.traceEntry();

        if (executionOutput.isEmpty()) {
            throw new ArcadeCabinetException("The execution output from the IntCodeComputer contains no data");
        }

        if (executionOutput.size() % 3 != 0) {
            throw new ArcadeCabinetException(String.format("Wrong execution output from the IntCodeComputer %d: is not a multiple of 3", executionOutput.size()));
        }

        logger.traceExit();
    }
}
