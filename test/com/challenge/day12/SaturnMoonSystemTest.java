package com.challenge.day12;

import static com.challenge.day12.SaturnMoonSystem.Builder.createSaturnMoonSystem;

import com.challenge.library.geometry.model.Int3DCoord;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SaturnMoonSystemTest {

    @Test
    public void test1CalculateTotalEnergy() {
        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystem()
            .withMoon(Int3DCoord.of(-1, 0, 2), Moon.MoonName.GANYMEDE)
            .withMoon(Int3DCoord.of(2, -10, -7), Moon.MoonName.CALLISTO)
            .withMoon(Int3DCoord.of(4, -8, 8), Moon.MoonName.EUROPA)
            .withMoon(Int3DCoord.of(3, 5, -1), Moon.MoonName.IO)
            .withDebugInfo(1)
            .build();

        saturnMoonSystem.simulateNSteps(10);
        long totalEnergy = saturnMoonSystem.calculateTotalEnergy();

        System.out.println("Total energy: " + totalEnergy);
        assertThat(totalEnergy).as("Checking the Saturn moon system total energy").isEqualTo(179);
    }

    @Test
    public void test2CalculateTotalEnergy() {
        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystem()
                .withMoon(Int3DCoord.of(-8, -10, 0), Moon.MoonName.GANYMEDE)
                .withMoon(Int3DCoord.of(5, 5, 10), Moon.MoonName.CALLISTO)
                .withMoon(Int3DCoord.of(2, -7, 3), Moon.MoonName.EUROPA)
                .withMoon(Int3DCoord.of(9, -8, -3), Moon.MoonName.IO)
                .withDebugInfo(10)
                .build();

        saturnMoonSystem.simulateNSteps(100);
        long totalEnergy = saturnMoonSystem.calculateTotalEnergy();

        System.out.println("Total energy: " + totalEnergy);
        assertThat(totalEnergy).as("Checking the Saturn moon system total energy").isEqualTo(1940);
    }
}