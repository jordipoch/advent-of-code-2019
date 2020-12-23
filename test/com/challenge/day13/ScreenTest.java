package com.challenge.day13;

import static com.challenge.day13.Screen.Builder.createScreen;

import com.challenge.day13.exception.ScreenException;
import com.challenge.day13.exception.WrongTileException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import static com.challenge.day13.TileChangeDetail.Builder.createTileChangeDetail;
import static org.assertj.core.api.Assertions.*;

public class ScreenTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void putTilesAndCountSimpleTest() throws ScreenException {
        logger.info("Performing test...");

        Screen screen = createScreen().build();

        screen.putTile(0, 0, 1);
        screen.putTile(1, 0, 2);
        screen.putTile(0, 1, 0);
        screen.putTile(2, 4, 4);
        screen.putTile(4, 2, 3);

        countAndCheckTiles(screen, TileType.BLOCK, 1L);

        logger.info("Test OK");
    }

    @Test
    public void putTilesAndCountOverrideTest() throws ScreenException {
        logger.info("Performing test...");

        Screen screen = createScreen().build();

        screen.putTile(0, 0, 1);
        screen.putTile(1, 0, 2);
        screen.putTile(0, 1, 2);
        screen.putTile(2, 4, 4);
        screen.putTile(1, 0, 3);
        screen.putTile(2, 0, 2);
        screen.putTile(0, 1, 2);

        countAndCheckTiles(screen, TileType.BLOCK, 2L);

        logger.info("Test OK");
    }

    @Test(expectedExceptions = {ScreenException.class})
    public void putTilesAndCountInvalidTileTest() throws ScreenException {
        logger.info("Performing test...");

        Screen screen = createScreen().build();

        try {
            screen.putTile(0, 0, 9);
        } catch (ScreenException e) {
            logger.info("Got exception: \"{}\"", e.getMessage());
            logger.info("Test OK");
            throw e;
        }
    }

    @Test
    public void putTilesAddedTileTest() throws ScreenException, WrongTileException {
        logger.info("Performing test...");

        final Int2DPoint pos = new Int2DPoint(1, 0);
        final Screen screen = createScreen().build();

        screen.putTile(Tile.of(0, 0, TileType.BLOCK.getId()));
        TileChangeDetail tileChangeDetail = screen.putTile(Tile.of(pos.getX(), pos.getY(), TileType.BALL.getId()));

        checkPutTileResult(tileChangeDetail, createTileChangeDetail(TileType.BALL, TileType.EMPTY).build());
        checkTileAtPos(screen, pos, TileType.BALL);

        logger.info("Test OK");
    }

    @Test
    public void putTilesReplaceLogicRemovedTileTest() throws ScreenException, WrongTileException {
        logger.info("Performing test...");

        final Int2DPoint pos = new Int2DPoint(0, 0);
        Screen screen = createScreen().build();

        screen.putTile(Tile.of(pos.getX(), pos.getY(), TileType.BLOCK.getId()));
        TileChangeDetail tileChangeDetail = screen.putTile(Tile.of(pos.getX(), pos.getY(), TileType.EMPTY.getId()));

        checkPutTileResult(tileChangeDetail,createTileChangeDetail(TileType.EMPTY, TileType.BLOCK).build());
        checkTileAtPos(screen, pos, TileType.EMPTY);

        logger.info("Test OK");
    }

    @Test
    public void putTilesReplaceLogicReplaceTileTest() throws ScreenException, WrongTileException {
        logger.info("Performing test...");

        final Int2DPoint pos = new Int2DPoint(0, 0);
        Screen screen = createScreen().build();

        screen.putTile(Tile.of(pos.getX(), pos.getY(), TileType.BLOCK.getId()));
        TileChangeDetail tileChangeDetail = screen.putTile(Tile.of(pos.getX(), pos.getY(), TileType.BALL.getId()));

        checkPutTileResult(tileChangeDetail, createTileChangeDetail(TileType.BALL, TileType.BLOCK).build());
        checkTileAtPos(screen, pos, TileType.BALL);

        logger.info("Test OK");
    }

    @Test
    public void putTilesReplaceLogicNoChangeTest() throws ScreenException, WrongTileException {
        logger.info("Performing test...");

        final Int2DPoint pos = new Int2DPoint(0, 0);
        Screen screen = createScreen().build();

        screen.putTile(Tile.of(pos.getX(), pos.getY(), TileType.BALL.getId()));
        TileChangeDetail tileChangeDetail = screen.putTile(Tile.of(pos.getX(), pos.getY(), TileType.BALL.getId()));

        checkPutTileResult(tileChangeDetail, createTileChangeDetail(TileType.BALL, TileType.BALL).build());
        checkTileAtPos(screen, pos, TileType.BALL);

        logger.info("Test OK");
    }

    private void countAndCheckTiles(Screen screen, TileType tileType, long expectedNum) {
        long numBlocks = screen.countNumTilesOfType(tileType);

        assertThat(numBlocks).as("Checking num blocks...").isEqualTo(expectedNum);

        logger.info("Num blocks = {}. OK!", numBlocks);
    }

    private void checkPutTileResult(TileChangeDetail result, TileChangeDetail expected) {
        logger.info("Put tile result: {}", result);
        assertThat(result).as("Checking tile change details...").isEqualTo(expected);


    }

    private void checkTileAtPos(Screen screen, Int2DPoint pos, TileType expectedTileType) {
        TileType tileTypeAtPos = screen.getTileTypeAtPos(pos.getX(), pos.getY());
        logger.info("New tile at pos {}: {}", pos, tileTypeAtPos);
        assertThat(tileTypeAtPos).as("Checking tile type in array...").isEqualTo(expectedTileType);
    }
}