package com.challenge.day13;

import com.challenge.day13.exception.ScreenException;
import com.challenge.day13.exception.WrongTileException;
import com.challenge.library.geometry.model.Int2DPoint;

import static com.challenge.day13.TileChangeDetail.Builder.createTileChangeDetail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Screen {
    private static final Logger logger = LogManager.getLogger();

    Map<Int2DPoint, TileType> tiles = new HashMap<>();
    TileType[][] tileArray;
    int width;
    int height;

    public Screen() {
    }

    public Screen(TileType[][] tileArray, int width, int height) {
        this.tileArray = tileArray;
        this.width = width;
        this.height = height;
    }

    public void putTile(int x, int y, int tileId) throws ScreenException {
        logger.traceEntry("Params: x={}, y={}, tileId={}", x, y, tileId);

        try {
            logger.traceExit(putTile(Tile.of(x, y, tileId)));
        } catch (WrongTileException e) {
            throw new ScreenException("Error creating tile", e);
        }
    }

    public TileChangeDetail putTile(Tile tile) throws ScreenException {
        logger.traceEntry("Params: tile={}", tile);

        int x = tile.getPosition().getX();
        int y = tile.getPosition().getY();
        TileType tileType = tile.getTileType();

        if (x < width && y < height) {
            final TileType oldTileType = getTileTypeAtPos(x, y);

            if (tileType != TileType.EMPTY) {
                tiles.put(new Int2DPoint(x, y), tileType);
                tileArray[x][y] = tileType;
            } else {
                tiles.remove(new Int2DPoint(x, y));
                tileArray[x][y] = null;
            }

            return logger.traceExit(createTileChangeDetail(tileType, oldTileType).build());
        } else {
            String errorMessage = String.format("Error trying tu put a tile (%d, %d) out of the map array bounds (array size=%dx%d)", x, y, width, height);
            logger.error(errorMessage);
            throw new ScreenException(errorMessage);
        }
    }

    public TileType getTileTypeAtPos(int x, int y) {
        return tileArray[x][y] != null ? tileArray[x][y] : TileType.EMPTY;
    }

    public long countNumTilesOfType(TileType tileType) {
        logger.traceEntry("Param: tile={}", tileType);

        long numTiles = tiles.values().stream().filter(t -> t == tileType).count();

        return logger.traceExit("Num tiles = {}", numTiles);
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tileArray[j][i] != null) {
                    sb.append(tileArray[j][i].getAbbreviation());
                } else {
                    sb.append(' ');
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    public static class Builder {
        private static final int MAX_SIZE = 1_000;

        private static final int DEFAULT_WIDTH = 100;
        private static final int DEFAULT_HEIGHT = 100;

        int width;
        int height;

        public Builder(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public static Builder createScreen() {
            return new Builder(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }

        public Builder withSize(int width, int height) {
            if (width <= 0 || width > MAX_SIZE) {
                throw new IllegalArgumentException(String.format("Invalid screen width: %d", width));
            }

            if (height <= 0 || height > MAX_SIZE) {
                throw new IllegalArgumentException(String.format("Invalid screen height: %d", height));
            }

            this.width = width;
            this.height = height;

            return this;
        }

        public Screen build() {
            return new Screen(new TileType[width][height], width, height);
        }
    }
}