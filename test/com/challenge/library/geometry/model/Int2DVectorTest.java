package com.challenge.library.geometry.model;

import static com.challenge.library.utils.NumberUtils.format;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Int2DVectorTest {

    @Test
    public void testNormalize() {
        calculateAndValidateNormalization(new Int2DVector(3, 0), new Int2DVector(1, 0));
        calculateAndValidateNormalization(new Int2DVector(0, 5), new Int2DVector(0, 1));
        calculateAndValidateNormalization(new Int2DVector(6, 3), new Int2DVector(2, 1));
        calculateAndValidateNormalization(new Int2DVector(-8, -4), new Int2DVector(-2, -1));
        calculateAndValidateNormalization(Int2DVector.ZERO, Int2DVector.ZERO);
    }

    @Test
    public void testAngleWith() {
        final Int2DVector vectorUp = new Int2DVector(0, 1);

        calculateAndValidateAngle(vectorUp, new Int2DVector(0, 2), "0");
        calculateAndValidateAngle(vectorUp, new Int2DVector(1, 1), "0.7854");
        calculateAndValidateAngle(vectorUp, new Int2DVector(1, 0), "1.5708");
        calculateAndValidateAngle(vectorUp, new Int2DVector(0, -1), "3.1416");
        calculateAndValidateAngle(vectorUp, new Int2DVector(-1, -3), "3.4633");
        calculateAndValidateAngle(vectorUp, new Int2DVector(-1, 0), "4.7124");
        calculateAndValidateAngle(vectorUp, new Int2DVector(-1, 1), "5.4978");
        calculateAndValidateAngle(vectorUp, new Int2DVector(-1, 4), "6.0382");
    }

    private void calculateAndValidateNormalization(Int2DVector v, Int2DVector expected) {
        Int2DVector normalized = v.normalize();

        System.out.println(String.format("Normalize %s: %s", v, normalized));

        assertEquals(normalized, expected);
    }

    private void calculateAndValidateAngle(Int2DVector v1, Int2DVector v2, String expected) {
        final String strAngle = format(v1.angleWith(v2));

        System.out.println(String.format("Angle between %s and %s: %s", v1, v2, strAngle));

        assertEquals(strAngle, expected);
    }
}