package com.challenge.day13;

import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Screen {
    private static final Logger logger = LogManager.getLogger();

    Map<Int2DPoint, Tile> tiles = new HashMap<>();

    public static Screen createScreen() {
        return new Screen();
    }

    public void putTile(int x, int y, int tileId) throws ScreenException {
        logger.traceEntry("Params: x={}, y={}, tileId={}", x, y, tileId);

        if (Tile.fromId(tileId).isPresent()) {
            Tile tile = Tile.fromId(tileId).get();
            tiles.put(new Int2DPoint(x, y), tile);
        } else {
            throw new ScreenException(String.format("Wrong tile Id (%d)", tileId));
        }

        logger.traceExit();
    }

    public long countNumTilesOfType(Tile tile) {
        logger.traceEntry("Param: tile={}", tile);

        long numTiles = tiles.values().stream().filter(t -> t == tile).count();

        return logger.traceExit("Num tiles = {}", numTiles);
    }
}
