package com.challenge.library.geometry.model.mutable;

import java.util.Objects;

public class Int3DCoord implements Cloneable {
    private int x;
    private int y;
    private int z;

    private static Int3DCoord ORIGIN = of(0, 0, 0);

    public static Int3DCoord of(int x, int y, int z) {
        return new Int3DCoord(x, y, z);
    }
    public static Int3DCoord getOrigin() { return new Int3DCoord(ORIGIN); }

    Int3DCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Int3DCoord(Int3DCoord other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
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

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public void setZ(int z) { this.z = z; }

    public void addToX(int x) { this.x += x; }

    public void addToY(int y) { this.y += y; }

    public void addToZ(int z) { this.z += z; }

    public Int3DCoord add(Int3DCoord other) {
        x += other.getX();
        y += other.getY();
        z += other.getZ();

        return this;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
