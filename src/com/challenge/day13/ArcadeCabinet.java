package com.challenge.day13;

import com.challenge.library.intcodecomputer.IntCodeComputer;
import static com.challenge.day13.GameController.Builder.createGameController;
import static com.challenge.day13.ArcadeCabinet.ConfigurationInfo.Builder.createConfigurationInfo;

import com.challenge.day13.exception.ArcadeCabinetBuilderException;
import com.challenge.day13.exception.ArcadeCabinetException;

import java.math.BigInteger;

import static com.challenge.library.intcodecomputer.IntCodeComputer.Builder.createNewIntCodeComputer;

public interface ArcadeCabinet {

    long runGame() throws ArcadeCabinetException;

    class Builder {
        private final boolean isDemo;
        private IntCodeComputer intCodeComputer;
        private GameController.Builder gameControllerBuilder;
        private final ConfigurationInfo.Builder configInfoBuilder = createConfigurationInfo();

        private Builder(boolean isDemo)
        {
            this.isDemo = isDemo;
        }

        public static ArcadeCabinet.Builder createArcadeCabinetDemo() {
            return new ArcadeCabinet.Builder(true);
        }

        public static ArcadeCabinet.Builder createArcadeCabinetFullGame() {
            return new ArcadeCabinet.Builder(false);
        }

        public ArcadeCabinet.Builder withIntCodeComputer(IntCodeComputer intCodeComputer) {
            this.intCodeComputer = intCodeComputer;
            createGameControllerIfNeeded();
            return this;
        }

        public ArcadeCabinet.Builder withGameCode(BigInteger[] gameCode) {
            intCodeComputer = createNewIntCodeComputer(gameCode)
                    .withMemoryAutoExpand()
                    .withFeedbackLoopMode(!isDemo)
                    .withAskForInputMode(!isDemo)
                    .build();

            createGameControllerIfNeeded();

            return this;
        }

        public ArcadeCabinet.Builder withScreenSize(int width, int height) {
            gameControllerBuilder.withScreenSize(width, height);

            return this;
        }

        public ArcadeCabinet.Builder withLoggingEnabled(int everyNMoves) {
            configInfoBuilder.withLoggingEnabled(everyNMoves);
            return this;
        }

        public ArcadeCabinet build() {
            if (isDemo) {
                return buildArcadeCabinetDemo();
            } else {
                return buildArcadeCabinetFullGame();
            }
        }

        private void createGameControllerIfNeeded() {
            if (!isDemo) {
                gameControllerBuilder = createGameController(intCodeComputer);
            }
        }

        private ArcadeCabinetDemo buildArcadeCabinetDemo() {
            if (intCodeComputer != null) {
                return new ArcadeCabinetDemo(intCodeComputer);
            } else {
                throw new ArcadeCabinetBuilderException("can't build an arcade cabinet demo: the int computer is not defined");
            }
        }

        private ArcadeCabinetFullGame buildArcadeCabinetFullGame() {
            if (gameControllerBuilder != null) {
                return new ArcadeCabinetFullGame(gameControllerBuilder.build(), configInfoBuilder.build());
            } else {
                throw new ArcadeCabinetBuilderException("can't build an arcade cabinet full game: the int computer is not defined");
            }
        }
    }

    class ConfigurationInfo {
        private final boolean logEnabled;
        private final int everyNMoves;

        private ConfigurationInfo(boolean logEnabled, int everyNMoves) {
            this.logEnabled = logEnabled;
            this.everyNMoves = everyNMoves;
        }

        public boolean isLogEnabled() {
            return logEnabled;
        }

        public int getEveryNMoves() {
            return everyNMoves;
        }

        public static class Builder {
            private boolean logEnabled;
            private int everyNMoves;

            private Builder() {}

            public static Builder createConfigurationInfo() {
                return new Builder();
            }

            public Builder withLoggingEnabled(int everyNMoves) {
                this.logEnabled = true;
                this.everyNMoves = everyNMoves;

                return this;
            }

            public ConfigurationInfo build() {
                return new ConfigurationInfo(logEnabled, everyNMoves);
            }
        }
    }
}
