package com.challenge.day12;

import com.challenge.library.geometry.model.Int3DCoord;
import static com.challenge.day12.SaturnMoonSystem.Builder.createSaturnMoonSystem;

public class Day12 {
    public static void main(String[] args) {
        runDay12Part1();
    }

    public static long runDay12Part1() {
        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystem()
                .withMoon(Int3DCoord.of(-7, -8, 9), Moon.MoonName.GANYMEDE)
                .withMoon(Int3DCoord.of(-12, -3, -4), Moon.MoonName.CALLISTO)
                .withMoon(Int3DCoord.of(6, -17, -9), Moon.MoonName.EUROPA)
                .withMoon(Int3DCoord.of(4, -10, -6), Moon.MoonName.IO)
                .withDebugInfo(100)
                .build();

        saturnMoonSystem.simulateNSteps(1000);
        long totalEnergy = saturnMoonSystem.calculateTotalEnergy();

        System.out.println("Total energy: " + totalEnergy);

        return totalEnergy;
    }
}
