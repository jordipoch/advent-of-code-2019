package com.challenge.day13;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Optional.ofNullable;

public enum Tile {
    EMPTY(0, "E"), WALL(1, "W"), BLOCK(2, "B"), HORIZ_PADDLE(3, "H"), BALL(4, "B");

    private int id;
    private String abbreviation;

    Tile(int id, String abbreviation) {
        this.id = id;
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        return abbreviation;
    }

    private static Map<Integer, Tile> idToTileMap = Stream.of(values()). collect(Collectors.toMap(tile -> tile.id, tile -> tile));

    public static Optional<Tile> fromId(int id) {
        return ofNullable(idToTileMap.get(id));
    }
}
