package com.challenge.day11;

import java.util.Map;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toMap;

public enum Turn {
    LEFT(0), RIGHT(1);

    private int turnCode;

    Turn(int turnCode) {
        this.turnCode = turnCode;
    }

    public int getTurnCode() {
        return turnCode;
    }

    private static Map<Integer, Turn> turnCodeToEnumMap = Stream.of(values()).collect(toMap(Turn::getTurnCode, t -> t));

    public static Turn fromTurnCode(int turnCode) {
        Turn turn = turnCodeToEnumMap.get(turnCode);

        if (turn == null)
            throw new IllegalArgumentException("Invalid turn code:" + turnCode);

        return turn;
    }
}
