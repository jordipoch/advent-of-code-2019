package com.challenge.day13;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class TileChangeDetail {
    private static final Logger logger = LogManager.getLogger();

    TileChangeType changeType;
    TileType tileAdded;
    TileType tileRemoved;

    private TileChangeDetail(TileChangeType changeType, TileType tileAdded, TileType tileRemoved) {
        this.changeType = changeType;
        this.tileAdded = tileAdded;
        this.tileRemoved = tileRemoved;
    }

    public TileChangeType getChangeType() {
        return changeType;
    }

    public TileType getTileAdded() {
        return tileAdded;
    }

    public TileType getTileRemoved() {
        return tileRemoved;
    }

    public enum TileChangeType {
        TILE_ADDED, TILE_REMOVED, TILE_REPLACED, NO_CHANGE
    }

    @Override
    public String toString() {
        switch (changeType) {
            case TILE_ADDED:
                return String.format("%s: %s", changeType, tileAdded);
            case TILE_REMOVED:
                return String.format("%s: %s", changeType, tileRemoved);
            case TILE_REPLACED:
                return String.format("%s: %s->%s", changeType, tileRemoved, tileAdded);
            case NO_CHANGE:
                return changeType.toString();
            default:
                return "Unknown tile type!";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TileChangeDetail that = (TileChangeDetail) o;
        return changeType == that.changeType && tileAdded == that.tileAdded && tileRemoved == that.tileRemoved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(changeType, tileAdded, tileRemoved);
    }

    public static class Builder {
        private final TileType newTileType;
        private final TileType oldTileType;

        public Builder(TileType newTileType, TileType oldTileType) {
            this.newTileType = newTileType;
            this.oldTileType = oldTileType;
        }

        public static Builder createTileChangeDetail(TileType newTileType, TileType oldTileType) {
            logger.traceEntry("Params: newTileType={}, oldTileType={}", newTileType, oldTileType);

            return new Builder(newTileType, oldTileType);
        }

        public TileChangeDetail build() {
            if (newTileType == oldTileType) {
                return logger.traceExit(createTileNoChangeResult());
            } else {
                if (newTileType != TileType.EMPTY) {
                    if (oldTileType == TileType.EMPTY) {
                        return logger.traceExit(createTileAddedResult(newTileType));
                    } else {
                        return logger.traceExit(createTileReplacedResult(newTileType, oldTileType));
                    }
                } else {
                    return logger.traceExit(createTileRemovedResult(oldTileType));
                }
            }
        }

        private TileChangeDetail createTileAddedResult(TileType tile) {
            return new TileChangeDetail(TileChangeType.TILE_ADDED, tile, null);
        }

        private TileChangeDetail createTileRemovedResult(TileType tile) {
            return new TileChangeDetail(TileChangeType.TILE_REMOVED, null, tile);
        }

        private TileChangeDetail createTileReplacedResult(TileType newTile, TileType oldTile) {
            return new TileChangeDetail(TileChangeType.TILE_REPLACED, newTile, oldTile);
        }

        private TileChangeDetail createTileNoChangeResult() {
            return new TileChangeDetail(TileChangeType.NO_CHANGE, null, null);
        }
    }
}
