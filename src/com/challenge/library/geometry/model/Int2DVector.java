package com.challenge.library.geometry.model;

import java.util.Objects;

import static com.challenge.library.utils.NumberUtils.mcd;

public class Int2DVector {
    public static final Int2DVector ZERO = new Int2DVector(0, 0);
    public static final Int2DVector UP = new Int2DVector(0, -1);

    int x, y;

    public Int2DVector(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Int2DVector(Int2DPoint p1, Int2DPoint p2) {
        this(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Int2DVector normalize() {
        if (this.equals(ZERO)) {
            return ZERO;
        }

        int xNorm = 0, yNorm = 0;
        if (x == 0 || y == 0) {
            if (x == 0) {
                yNorm = Integer.compare(y, 0);
            } else {
                xNorm = Integer.compare(x, 0);
            }
        } else {
            int mcd = mcd(x, y);
            xNorm = x / mcd;
            yNorm = y / mcd;
        }

        return new Int2DVector(xNorm, yNorm);
    }

    public double angleWith(Int2DVector v) {
        double angle = Math.acos(dotProductWith(v) / (this.getLength() * v.getLength()));
        if (v.getX() < x) angle = 2 * Math.PI - angle;

        return angle;
    }

    public double getLength() {
        return Math.sqrt(x*x + y*y);
    }

    private int dotProductWith(Int2DVector v) {
        return x*v.getX() + y*v.getY();
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Int2DVector that = (Int2DVector) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
