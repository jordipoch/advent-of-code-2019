package com.challenge.day15;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum CellType {
    WALL('#') {
        @Override
        public MovementResult getRelatedMovementResult() {
            return MovementResult.WALL;
        }
    }, EMPTY('.') {
        @Override
        public MovementResult getRelatedMovementResult() {
            return MovementResult.MOVED;
        }
    }, OXYGEN('O') {
        @Override
        public MovementResult getRelatedMovementResult() {
            return MovementResult.OXYGEN_SYSTEM;
        }
    }, UNKNOWN('?') {
        @Override
        public MovementResult getRelatedMovementResult() {
            throw new IllegalStateException("No related movement result exists for UNKNOWN(?) cell type");
        }
    }, EXPLORED('E') {
        @Override
        public MovementResult getRelatedMovementResult() {
            throw new IllegalStateException("No related movement result exists for EXPLORED(E) cell type");
        }
    }, INITIAL('I') {
        @Override
        public MovementResult getRelatedMovementResult() {
            throw new IllegalStateException("No related movement result exists for INITIAL(I) cell type");
        }
    };

    private final char charCode;
    private static final Map<Character, CellType> charCodeToEnum = Stream.of(values()).collect(toMap(CellType::getCharCode, Function.identity()));

    public char getCharCode() {
        return charCode;
    }

    CellType(char charCode) {
        this.charCode = charCode;
    }

    public abstract MovementResult getRelatedMovementResult();

    public static Optional<CellType> fromCharCode(char charCode) {
        return Optional.ofNullable(charCodeToEnum.get(charCode));
    }
}
