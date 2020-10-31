package com.challenge.day12;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Day12Test {

    @Test
    public void testRunDay12Part1() {
        long totalEnergy = Day12.runDay12Part1();

        assertThat(totalEnergy).as("Checking the Saturn moon system total energy").isEqualTo(12773);
    }
}