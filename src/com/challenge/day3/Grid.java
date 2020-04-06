package com.challenge.day3;

import java.util.List;

public class Grid {

    GridPosition[][] positions;
    int width, height;
    int xPos, yPos;
    int cpXPos, cpYPos;
    boolean wireCross;
    int xPosClosestWireCross, yPosClosestWireCross;
    int minMD;

    int wire1Length = 0, wire2Length = 0;
    int xPosMinDistanceWireCross, yPosMinDistanceWireCross;
    int minSummedWireLength = Integer.MAX_VALUE;

    public Grid(GridDimension dimension) {
        width = dimension.getxSize();
        height = dimension.getySize();

        wireCross = false;

        positions = new GridPosition[width][height];

        cpXPos = dimension.getxCentral();
        cpYPos = dimension.getyCentral();

        minMD = width + height;

        resetPositionToCentralPort();
    }

    public void resetPositionToCentralPort() {
        xPos = cpXPos;
        yPos = cpYPos;
    }

    public WireMovementResult applyMovement(Movement mov, WireNum wireNum) {
        for (int i = 0; i < mov.getDistance(); i++) {
            updateGridXYPos(mov.getDirection());
            GridPosition gridPosition = getGridPosition();
            boolean cross = gridPosition.applyStep(mov.getDirection(), wireNum, this);
            wireCross |= cross;
            if (cross) {
                updateMinDistanceToWireCross();
                updateMinLengthToWireCross(gridPosition.getWiresCombinedDistance());
            }
        }

        return new WireMovementResult(
                xPos, yPos,
                wireCross,
                xPosClosestWireCross, yPosClosestWireCross,
                xPosMinDistanceWireCross, yPosMinDistanceWireCross,
                minMD, minSummedWireLength);
    }

    private void updateGridXYPos(Movement.Direction direction) {
        switch (direction) {
            case UP:
                yPos--;
                break;
            case DOWN:
                yPos++;
                break;
            case LEFT:
                xPos--;
                break;
            case RIGHT:
                xPos++;
                break;
        }
    }

    private GridPosition getGridPosition() {
        GridPosition gridPosition = positions[xPos][yPos];
        if (gridPosition == null) {
            gridPosition = new GridPosition();
            positions[xPos][yPos] = gridPosition;
        }


        return gridPosition;
    }

    private void updateMinDistanceToWireCross() {
        int md = calculateManhattanDistance(cpXPos, cpYPos, xPos, yPos);
        if (md < minMD) {
            minMD = md;
            xPosClosestWireCross = xPos;
            yPosClosestWireCross = yPos;
        }
    }

    private void updateMinLengthToWireCross(int wireLength) {
        if (wireLength < minSummedWireLength) {
            minSummedWireLength = wireLength;
            xPosMinDistanceWireCross = xPos;
            yPosMinDistanceWireCross = yPos;
        }
    }

    public int increaseWire1Length() {
        return ++wire1Length;
    }

    public int increaseWire2Length() {
        return ++wire2Length;
    }

    private static int calculateManhattanDistance(int x1, int y1, int x2, int y2) {
        return max(x1, x2) - min(x1, x2) + max(y1, y2) - min(y1, y2);
    }

    private static int min(int a, int b) {
        return a < b ? a : b;
    }
    private static int max(int a, int b) {
        return a > b ? a : b;
    }

    @Override
    public String toString() {
        return String.format("(0,0)->(%d, %d)%n CentralPort->(%d, %d)", width-1, height-1, xPos, yPos);
    }

    public static class GridPosition {
        private boolean wire1 = false;
        private boolean wire2 = false;

        private int distanceWire1 = 0;
        private int distanceWire2 = 0;

        private char printableChar = '.';

        public boolean applyStep(Movement.Direction direction, WireNum wireNum, Grid grid) {
            wireNum.applyPosition(this, grid);
            boolean wireCross = isWireCross();
            if(wireCross) {
                printableChar = '+';
            } else {
                printableChar = direction.getPrintableChar();
            }
            return wireCross;
        }

