package com.challenge.day13;

import com.challenge.day13.exception.WrongTileException;
import com.challenge.library.geometry.model.Int2DPoint;

public class Tile {
    Int2DPoint position;
    TileType tileType;

    public static Tile of(int x, int y, int tileId) throws WrongTileException {
        if (TileType.fromId(tileId).isPresent()) {
            return Tile.of(x, y, TileType.fromId(tileId).get());
        } else {
            throw new WrongTileException(String.format("Wrong tile Id (%d)", tileId));
        }
    }

    public static Tile of (int x, int y, TileType tileType) {
        return new Tile(new Int2DPoint(x, y), tileType);
    }

    public static Tile of (Int2DPoint tilePos, TileType tileType) {
        return new Tile(tilePos, tileType);
    }

    private Tile(Int2DPoint position, TileType tileType) {
        this.position = position;
        this.tileType = tileType;
    }

    public Int2DPoint getPosition() {
        return position;
    }

    public TileType getTileType() {
        return tileType;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", position, tileType);
    }
}
