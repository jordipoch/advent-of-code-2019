package com.challenge.day11;

import com.challenge.day11.exception.HullPaintingRobotException;
import com.challenge.library.intcodecomputer.IntCodeLoader;
import static com.challenge.day11.HullPaintingRobot.Builder.createHullPaintingRobot;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;

public class Day11 {
    public static void main(String[] args) {
        try {
            runDay11Part1();
        } catch (Exception e) {
            System.out.println("Error running day 11 challenge: " + e);
            e.printStackTrace();
        }
    }

    static int runDay11Part1() throws IOException, HullPaintingRobotException {
        BigInteger[] code = IntCodeLoader
                .getInstance()
                .loadBigIntCodeFromFile(Paths.get("resources", "com", "challenge", "day11"), "input.txt");

        System.out.println("Running day 11 challenge, part 1...");

        HullPaintingRobot hullPaintingRobot = createHullPaintingRobot(code).build();
        int numPanelsPainted = hullPaintingRobot.paintSpaceShipHull();

        System.out.println("Number of panels painted: " + numPanelsPainted);

        return numPanelsPainted;
    }
}