        private void visitPositionWire1(int wireLength) {
            wire1 = true;
            if (wireLength > distanceWire1)
                distanceWire1 = wireLength;
        }

        private void visitPositionWire2(int wireLength) {
            wire2 = true;
            if (wireLength > distanceWire2)
                distanceWire2 = wireLength;
        }

        public boolean isWireCross() {
            return wire1 && wire2;
        }

        public int getWiresCombinedDistance() {
            return distanceWire1 + distanceWire2;
        }

        public char getPrintableChar() {
            return printableChar;
        }

        @Override
        public String toString() {
            return String.format("{Wire 1: %b, Wire 2: %b}", wire1, wire2);
        }
    }

    public enum WireNum {
        WIRE1 {
            public void applyPosition (GridPosition position, Grid grid) { position.visitPositionWire1(grid.increaseWire1Length()); }
            },
        WIRE2 {
            public void applyPosition (GridPosition position, Grid grid) { position.visitPositionWire2(grid.increaseWire2Length()); }
        };

        public abstract void applyPosition(GridPosition pos, Grid grid);
    }

    public static class WireMovementResult {
        int headXPos, headYPos;
        boolean cross;
        int closestCrossXPos, closestCrossYPos;
        int minLengthCrossXPos, minLengthCrossYPos;
        int minMD;
        int minWireLength;

        public WireMovementResult(int headXPos, int headYPos, boolean cross,
                                  int closestCrossXPos, int closestCrossYPos,
                                  int minLengthCrossXPos, int minLengthCrossYPos,
                                  int minMD, int minLength) {
            this.headXPos = headXPos;
            this.headYPos = headYPos;
            this.cross = cross;
            this.closestCrossXPos = closestCrossXPos;
            this.closestCrossYPos = closestCrossYPos;
            this.minLengthCrossXPos = minLengthCrossXPos;
            this.minLengthCrossYPos = minLengthCrossYPos;
            this.minMD = minMD;
            this.minWireLength = minLength;
        }

        public int getMinMD() {
            return minMD;
        }

        public int getMinWireLength() {
            return minWireLength;
        }

        @Override
        public String toString() {
            String str = String.format("Wire head: (%d, %d)", headXPos, headYPos);
            if (cross) {
                str += String.format(". Closest cross: (%d, %d), distance to CP: %d | Min wire length cross (%d, %d), wire length: %d",
                        closestCrossXPos, closestCrossYPos, minMD,
                        minLengthCrossXPos, minLengthCrossYPos, minWireLength);
            } else {
                str += ". No cross.";
            }

            return str;
        }
    }

    public static class GridDimension {
        private int minX = 0, maxX = 0, minY = 0, maxY = 0;

        private int xSize, ySize, xCentral, yCentral;

        public GridDimension calculateDimension(List<Movement> wire) {
            int x = 0, y = 0;

            for (Movement m : wire) {
                switch (m.direction) {
                    case UP:
                        y -= m.getDistance();
                        minY = min(minY, y);
                        break;
                    case DOWN:
                        y += m.getDistance();
                        maxY = max(maxY, y);
                        break;
                    case LEFT:
                        x -= m.getDistance();
                        minX = min(minX, x);
                        break;
                    case RIGHT:
                        x += m.getDistance();
                        maxX = max(maxX, x);
                        break;
                }
            }

            return this;
        }

        public GridDimension toCanonicalDimension() {
            xSize = maxX - minX + 1;
            ySize = maxY - minY + 1;
            xCentral = -minX;
            yCentral = -minY;

            return this;
        }

        public int getxSize() {
            return xSize;
        }

        public int getySize() {
            return ySize;
        }

        public int getxCentral() {
            return xCentral;
        }

        public int getyCentral() {
            return yCentral;
        }
    }

}
