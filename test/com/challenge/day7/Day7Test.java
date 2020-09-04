package com.challenge.day7;

import org.testng.annotations.Test;

import static com.challenge.day7.Day7.day7Part1;
import static com.challenge.day7.Day7.day7Part2;
import static org.testng.Assert.*;

public class Day7Test {
    @Test
    public void testDay7Part1() {
        assertEquals(day7Part1(), 567045);
    }

    @Test
    public void testDay7Part2() {
        assertEquals(day7Part2(), 39016654);
    }
}