package com.challenge.day6;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class PathToOriginTest {
    @Test
    public void testGetDistanceToObject() {
        PathToOrigin pathToOrigin = new PathToOrigin();

        pathToOrigin.addObject("A");
        pathToOrigin.addObject("B");
        pathToOrigin.addObject("C");
        pathToOrigin.addObject("D");
        pathToOrigin.addObject("E");

        assertEquals(pathToOrigin.getDistanceToObject("D"), 3);
    }
}
