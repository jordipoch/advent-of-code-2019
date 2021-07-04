package com.challenge.day15;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum MovementResult {
    WALL(0, CellType.WALL), MOVED(1, CellType.EMPTY), OXYGEN_SYSTEM(2, CellType.OXYGEN);

    private final int id;
    private final CellType cellType;
    private static final Map<Integer, MovementResult> idToEnum = Stream.of(values()).collect(toMap(MovementResult::getId, Function.identity()));


    MovementResult(int id, CellType cellType) {
        this.id = id;
        this.cellType = cellType;
    }

    public int getId() {
        return id;
    }

    public CellType getCellType() {
        return cellType;
    }

    public static Optional<MovementResult> fromId(Integer id) {
        return Optional.ofNullable(idToEnum.get(id));
    }
}
