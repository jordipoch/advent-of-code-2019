package com.challenge.day11;

import com.challenge.day11.exception.HullPaintingRobotException;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class Day11Test {

    @Test
    public void testRunDay11Part1() throws IOException, HullPaintingRobotException {
        int numPaintedPanels = Day11.runDay11Part1();

        assertThat(numPaintedPanels).as("Checking the number of painted panels...").isEqualTo(2276);
    }

    @Test
    public void testRunDay11Part2() throws IOException, HullPaintingRobotException {
        String registrationIdentifier = Day11.runDay11Part2();

        // CBLPJZCU
        String expected = String.format(
                "..##..###..#....###....##.####..##..#..#...%n" +
                ".#..#.#..#.#....#..#....#....#.#..#.#..#...%n" +
                ".#....###..#....#..#....#...#..#....#..#...%n" +
                ".#....#..#.#....###.....#..#...#....#..#...%n" +
                ".#..#.#..#.#....#....#..#.#....#..#.#..#...%n" +
                "..##..###..####.#.....##..####..##...##....%n"
        );

        assertThat(registrationIdentifier).as("Checking registration identified...").isEqualTo(expected);
    }
}