package com.challenge.day15.mocks;

import com.challenge.day15.CellType;
import com.challenge.day15.mocks.exception.TestGridMapCreationException;
import com.challenge.day15.mocks.exception.TestGridMapOutOfBoundsException;
import com.challenge.library.files.TextFileReader;
import com.challenge.library.geometry.model.Int2DPoint;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestGridMap {
    public static final Path BASE_PATH = Paths.get("resources", "com", "challenge", "day15");

    /*
            Y
    [0][0]  ^ +
            |
     -      |       +
    <---------------> X
            |
            |
            v -     [xSize-1][ySize-1]

    (x, y) -> [originOffset.x + x][originOffset.y - y]
    originOffset = (2, 2)
    (0, 0) -> [2][2]
    (2, -2) -> [4][4]
    (-2, 2) -> [0][0]
    */
    private final CellType[][] gridCellMatrix; // [X][Y]
    private final Int2DPoint originOffset;
    private final int xSize;
    private final int ySize;

    private TestGridMap(CellType[][] gridCellMatrix, Int2DPoint originOffset, int xSize, int ySize) {
        this.gridCellMatrix = gridCellMatrix;
        this.originOffset = originOffset;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public static TestGridMap createTestGridMap(String file) throws TestGridMapCreationException {
        var filePath = BASE_PATH.resolve(file);
        List<String> gridMapRows = readFile(filePath);

        var xSize = gridMapRows.get(0).length();
        var ySize = gridMapRows.size();
        CellType[][] gridCellMatrix = new CellType[xSize][ySize];
        Int2DPoint originOffset = new Int2DPoint(xSize/2, ySize/2);

        fillGridCellMatrix(gridCellMatrix, gridMapRows, xSize, ySize);

        return new TestGridMap(gridCellMatrix, originOffset, xSize, ySize);
    }

    public CellType getCell(Int2DPoint position) {
        var x = originOffset.getX() + position.getX();
        var y = originOffset.getY() - position.getY();
        checkArrayBounds(x, y, position);

        return gridCellMatrix[x][y];
    }

    public Int2DPoint getPositionFromInternalArrayCoordinates(int x, int y) {
        return new Int2DPoint(
                x - originOffset.getX(),
                originOffset.getY() - y
        );
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    private void checkArrayBounds(int x, int y, Int2DPoint position) {
        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            throw new TestGridMapOutOfBoundsException(position, x, y, xSize, ySize);
        }
    }

    private static List<String> readFile(Path file) throws TestGridMapCreationException {
        List<String> gridMapRows = null;
        try {
            gridMapRows = TextFileReader.readAllLinesFromFile(file);
        } catch (IOException e) {
            throw new TestGridMapCreationException("Cannot read file " + file.toString(), e);
        }
        return gridMapRows;
    }

    private static void fillGridCellMatrix(CellType[][] gridCellMatrix, List<String> gridMapRows, int xSize, int ySize) throws TestGridMapCreationException {
        for (int i = 0; i < ySize; i++) {
            String currentRow = getCurrentRow(gridMapRows, xSize, i);
            for (int j = 0; j < xSize; j++) {
                var charCode = currentRow.charAt(j);
                var x = j;
                var y = i;
                gridCellMatrix[j][i] = CellType.fromCharCode(charCode).orElseThrow(
                        () -> new TestGridMapCreationException(String.format("Invalid character '%c' at position (%d, %d)", charCode, x, y))
                );
            }
        }
    }

    private static String getCurrentRow(List<String> gridMapRows, int xSize, int rowNumber) throws TestGridMapCreationException {
        var currentRow = gridMapRows.get(rowNumber);
        if (currentRow.length() != xSize) {
            throw new TestGridMapCreationException(String.format("Wrong size for row %d (%d). Expecting size %d", rowNumber, currentRow.length(), xSize));
        }
        return currentRow;
    }

    @Override
    public String toString() {
        var gridContent = new StringBuilder();
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                gridContent.append(gridCellMatrix[j][i].getCharCode());
            }
            gridContent.append(System.lineSeparator());
        }

        return "TestGridMap{" +
                ", originOffset=" + originOffset +
                ", xSize=" + xSize +
                ", ySize=" + ySize +
                ", content=" + System.lineSeparator() +
                gridContent + System.lineSeparator() +
                '}';
    }
    
    public String getRawContent() {
        var sb = new StringBuilder();
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                sb.append(gridCellMatrix[j][i].getCharCode());
            }
        }
        return sb.toString();
    }
}
