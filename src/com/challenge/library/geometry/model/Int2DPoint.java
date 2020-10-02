package com.challenge.library.geometry.model;

import java.util.Objects;

public class Int2DPoint {
    public static Int2DPoint ORIGIN = new Int2DPoint(0, 0);

    int x, y;

    public Int2DPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Int2DPoint add(Int2DVector v) {
        return new Int2DPoint(x + v.getX(), y + v.getY());
    }

    public int normalizedDistanceWith(Int2DPoint p) {
        if (this.equals(p)) {
            return 0;
        }

        Int2DVector v = new Int2DVector(this, p);
        return (int) Math.round(v.getLength() / v.normalize().getLength());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Int2DPoint that = (Int2DPoint) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
