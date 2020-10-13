package com.challenge.day11;

import com.challenge.library.geometry.model.Int2DPoint;
import com.sun.istack.internal.NotNull;

import java.util.Objects;

public class HullPanel {
    PanelColor color;
    Int2DPoint position;

    public HullPanel(Int2DPoint position, PanelColor color) {
        Objects.requireNonNull(position, "The panel position can't be null");
        Objects.requireNonNull(color, "The panel color can't be null");

        this.position = position;
        this.color = color;
    }

    public PanelColor getColor() {
        return color;
    }

    public void setColor(PanelColor color) {
        Objects.requireNonNull(color, "The panel color can't be null");

        this.color = color;
    }

    public Int2DPoint getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "{" +
                "C=" + color +
                ", P=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HullPanel hullPanel = (HullPanel) o;
        return color == hullPanel.color &&
                position.equals(hullPanel.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, position);
    }
}
