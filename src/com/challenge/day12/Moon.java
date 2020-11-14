package com.challenge.day12;

import com.challenge.library.geometry.model.Int3DCoord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Random;

import static com.challenge.library.utils.NumberUtils.sign;

import static java.lang.Math.abs;

public class Moon implements Cloneable {
    private static final Logger logger = LogManager.getLogger();

    private Int3DCoord velocity;
    private Int3DCoord position;
    private MoonName name;

    private Moon(Int3DCoord position, Int3DCoord velocity, MoonName name) {
        this.position = position;
        this.name = name;
        this.velocity = velocity;
    }

    public static Moon createMoon(Int3DCoord position, Int3DCoord velocity, MoonName name) {
        return new Moon(position, velocity, name);
    }

    public static Moon createMoon(Int3DCoord position, Int3DCoord velocity) {
        return new Moon(position, velocity, chooseRandomName());
    }

    public static Moon createMoon(Int3DCoord position, MoonName name) {
        return new Moon(position, Int3DCoord.ORIGIN, name);
    }

    public static Moon createMoon(Int3DCoord position) {
        return new Moon(position, Int3DCoord.ORIGIN, chooseRandomName());
    }



    public Int3DCoord getVelocity() {
        return velocity;
    }

    public Int3DCoord getPosition() {
        return position;
    }

    public MoonName getName() {
        return name;
    }

    public Int3DCoord calculateVelocityDelta(Moon otherMoon) {
        Int3DCoord otherPos = otherMoon.getPosition();
        return Int3DCoord.of(
                sign(otherPos.getX() - position.getX()),
                sign(otherPos.getY() - position.getY()),
                sign(otherPos.getZ() - position.getZ()));
    }

    public void applyVelocityChange(Int3DCoord v) {
        velocity = velocity.add(v);
        position = position.add(velocity);
    }

    public long calculateTotalEnergy() {
        long potentialEnergy = calculateEnergy(position);
        long kineticEnergy = calculateEnergy(velocity);

        return potentialEnergy * kineticEnergy;
    }

    private static long calculateEnergy(Int3DCoord p) {
        return abs((long) p.getX()) + abs(p.getY()) + abs(p.getZ());
    }

    private static MoonName chooseRandomName() {
        return MoonName.values()[new Random().nextInt(MoonName.values().length)];
    }

    @Override
    public String toString() {
        return String.format("{(%s) pos=%s, vel=%s}", name, position, velocity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Moon moon = (Moon) o;
        return velocity.equals(moon.velocity) &&
                position.equals(moon.position) &&
                name == moon.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(velocity, position, name);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public enum MoonName {
        IO, EUROPA, GANYMEDE, CALLISTO;
    }
}
