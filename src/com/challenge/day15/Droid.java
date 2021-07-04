package com.challenge.day15;

import com.challenge.day15.exception.DroidEngineException;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.IntCodeComputer.ExecutionResult;
import com.challenge.library.intcodecomputer.IntCodeLoader;
import com.challenge.library.intcodecomputer.exception.IntComputerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Optional;

import static com.challenge.library.intcodecomputer.IntCodeComputer.Builder.createNewIntCodeComputer;

public class Droid {
    private static final Logger logger = LogManager.getLogger();

    private final IntCodeComputer engine;

    private Droid(IntCodeComputer engine) {
        this.engine = engine;
    }

    public MovementResult move(DroidDirection movement) throws DroidEngineException {
        logger.traceEntry("Movement: {}", movement);

        engine.addInputValue(movement.getId());

        ExecutionResult executionResult = null;
        try {
            executionResult = engine.executeCode();
        } catch (IntComputerException e) {
            throw new DroidEngineException("Unexpected error running the droid engine", e);
        }

        return logger.traceExit(processExecutionResult(executionResult));
    }

    private MovementResult processExecutionResult(ExecutionResult executionResult) throws DroidEngineException {
        if (!executionResult.hasAnyOutput()) {
            throw new DroidEngineException("Expecting output from the droid engine");
        }
        Optional<MovementResult> optionalMovementResult = MovementResult.fromId(executionResult.getOutput().get(0).intValue());
        return optionalMovementResult.orElseThrow(() -> new DroidEngineException("Unexpected movement result from the droid engine"));
    }

    static class Builder {
        private IntCodeComputer engine;

        public static Builder createDroid() {
            return new Builder();
        }

        public Builder withEngine(IntCodeComputer engine) {
            this.engine = engine;
            return this;
        }

        public Builder withEngineCode(BigInteger[] engineCode) {
            engine = createNewIntCodeComputer(engineCode)
                    .withMemoryAutoExpand()
                    .withFeedbackLoopMode(true)
                    .build();

            return this;
        }

        public Builder withEngineCodeFile(Path basePath, String file) throws IOException {
            BigInteger[] engineCode = IntCodeLoader
                        .getInstance()
                        .loadBigIntCodeFromFile(basePath, file);
            return withEngineCode(engineCode);
        }

        public Droid build() {
            if (engine != null) {
                return new Droid(engine);
            } else {
                throw new IllegalStateException("Can't build the droid because: the engine has not been created");
            }
        }
    }
}
