package com.challenge.day3;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

class Movement {
    Direction direction;
    int distance;

    Movement(String strMov) {
        this.direction = Direction.fromString(strMov.substring(0, 1)).get();
        this.distance = Integer.parseInt(strMov.substring(1));
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return String.format("%s%d", direction.toString(), distance);
    }

    enum Direction {
        UP("U", '|'), DOWN("D", '|'), LEFT("L", '-'), RIGHT("R", '-');

        String dir;
        char printableChar;

        Direction(String dir, char printableChar) {
            this.dir = dir;
            this.printableChar = printableChar;
        }

        @Override
        public String toString() {
            return dir;
        }

        public char getPrintableChar() {
            return printableChar;
        }

        private static final Map<String, Direction> stringToEnum = Stream.of(values()).collect(toMap(Object::toString, e -> e));

        public static Optional<Direction> fromString(String dir) {
            return Optional.ofNullable(stringToEnum.get(dir));
        }
    }
}