package com.challenge.day11;

import com.challenge.library.geometry.model.Int2DPoint;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HullPanelGridTest {

    @Test
    public void testPaintPanel_notPainted() {
        HullPanelGrid hullPanelGrid = new HullPanelGrid();

        HullPanel hullPanel = hullPanelGrid.paintPanel(new Int2DPoint(0, 0), PanelColor.WHITE);
        System.out.println("Hull panel painted: " + hullPanel);

        assertThat(hullPanel).as("Check panel painted: " + hullPanel).isEqualTo(new HullPanel(new Int2DPoint(0, 0), PanelColor.WHITE));
    }

    @Test
    public void testPaintPanel_repaint() {
        HullPanelGrid hullPanelGrid = new HullPanelGrid();

        hullPanelGrid.paintPanel(new Int2DPoint(0, 0), PanelColor.WHITE);
        HullPanel hullPanel00 = hullPanelGrid.paintPanel(new Int2DPoint(0, 0), PanelColor.BLACK);
        HullPanel hullPanel10 = hullPanelGrid.paintPanel(new Int2DPoint(1, 0), PanelColor.WHITE);

        System.out.println("Panell at postion (0, 0): " + hullPanel00);
        System.out.println("Panell at postion (1, 0): " + hullPanel10);

        assertThat(hullPanel00).as("Check panel at position (0, 0): " + hullPanel00).isEqualTo(new HullPanel(new Int2DPoint(0, 0), PanelColor.BLACK));
    }

    @Test
    public void testGetNumPaintedPanels() {
        HullPanelGrid hullPanelGrid = new HullPanelGrid();

        hullPanelGrid.paintPanel(new Int2DPoint(0, 0), PanelColor.WHITE);
        hullPanelGrid.paintPanel(new Int2DPoint(1, 0), PanelColor.BLACK);
        hullPanelGrid.paintPanel(new Int2DPoint(1, 1), PanelColor.WHITE);
        hullPanelGrid.paintPanel(new Int2DPoint(0, 1), PanelColor.BLACK);
        hullPanelGrid.paintPanel(new Int2DPoint(0, 0), PanelColor.BLACK);
        hullPanelGrid.paintPanel(new Int2DPoint(1, 0), PanelColor.WHITE);
        hullPanelGrid.paintPanel(new Int2DPoint(1, -1), PanelColor.WHITE);

        int numPaintedPanels = hullPanelGrid.getNumPaintedPanels();
        System.out.println("Num painted panels " + numPaintedPanels);

        assertThat(numPaintedPanels).as("Check the number of panels painted").isEqualTo(5);
    }

    @Test
    public void testGetColorAtNonPaintedPosition() {
        HullPanelGrid hullPanelGrid = new HullPanelGrid();
        Int2DPoint paintedPanelPosition = Int2DPoint.ORIGIN;
        Int2DPoint consultedPanelPosition = new Int2DPoint(1, 0);

        hullPanelGrid.paintPanel(paintedPanelPosition, PanelColor.WHITE);

        PanelColor panelColor = hullPanelGrid.getColorAtPosition(consultedPanelPosition);
        System.out.printf("Color at position %s: %s", consultedPanelPosition, panelColor);

        assertThat(panelColor).as("Checking color at position %s...", consultedPanelPosition).isEqualTo(PanelColor.BLACK);
    }

    @Test
    public void testGetColorAtPaintedPosition() {
        HullPanelGrid hullPanelGrid = new HullPanelGrid();
        Int2DPoint paintedPanelPosition = Int2DPoint.ORIGIN;
        Int2DPoint consultedPanelPosition = Int2DPoint.ORIGIN;

        hullPanelGrid.paintPanel(paintedPanelPosition, PanelColor.WHITE);

        PanelColor panelColor = hullPanelGrid.getColorAtPosition(consultedPanelPosition);
        System.out.printf("Color at position %s: %s", consultedPanelPosition, panelColor);

        assertThat(panelColor).as("Checking color at position %s...", consultedPanelPosition).isEqualTo(PanelColor.WHITE);
    }
}