package com.challenge.day13;

import com.challenge.day13.exception.ArcadeCabinetException;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.exception.IntComputerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.challenge.day13.Screen.createScreen;
import static com.challenge.library.intcodecomputer.IntCodeComputer.Builder.createNewIntCodeComputer;

import java.math.BigInteger;
import java.util.List;

public class ArcadeCabinet {
    private static final Logger logger = LogManager.getLogger();

    private final IntCodeComputer intCodeComputer;

    private ArcadeCabinet(IntCodeComputer intCodeComputer) {
        this.intCodeComputer = intCodeComputer;
    }

    public long runGame() throws ArcadeCabinetException {
        logger.traceEntry();

        try {
            List<BigInteger> executionOutput = intCodeComputer.executeCode();
            logger.debug("Int code computer execution output: {}", executionOutput);
            final Screen screen = createArcadeScreenFromExecutionOutput(executionOutput);
            return logger.traceExit(screen.countNumTilesOfType(Tile.BLOCK));
        } catch (IntComputerException e){
            throw new ArcadeCabinetException("Fatal error while executing the game code", e);
        }
    }

    private Screen createArcadeScreenFromExecutionOutput(List<BigInteger> executionOutput) throws ArcadeCabinetException {
        logger.traceEntry();

        checkExecutionOutput(executionOutput);

        final Screen screen = createScreen();

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


    public static class Builder {
        private IntCodeComputer intCodeComputer;
        private IntCodeComputer.Builder intCodeComputerBuilder;

        public static Builder createArcadeCabinet() {
            return new Builder();
        }

        public Builder withIntCodeComputer(IntCodeComputer intCodeComputer) {
            this.intCodeComputer = intCodeComputer;

            return this;
        }

        public Builder withGameCode(BigInteger[] gameCode) {
            intCodeComputerBuilder = createNewIntCodeComputer(gameCode)
                    .withFeedbackLoopMode(false)
                    .withMemoryAutoExpand();

            return this;
        }

        public ArcadeCabinet build() {
            return new ArcadeCabinet(getIntCodeComputer());
        }

        private IntCodeComputer getIntCodeComputer() {
            if (intCodeComputer != null) {
                return intCodeComputer;
            } else {
                if (intCodeComputerBuilder == null) {
                    throw new IllegalStateException("Expecting a call to \"withGameCode\"");
                }

                return intCodeComputerBuilder.build();
            }
        }
    }
}
