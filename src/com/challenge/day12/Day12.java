package com.challenge.day12;

import com.challenge.library.geometry.model.Int3DCoord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.challenge.day12.SaturnMoonSystem.Builder.createSaturnMoonSystem;
import static com.challenge.day12.SaturnMoonSystem.LogConfiguration.createLogConfiguration;

public class Day12 {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        runDay12Part1();
    }

    public static long runDay12Part1() {
        logger.info("Starting execution");

        SaturnMoonSystem saturnMoonSystem = createSaturnMoonSystem()
                .withMoon(Int3DCoord.of(-7, -8, 9), Moon.MoonName.GANYMEDE)
                .withMoon(Int3DCoord.of(-12, -3, -4), Moon.MoonName.CALLISTO)
                .withMoon(Int3DCoord.of(6, -17, -9), Moon.MoonName.EUROPA)
                .withMoon(Int3DCoord.of(4, -10, -6), Moon.MoonName.IO)
                .withLogConfig(createLogConfiguration().everyNSteps(100))
                .build();

        saturnMoonSystem.simulateNSteps(1000);
        long totalEnergy = saturnMoonSystem.calculateTotalEnergy();

        System.out.println("Total energy: " + totalEnergy);

        logger.info("Execution finished");

        return totalEnergy;
    }
}
