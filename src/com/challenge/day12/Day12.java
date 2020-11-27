package com.challenge.day12;

import com.challenge.library.geometry.model.Int3DCoord;

import static com.challenge.day12.SaturnMoonSystem.Builder.createSaturnMoonSystem;
import static com.challenge.day12.SaturnMoonSystem.LogConfiguration.createLogConfiguration;

public class Day12 {
    public static void main(String[] args) {
        runDay12Part1();
        runDay12Part2();
    }

    public static long runDay12Part1() {
        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystemBuilder()
                .withLogConfig(createLogConfiguration().everyNSteps(100))
                .build();

        saturnMoonSystem.simulateNSteps(1000);
        long totalEnergy = saturnMoonSystem.calculateTotalEnergy();

        System.out.println("Total energy: " + totalEnergy);

        return totalEnergy;
    }

    public static long runDay12Part2() {
        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystemBuilder()
                .withLogConfig(createLogConfiguration().everyNSteps(100_000))
                .build();

        long repeatingState = saturnMoonSystem.simulateAndFindRepeatingState(1_000_000);

        System.out.println("State repeated at step: " + repeatingState);

        return repeatingState;
    }


    private static SaturnMoonSystem.Builder createSaturnMoonSystemBuilder() {
        return createSaturnMoonSystem()
                .withMoon(Int3DCoord.of(-7, -8, 9), Moon.MoonName.GANYMEDE)
                .withMoon(Int3DCoord.of(-12, -3, -4), Moon.MoonName.CALLISTO)
                .withMoon(Int3DCoord.of(6, -17, -9), Moon.MoonName.EUROPA)
                .withMoon(Int3DCoord.of(4, -10, -6), Moon.MoonName.IO);
    }
}
