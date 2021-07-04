package com.challenge.day15;

import com.challenge.library.geometry.model.Int2DPoint;

import java.util.Arrays;
import java.util.List;

public enum DroidDirection {
    NORTH(1) {
        @Override
        public DroidDirection getReverseDirection() {
            return SOUTH;
        }

        @Override
        public Int2DPoint moveDirection(Int2DPoint currentPos) {
            return new Int2DPoint(currentPos.getX(), currentPos.getY() + 1);
        }

    }, SOUTH(2) {
        @Override
        public DroidDirection getReverseDirection() {
            return NORTH;
        }

        @Override
        public Int2DPoint moveDirection(Int2DPoint currentPos) {
            return new Int2DPoint(currentPos.getX(), currentPos.getY() - 1);
        }
    }, WEST(3) {
        @Override
        public DroidDirection getReverseDirection() {
            return EAST;
        }

        @Override
        public Int2DPoint moveDirection(Int2DPoint currentPos) {
            return new Int2DPoint(currentPos.getX()-1, currentPos.getY());
        }
    }, EAST(4) {
        @Override
        public DroidDirection getReverseDirection() {
            return WEST;
        }

        @Override
        public Int2DPoint moveDirection(Int2DPoint currentPos) {
            return new Int2DPoint(currentPos.getX()+1, currentPos.getY());
        }
    };

    private final int id;

    DroidDirection(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract DroidDirection getReverseDirection();

    public abstract Int2DPoint moveDirection(Int2DPoint currentPos);

    public static int getNumValues() {
        return values().length;
    }

    public static List<DroidDirection> getAllDirections() {
        return Arrays.asList(DroidDirection.values());
    }
}
