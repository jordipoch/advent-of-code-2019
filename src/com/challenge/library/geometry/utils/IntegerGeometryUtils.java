package com.challenge.library.geometry.utils;

import com.challenge.library.geometry.model.Int2DPoint;
import com.challenge.library.geometry.model.Int2DVector;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class IntegerGeometryUtils {
        public static Int2DPoint getAbsoluteDistance(Int2DPoint p1, Int2DPoint p2) {
            return new Int2DPoint(abs(p1.getX()-p2.getX()), abs(p1.getY()-p2.getY()));
        }

        public static List<Int2DPoint> calculateIntermediatePositionPoints(Int2DPoint p1, Int2DPoint p2, Int2DVector vIncrement) {
            List<Int2DPoint> intermediatePositionsList = new ArrayList<>();

            Int2DPoint position = p1;
            while (!position.equals(p2)) {
                position = position.add(vIncrement);
                if (!position.equals(p2)) {
                    intermediatePositionsList.add(position);
                }
            }

            return intermediatePositionsList;
        }
}
