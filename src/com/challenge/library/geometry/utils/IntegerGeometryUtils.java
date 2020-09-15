package com.challenge.library.geometry.utils;

import com.challenge.library.geometry.model.Int2DCoord;

import static java.lang.Math.abs;

public class IntegerGeometryUtils {
        public static Int2DCoord getAbsoluteDistance(Int2DCoord p1, Int2DCoord p2) {
            return new Int2DCoord(abs(p1.getX()-p2.getX()), abs(p1.getY()-p2.getY()));
        }
}
