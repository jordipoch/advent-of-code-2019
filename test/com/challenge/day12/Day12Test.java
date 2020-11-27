package com.challenge.day12;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Day12Test {

    @Test
    public void testRunDay12Part1() {
        long totalEnergy = Day12.runDay12Part1();

        assertThat(totalEnergy).as("Checking the Saturn moon system total energy").isEqualTo(12_773);
    }

    @Test(groups = {"heavy_tests"})
    public void testRunDay12Part2() {
        long repeatingState = Day12.runDay12Part2();

        assertThat(repeatingState).as("Checking the Saturn moon system repeating state").isEqualTo(306_798_770_391_636L);
    }
}