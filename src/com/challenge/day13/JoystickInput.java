package com.challenge.day13;

public enum JoystickInput {
    MOVE_LEFT(-1), MOVE_RIGHT(1), NO_MOVE(0);

    private final int id;

    JoystickInput(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
