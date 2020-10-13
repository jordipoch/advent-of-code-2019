package com.challenge.day11;

import com.challenge.library.geometry.model.Int2DPoint;

import java.util.HashMap;
import java.util.Map;

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
}
