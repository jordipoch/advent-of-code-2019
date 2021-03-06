package com.challenge.day12;

import static com.challenge.day12.SaturnMoonSystem.Builder.createSaturnMoonSystem;
import static com.challenge.day12.SaturnMoonSystem.LogConfiguration.createLogConfiguration;
import static org.assertj.core.api.Assertions.*;

import com.challenge.library.geometry.model.Int3DCoord;
import org.testng.annotations.Test;

public class SaturnMoonSystemTest {

    @Test
    public void test1CalculateTotalEnergy() {
        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystemBuilderForTest1()
                .withLogConfig(createLogConfiguration().everyNSteps(1))
                .build();

        saturnMoonSystem.simulateNSteps(10);
        long totalEnergy = saturnMoonSystem.calculateTotalEnergy();

        System.out.println("Total energy: " + totalEnergy);
        assertThat(totalEnergy).as("Checking the Saturn moon system total energy").isEqualTo(179);
    }

    @Test
    public void test2CalculateTotalEnergy() {
        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystemBuilderForTest2()
            .withLogConfig(createLogConfiguration().everyNSteps(10))
            .build();

        saturnMoonSystem.simulateNSteps(100);
        long totalEnergy = saturnMoonSystem.calculateTotalEnergy();

        System.out.println("Total energy: " + totalEnergy);
        assertThat(totalEnergy).as("Checking the Saturn moon system total energy").isEqualTo(1940);
    }

    @Test
    public void test1SimulateAndFindRepeatingState() {
        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystemBuilderForTest1()
            .withLogConfig(createLogConfiguration().everyNSteps(100))
            .build();

        long result = saturnMoonSystem.simulateAndFindRepeatingState(5000);

        if (result >= 0) {
            System.out.println("Repeating state found: " + result);
            assertThat(result).as("Checking the repeating state...").isEqualTo(2772);
        } else {
            fail("No repeating state found!");
        }
    }

    @Test
    public void test2SimulateAndFindRepeatingState() {
        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystemBuilderForTest2()
                .withLogConfig(createLogConfiguration().everyNSteps(100_00))
                .build();

        long result = saturnMoonSystem.simulateAndFindRepeatingState(1_000_000L);

        if (result >= 0) {
            System.out.println("Repeating state found: " + result);
            assertThat(result).as("Checking the repeating state...").isEqualTo(4_686_774_924L);
        } else {
            fail("No repeating state found!");
        }
    }

    private SaturnMoonSystem.Builder createSaturnMoonSystemBuilderForTest1() {
        return createSaturnMoonSystem()
            .withMoon(Int3DCoord.of(-1, 0, 2), Moon.MoonName.GANYMEDE)
            .withMoon(Int3DCoord.of(2, -10, -7), Moon.MoonName.CALLISTO)
            .withMoon(Int3DCoord.of(4, -8, 8), Moon.MoonName.EUROPA)
            .withMoon(Int3DCoord.of(3, 5, -1), Moon.MoonName.IO);
    }

    private SaturnMoonSystem.Builder createSaturnMoonSystemBuilderForTest2() {
        return createSaturnMoonSystem()
            .withMoon(Int3DCoord.of(-8, -10, 0), Moon.MoonName.GANYMEDE)
            .withMoon(Int3DCoord.of(5, 5, 10), Moon.MoonName.CALLISTO)
            .withMoon(Int3DCoord.of(2, -7, 3), Moon.MoonName.EUROPA)
            .withMoon(Int3DCoord.of(9, -8, -3), Moon.MoonName.IO);
    }
}