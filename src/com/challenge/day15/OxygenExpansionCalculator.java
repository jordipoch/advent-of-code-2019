package com.challenge.day15;

import com.challenge.day15.exception.OxygenExpansionCalculatorException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.aqpache.logging.log4j.Logger;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

public class OxygenExpansionCalculator {
    private static final Logger logger = LogManager.getLogger();

    private static OxygenExpansionCalculator THE_INSTANCE = new OxygenExpansionCalculator();

    public static OxygenExpansionCalculator getInstance() {
        return THE_INSTANCE;
    }

    public int calculateNumMinutesToFillWithOxygen(Grid gridMap) throws OxygenExpansionCalculatorException {

        var initialOxygenPosition = gridMap.getOxygenPosition()
                .orElseThrow(() -> new OxygenExpansionCalculatorException("Oxygen not found in the map"));
        var minutes = 0;

        Queue<PositionTime> expandableOxygenPositions = new PriorityQueue<>();
        expandableOxygenPositions.offer(PositionTime.of(initialOxygenPosition, minutes));

        while ( !expandableOxygenPositions.isEmpty() ) {
            var currentPositionTime = expandableOxygenPositions.peek();
            final var time = currentPositionTime.getTime();
            if (time > minutes) {
                minutes++;

                logger.debug("AFTER {} MINUTES:", minutes);
                logger.debug("Grid map:{}{}", System.lineSeparator(), gridMap);
                logger.debug("Queue:{}", expandableOxygenPositions);
            }

            expandableOxygenPositions.remove();

            var adjacentPositions = gridMap.getAdjacentEmptyPositions(currentPositionTime.getPosition());
            for (var adjacentPosition : adjacentPositions) {
                gridMap.putCell(CellType.OXYGEN, adjacentPosition);
                expandableOxygenPositions.offer(PositionTime.of(adjacentPosition, time + 1));
            }
        }

        return minutes;
    }

    private static class PositionTime implements Comparable<PositionTime> {
        private Int2DPoint position;
        private int time;

        private PositionTime(Int2DPoint position, int time) {
            this.position = position;
            this.time = time;
        }

        private static PositionTime of(Int2DPoint position, int time) {
            return new PositionTime(position, time);
        }

        public Int2DPoint getPosition() {
            return position;
        }

        public int getTime() {
            return time;
        }

        @Override
        public int compareTo(PositionTime that) {
            if (that == null) {
                throw new IllegalArgumentException("The object to compare cannot be null");
            }

            return time - that.time;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PositionTime that = (PositionTime) o;
            return time == that.time;
        }

        @Override
        public int hashCode() {
            return Objects.hash(time);
        }

        @Override
        public String toString() {
            return "{" + position +
                    ", " + time +
                    '}';
        }
    }
}
