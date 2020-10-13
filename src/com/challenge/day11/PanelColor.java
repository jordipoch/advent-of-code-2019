package com.challenge.day11;

public enum PanelColor {
    BLACK(0), WHITE(1);

    private int colorCode;

    PanelColor(int colorCode) {
        this.colorCode = colorCode;
    }

    public int getColorCode() {
        return colorCode;
    }

    public static PanelColor fromColorCode(int colorCode) {
        switch (colorCode) {
            case 0: return BLACK;
            case 1: return WHITE;
            default: throw new IllegalArgumentException("Invalid color code: " + colorCode);
        }
    }
}
