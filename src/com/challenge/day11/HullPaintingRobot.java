package com.challenge.day11;

import com.challenge.day11.exception.HullPaintingRobotException;
import com.challenge.library.geometry.model.Int2DPoint;
import com.challenge.library.intcodecomputer.IntCodeComputer;
import com.challenge.library.intcodecomputer.exception.IntComputerException;

import static com.challenge.day11.PanelColor.fromColorCode;
import static com.challenge.day11.Turn.fromTurnCode;
import static com.challenge.library.intcodecomputer.IntCodeComputer.Builder.createNewIntCodeComputer;

import java.math.BigInteger;
import java.util.List;

public class HullPaintingRobot {
    private final HullPanelGrid hullPanelGrid = new HullPanelGrid();
    private Int2DPoint position = new Int2DPoint(0, 0);
    private Direction direction = Direction.UP;
    private final IntCodeComputer intCodeComputer;

    private HullPaintingRobot(IntCodeComputer intCodeComputer) {
        this.intCodeComputer = intCodeComputer;
    }

    public static HullPaintingRobot createFromIntCodeComputer(IntCodeComputer intCodeComputer) {
        return new HullPaintingRobot(intCodeComputer);
    }

    public int paintSpaceShipHull() throws HullPaintingRobotException {
        try {

            boolean paintingFinished = false;
            while (!paintingFinished) {
                intCodeComputer.addInputValue(hullPanelGrid.getColorAtPosition(position).getColorCode());

                List<BigInteger> executionResult = intCodeComputer.executeCode();
                if (!executionResult.isEmpty()) {
                    PanelColor panelColor = fromColorCode(executionResult.get(0).intValue());
                    hullPanelGrid.paintPanel(position, panelColor);
                    executionResult = intCodeComputer.executeCode();
                    if (executionResult.isEmpty()) {
                        throw new HullPaintingRobotException("Expecting an output value from Int Code Computer execution.");
                    }
                    Turn turn = fromTurnCode(executionResult.get(0).intValue());
                    direction = direction.turn(turn);
                    position = position.add(direction.getDirectionVector());
                } else {
                    paintingFinished = true;
                }
            }


        } catch (IntComputerException e) {
            throw new HullPaintingRobotException("An error has occurred executing the Int Code Machine", e);
        }

        return hullPanelGrid.getNumPaintedPanels();
    }

    public static class Builder {
        IntCodeComputer.Builder intCodeComputerBuilder;

        private Builder(long[] robotCode) {
            intCodeComputerBuilder = createNewIntCodeComputer(robotCode);
        }

        private Builder(BigInteger[] robotCode) {
            intCodeComputerBuilder = createNewIntCodeComputer(robotCode);
        }

        public static Builder createHullPaintingRobot(long[] robotCode) {
            return new Builder(robotCode);
        }

        public static Builder createHullPaintingRobot(BigInteger[] robotCode) {
            return new Builder(robotCode);
        }

        public HullPaintingRobot build() {
            IntCodeComputer intCodeComputer = intCodeComputerBuilder
                    .withFeedbackLoopMode(true)
                    .withMemoryAutoExpand()
                    .build();
            return new HullPaintingRobot(intCodeComputer);
        }
    }
}
