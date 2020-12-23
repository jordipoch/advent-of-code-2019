package com.challenge.day13;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Optional.ofNullable;

public enum TileType {
    EMPTY(0, "E"), WALL(1, "W"), BLOCK(2, "B"), HORIZ_PADDLE(3, "P"), BALL(4, "L");

    private int id;
    private String abbreviation;

    TileType(int id, String abbreviation) {
        this.id = id;
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getId() {
        return id;
    }

    private static Map<Integer, TileType> idToTileMap = Stream.of(values()). collect(Collectors.toMap(tileType -> tileType.id, tileType -> tileType));

    public static Optional<TileType> fromId(int id) {
        return ofNullable(idToTileMap.get(id));
    }
}
