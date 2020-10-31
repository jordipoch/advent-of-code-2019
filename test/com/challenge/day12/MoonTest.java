package com.challenge.day12;

import static com.challenge.day12.Moon.createMoon;

import com.challenge.library.geometry.model.Int3DCoord;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MoonTest {

    @Test
    public void testApplyGravity1() {
        Moon moon1 = createMoon(Int3DCoord.of(5, 4, -1), Moon.MoonName.CALLISTO);
        Moon moon2 = createMoon(Int3DCoord.of(3, 10, -1), Moon.MoonName.EUROPA);
        assertEquals(moon1.calculateVelocityDelta(moon2), Int3DCoord.of(-1, 1, 0));
    }

    @Test
    public void testApplyGravity2() {
        Moon moon1 = createMoon(Int3DCoord.of(5, 4, -1), Moon.MoonName.CALLISTO);
        Moon moon2 = createMoon(Int3DCoord.of(5, 4, -1), Moon.MoonName.EUROPA);
        assertEquals(moon1.calculateVelocityDelta(moon2), Int3DCoord.ORIGIN);
    }

    @Test
    public void testApplyGravity3() {
        Moon moon1 = createMoon(Int3DCoord.of(-5, 14, 0), Moon.MoonName.CALLISTO);
        Moon moon2 = createMoon(Int3DCoord.of(-4, -14, 1), Moon.MoonName.CALLISTO);
        assertEquals(moon1.calculateVelocityDelta(moon2), Int3DCoord.of(1, -1, 1));
    }

    @Test
    public void test1CalculateTotalEnergy() {
        calculateAndCheckTotalEnergy(Int3DCoord.of(2, 1, -3), Int3DCoord.of(-3, -2, 1), 36);
    }

    @Test
    public void test2CalculateTotalEnergy() {
        calculateAndCheckTotalEnergy(Int3DCoord.of(1, -8, 0), Int3DCoord.of(1, 1, 3), 45);
    }

    @Test
    public void test3CalculateTotalEnergy() {
        calculateAndCheckTotalEnergy(Int3DCoord.of(3, -6, 1), Int3DCoord.of(3, 2, -3), 80);
    }

    @Test
    public void test4CalculateTotalEnergy() {
        calculateAndCheckTotalEnergy(Int3DCoord.of(2, 0, 4), Int3DCoord.of(1, -1, -1), 18);
    }

    private void calculateAndCheckTotalEnergy(Int3DCoord pos, Int3DCoord vel, long expected) {
        Moon moon = createMoon(pos, vel);

        long totalEnergy = moon.calculateTotalEnergy();

        System.out.println("Moon: " + moon);
        System.out.println("Total energy: " + totalEnergy);

        assertEquals(totalEnergy, expected);
    }
}