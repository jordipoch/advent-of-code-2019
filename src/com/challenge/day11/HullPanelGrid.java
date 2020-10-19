package com.challenge.day11;

import com.challenge.library.geometry.model.Int2DPoint;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.math.NumberUtils.min;
import static org.apache.commons.lang3.math.NumberUtils.max;

public class HullPanelGrid {
    Map<Int2DPoint, HullPanel> visitedPanelsMap = new HashMap<>();

    public PanelColor getColorAtPosition(Int2DPoint position) {
        HullPanel hullPanelAtPos = visitedPanelsMap.get(position);

        return hullPanelAtPos != null ? hullPanelAtPos.getColor() : PanelColor.BLACK;
    }

    public HullPanel paintPanel(Int2DPoint position, PanelColor color) {
        HullPanel hullPanel = visitedPanelsMap.get(position);
        if (hullPanel == null) {
            hullPanel = new HullPanel(position, color);
            visitedPanelsMap.put(position, hullPanel);
        } else {
            hullPanel.setColor(color);
        }

        return hullPanel;
    }

    public int getNumPaintedPanels() {
        return visitedPanelsMap.values().size();
    }

    public String getPrintedRegistrationIdentifier() {

        return buildRegistrationIdentifier(buildHullPanelArray());
    }

    private HullPanel[][] buildHullPanelArray() {
        int minX = 0, maxX = 0, minY = 0, maxY = 0;
        for(Int2DPoint p : visitedPanelsMap.keySet()) {
            minX = min(minX, p.getX());
            minY = min(minY, p.getY());

            maxX = max(maxX, p.getX());
            maxY = max(maxY, p.getY());
        }

        int arrayWidth = maxX - minX + 1;
        int arrayHeight = maxY - minY + 1;
        int xOffset = -minX;
        int yOffset = -minY;

        HullPanel[][] hullPanelArray = new HullPanel[arrayHeight][arrayWidth];

        for(Int2DPoint p : visitedPanelsMap.keySet()) {
            hullPanelArray[p.getY() + yOffset][p.getX() + xOffset] = visitedPanelsMap.get(p);
        }

        return hullPanelArray;
    }

    private String buildRegistrationIdentifier(HullPanel[][] hullPanelArray) {
        final char painted = '#';
        final char nonPainted = '.';

        StringBuilder stringBuilder = new StringBuilder();

        for (HullPanel[] hullPanelRow : hullPanelArray) {
            for (HullPanel hullPanel : hullPanelRow) {
                if (hullPanel == null || hullPanel.getColor() == PanelColor.BLACK) {
                    stringBuilder.append(nonPainted);
                } else {
                    stringBuilder.append(painted);
                }
            }
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }
}
