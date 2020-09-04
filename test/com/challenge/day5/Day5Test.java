package com.challenge.day5;

import org.testng.annotations.Test;

import static com.challenge.day5.Day5.getIntComputerCode;
import static com.challenge.day5.Day5.runDay5;
import static org.testng.Assert.*;

public class Day5Test {
    @Test
    public void testDay5Part1() {
        assertEquals(runDay5(getIntComputerCode(), 1L, "Day 5, part 1"), 15314507);
    }

    @Test
    public void testDay5Part2() {
        assertEquals(runDay5(getIntComputerCode(), 5L, "Day 5, part 2"), 652726);
    }
}