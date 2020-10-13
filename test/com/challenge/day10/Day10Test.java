package com.challenge.day10;

import com.challenge.day10.exception.Day10ExecutionException;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

import static org.testng.Assert.*;

public class Day10Test {

    @Test
    public void testRunDay10Part1() throws Day10ExecutionException {
        //assertEquals(Day10.runDay10Part1(), 329);
        assertThat(Day10.runDay10Part1()).as("Check result of Day 10 part 1...").isEqualTo(329);
    }

    @Test
    public void testRunDay10Part2() throws Day10ExecutionException {
        assertEquals(Day10.runDay10Part2(), 512);
    }
}