package com.challenge.day15.mocks;

import com.challenge.day15.CellType;
import com.challenge.day15.mocks.TestGridMap;
import com.challenge.day15.mocks.exception.TestGridMapCreationException;
import com.challenge.day15.mocks.exception.TestGridMapOutOfBoundsException;
import com.challenge.library.geometry.model.Int2DPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class TestGridMapTest {
    private static final Logger logger = LogManager.getLogger();

    private static final Path BASE_PATH = Paths.get("resources", "com", "challenge", "day15");

    @Test (dataProvider = "testGridCreation")
    public void testGridMapTestCreation(String filename, String expectedChars) throws TestGridMapCreationException {
        logger.info("Performing test...");

        TestGridMap testGridMap = TestGridMap.createTestGridMap(filename);
        assertEquals(testGridMap.getRawContent(), expectedChars);

        logger.info("Test OK");
    }

    @Test (expectedExceptions = TestGridMapCreationException.class,
            dataProvider = "testGridCreationError")
    public void testGridMapTestCreationError(String filename, String expectedError) throws TestGridMapCreationException {
        logger.info("Performing test...");

        try {
            TestGridMap.createTestGridMap(filename);
        } catch (TestGridMapCreationException e) {
            assertThat(e.getMessage()).as("Asserting that exception message contains \"%s\"", expectedError)
                    .contains(expectedError);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test
    public void testGetCell() throws TestGridMapCreationException {
        logger.info("Performing test...");

        var testGridMap = TestGridMap.createTestGridMap("CellGridMapTest1.txt");

        Assert.assertEquals(testGridMap.getCell(Int2DPoint.ORIGIN), CellType.EMPTY);
        assertEquals(testGridMap.getCell(new Int2DPoint(-2, 0)), CellType.OXYGEN);
        assertEquals(testGridMap.getCell(new Int2DPoint(-2, 2)), CellType.EMPTY);
        assertEquals(testGridMap.getCell(new Int2DPoint(1, 2)), CellType.WALL);
        assertEquals(testGridMap.getCell(new Int2DPoint(1, -1)), CellType.WALL);

        logger.info("Test OK");
    }

    @Test (expectedExceptions = TestGridMapOutOfBoundsException.class,
        dataProvider = "testGridGetCellInvalidPositions")
    public void testGetCellErrorOutOfBounds(Int2DPoint invalidPos) throws TestGridMapCreationException {
        logger.info("Performing test...");

        var testGridMap = TestGridMap.createTestGridMap("CellGridMapTest1.txt");

        try {
            testGridMap.getCell(invalidPos);
        } catch(TestGridMapOutOfBoundsException e) {
            logger.info("Got exception: {}", e.getMessage());
            logger.info("Test OK");
            throw e;
        }
    }

    @DataProvider(name = "testGridCreation")
    private Iterator<Object[]> createTestGridCreation() {
        return Arrays.asList(
                new Object[] {"CellGridMapTest1.txt", ".#.##..#O...##.#"},
                new Object[] {"CellGridMapTest2.txt",
                        "##########" +
                        "#...#....#" +
                        "#...####.#" +
                        "#........#" +
                        "#..#.#.###" +
                        "#......#.#" +
                        "###.#....#" +
                        "#O..###..#" +
                        "#........#" +
                        "##########"}

        ).iterator();
    }

    @DataProvider(name = "testGridCreationError")
    private Iterator<Object[]> createTestGridCreationError() {
        return Arrays.asList(
                new Object[] {"CellGridMapTestError1.txt", "Invalid character"},
                new Object[] {"CellGridMapTestError2.txt", "Wrong size for row"}
        ).iterator();
    }

    @DataProvider(name = "testGridGetCellInvalidPositions")
    private Iterator<Object> createInvalidPositions() {
        return Arrays.<Object>asList(
                new Int2DPoint(2, 2),
                new Int2DPoint(2, -2),
                new Int2DPoint(-3, 0),
                new Int2DPoint(-1, 3)
        ).iterator();
    }
}