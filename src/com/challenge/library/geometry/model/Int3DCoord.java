package com.challenge.library.geometry.model;

import java.util.Objects;

public class Int3DCoord {
    private int x;
    private int y;
    private int z;

    public static Int3DCoord ORIGIN = of(0, 0, 0);

    Int3DCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Int3DCoord of(int x, int y, int z) {
        return new Int3DCoord(x, y, z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Int3DCoord add(Int3DCoord coord) {
        return Int3DCoord.of(
                x + coord.getX(),
                y + coord.getY(),
                z + coord.getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Int3DCoord)) return false;
        Int3DCoord that = (Int3DCoord) o;
        return x == that.x &&
                y == that.y &&
                z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d)", x, y, z);
    }
}
