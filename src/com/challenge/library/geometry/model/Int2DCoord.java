package com.challenge.library.geometry.model;

import java.util.Objects;

public class Int2DCoord {
    int x, y;

    public Int2DCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Int2DCoord add(Int2DCoord vector) {
        return new Int2DCoord(x + vector.getX(), y + vector.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Int2DCoord that = (Int2DCoord) o;
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
