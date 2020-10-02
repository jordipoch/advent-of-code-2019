package com.challenge.library.geometry.model;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class Int2DPointTest {

    @Test
    public void testNormalizedDistance() {
        calculateAndValidateNormalizedDistance(Int2DPoint.ORIGIN, new Int2DPoint(3, 3), 3);
        calculateAndValidateNormalizedDistance(new Int2DPoint(1, 1), new Int2DPoint(4, 2), 1);
        calculateAndValidateNormalizedDistance(new Int2DPoint(-1, 0), new Int2DPoint(5, 4), 2);
        calculateAndValidateNormalizedDistance(new Int2DPoint(2, 2), new Int2DPoint(2, 2), 0);
    }

    private void calculateAndValidateNormalizedDistance(Int2DPoint p1, Int2DPoint p2, int expected) {
        int normalizedDistance = p1.normalizedDistanceWith(p2);

        System.out.println(String.format("Normalized distance between %s and %s: %d", p1, p2, normalizedDistance));
        assertEquals(normalizedDistance, expected);
    }
}
