package com.challenge.day13;

import static com.challenge.day13.Screen.createScreen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

public class ScreenTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void putTilesAndCountSimpleTest() throws ScreenException {
        logger.info("Performing test...");

        Screen screen = createScreen();

        screen.putTile(0, 0, 1);
        screen.putTile(1, 0, 2);
        screen.putTile(0, 1, 0);
        screen.putTile(2, 4, 4);
        screen.putTile(4, 2, 3);

        countAndCheckTiles(screen, Tile.BLOCK, 1L);

        logger.info("Test OK");
    }

    @Test
    public void putTilesAndCountOverrideTest() throws ScreenException {
        logger.info("Performing test...");

        Screen screen = createScreen();

        screen.putTile(0, 0, 1);
        screen.putTile(1, 0, 2);
        screen.putTile(0, 1, 2);
        screen.putTile(2, 4, 4);
        screen.putTile(1, 0, 3);
        screen.putTile(2, 0, 2);
        screen.putTile(0, 1, 2);

        countAndCheckTiles(screen, Tile.BLOCK, 2L);

        logger.info("Test OK");
    }

    @Test(expectedExceptions = {ScreenException.class})
    public void putTilesAndCountInvalidTileTest() throws ScreenException {
        logger.info("Performing test...");

        Screen screen = createScreen();

        try {
            screen.putTile(0, 0, 9);
        } catch (ScreenException e) {
            logger.info("Got exception: \"{}\"", e.getMessage());
            logger.info("Test OK");
            throw e;
        }
    }

    private void countAndCheckTiles(Screen screen, Tile tile, long expectedNum) {
        long numBlocks = screen.countNumTilesOfType(tile);

        assertThat(numBlocks).as("Checking num blocks...").isEqualTo(expectedNum);

        logger.info("Num blocks = {}. OK!", numBlocks);
    }
}