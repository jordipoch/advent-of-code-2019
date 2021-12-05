package com.challenge.day15.helper;

import com.challenge.day15.CellType;
import com.challenge.day15.Grid;
import com.challenge.day15.exception.GridCreationException;
import com.challenge.day15.mocks.TestGridMap;
import com.challenge.day15.mocks.exception.TestGridMapCreationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class GridCreatorTest {
    private static final Logger logger = LogManager.getLogger();

    @Test (dataProvider = "gridMapTestFiles")
    public void simple2x2GridCreation(String fileName) throws TestGridMapCreationException, GridCreationException {
        logger.info("Performing test...");

        //final var fileName = "2x2ExploredGrid.txt";

        var expectedContent = TestGridMap.createTestGridMap(fileName);
        logger.debug("Expected content: {}", expectedContent);

        var exploredGrid = GridCreator.createExploredGridFromFile(fileName);

        logger.debug("Created grid: {}{}", System.lineSeparator(), exploredGrid);

        checkGridContents(exploredGrid, expectedContent);

        logger.info("Test OK");
    }

    private void checkGridContents(Grid exploredGrid, TestGridMap expectedContent) {
        final var xSize = expectedContent.getXSize();
        final var ySize = expectedContent.getYSize();

        for (var i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                var position = expectedContent.getPositionFromInternalArrayCoordinates(j, i);
                var expectedCellType = expectedContent.getCell(position);
                var actualCellType = exploredGrid.getCell(position).getCellType();
                if (expectedCellType.equals(CellType.INITIAL)) expectedCellType = CellType.EMPTY;
                assertThat(actualCellType).as("Checking that cell type for position %s is the one expected...", position).isEqualTo(expectedCellType);
            }
        }
    }

    @DataProvider(name = "gridMapTestFiles")
    private Iterator<Object> getGridMapTestFiles() {
        return Arrays.asList(new Object[]{
                "2x2ExploredGrid.txt",
                "5x5ExploredGrid.txt"
        }).iterator();
    }
}