package com.challenge.library.utils;

import com.challenge.library.utils.exception.WrongArraySizeException;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class ArrayUtils {
    public static int[][] transpose2dMatrix(int[][] matrix) {
        if (isEmpty(matrix))
            throw new NullPointerException("The matrix is null or empty");

        checkValid2dMatrix(matrix);

        int[][] transposedMatrix = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                transposedMatrix[j][i] = matrix[i][j];

        return transposedMatrix;
    }

    public static String matrixToString(int[][] matrix) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : matrix) {
            for (int element : row)
                stringBuilder.append(element);
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    public static int[][] splitIntArray(int[] array, int chunkSize) throws WrongArraySizeException {
        if (array.length % chunkSize != 0)
            throw new WrongArraySizeException(array.length, chunkSize);

        int numOfChunks = (int) Math.ceil((double) array.length / chunkSize);
        //System.out.println("numOfChunks = " + numOfChunks);

        int[][] chunkedArray = new int[numOfChunks][];
        int chunkNum = -1;

        for (int i = 0; i < array.length; i++) {
            if (i % chunkSize == 0)
                chunkedArray[++chunkNum] = new int[chunkSize];

            chunkedArray[chunkNum][i % chunkSize] = array[i];
        }

        return chunkedArray;
    }

    private static void checkValid2dMatrix(int[][] matrix) {
        int innerArraySize = matrix[0].length;
        for (int[] row : matrix) {
            if (isEmpty(row))
                throw new IllegalArgumentException("Not a valid 2d matrix: one of the rows is empty");
            if (row.length != innerArraySize)
                throw new IllegalArgumentException("Not a valid 2d matrix: rows of different length");
        }
    }
}
