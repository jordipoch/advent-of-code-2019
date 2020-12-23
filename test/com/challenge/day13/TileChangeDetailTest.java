package com.challenge.day13;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import static com.challenge.day13.TileChangeDetail.Builder.createTileChangeDetail;
import static org.assertj.core.api.Assertions.*;

public class TileChangeDetailTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void createTileChangeDetailTest_tileAdded() {
        logger.info("Performing test...");

        TileChangeDetail tileChangeDetail = createTileChangeDetail(TileType.BLOCK, TileType.EMPTY).build();

        checkTileChangeDetail(tileChangeDetail, TileChangeDetail.TileChangeType.TILE_ADDED, TileType.BLOCK, null);

        logger.info("Test OK");
    }

    @Test
    public void createTileChangeDetailTest_tileRemoved() {
        logger.info("Performing test...");

        TileChangeDetail tileChangeDetail = createTileChangeDetail(TileType.EMPTY, TileType.BLOCK).build();

        checkTileChangeDetail(tileChangeDetail, TileChangeDetail.TileChangeType.TILE_REMOVED, null, TileType.BLOCK);

        logger.info("Test OK");
    }

    @Test
    public void createTileChangeDetailTest_tileReplaced() {
        logger.info("Performing test...");

        TileChangeDetail tileChangeDetail = createTileChangeDetail(TileType.BALL, TileType.BLOCK).build();

        checkTileChangeDetail(tileChangeDetail, TileChangeDetail.TileChangeType.TILE_REPLACED, TileType.BALL, TileType.BLOCK);

        logger.info("Test OK");
    }

    @Test
    public void createTileChangeDetailTest_NoChangeBall() {
        logger.info("Performing test...");

        TileChangeDetail tileChangeDetail = createTileChangeDetail(TileType.BALL, TileType.BALL).build();

        checkTileChangeDetail(tileChangeDetail, TileChangeDetail.TileChangeType.NO_CHANGE, null, null);

        logger.info("Test OK");
    }

    @Test
    public void createTileChangeDetailTest_NoChangeEmpty() {
        logger.info("Performing test...");

        TileChangeDetail tileChangeDetail = createTileChangeDetail(TileType.EMPTY, TileType.EMPTY).build();

        checkTileChangeDetail(tileChangeDetail, TileChangeDetail.TileChangeType.NO_CHANGE, null, null);

        logger.info("Test OK");
    }

    private void checkTileChangeDetail(TileChangeDetail tileChangeDetail, TileChangeDetail.TileChangeType expectedChangeType,
                                       TileType expectedTileAdded, TileType expectedTileRemoved) {

        logger.info("Tile change detail = {}", tileChangeDetail);

        assertThat(tileChangeDetail.getChangeType()).as("Checking change type").isEqualTo(expectedChangeType);
        assertThat(tileChangeDetail.getTileAdded()).as("Checking tile added").isEqualTo(expectedTileAdded);
        assertThat(tileChangeDetail.getTileRemoved()).as("Checking tile removed").isEqualTo(expectedTileRemoved);
    }
}