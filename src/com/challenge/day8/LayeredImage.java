package com.challenge.day8;

import com.challenge.day8.exception.NoPixelColorException;

import java.util.Arrays;
import java.util.OptionalInt;

import static com.challenge.library.utils.ArrayUtils.transpose2dMatrix;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.repeat;

public class LayeredImage {
    int[][] pixelLayers;
    int width, height;

    public LayeredImage(int[][] pixelLayers, int width, int height) {
        this.pixelLayers = pixelLayers;
        this.width = width;
        this.height = height;
    }

    public int calculateVerificationNumber() {
        int[] layerWithFewestZeros = getLayerWithFewestZeros();

        return countNumberOfADigitInLayer(layerWithFewestZeros, 1) * countNumberOfADigitInLayer(layerWithFewestZeros, 2);
    }

    public Image calculateFinalImage() throws NoPixelColorException {

        /*int[] imagePixels = Arrays.copyOf(pixelLayers[0], pixelLayers[0].length);
        for (int j = 0; j < pixelLayers[0].length; j++) {
            INNER_FOR:
            for (int i = 1; i < pixelLayers.length; i++) {
                if (imagePixels[j] == 2 && pixelLayers[i][j] != 2) {
                    imagePixels[j] = pixelLayers[i][j];
                    break INNER_FOR;
                }
            }
        }*/

        int[][] transposedLayeredImage = transpose2dMatrix(pixelLayers);

        // System.out.println(String.format("Transposed image:%n%s", ArrayUtils.matrixToString(transposedLayeredImage)));
        int[] imagePixels = new int[width * height];
        for (int i = 0; i < transposedLayeredImage.length; i++) {
            OptionalInt finalPixel = Arrays.stream(transposedLayeredImage[i]).filter(p -> p != 2).findFirst();
            final int finalI = i;
            imagePixels[i] = finalPixel.orElseThrow(() -> new NoPixelColorException(finalI % width, finalI / height));
        }

        return new Image(imagePixels, width, height);
    }

    private int[] getLayerWithFewestZeros() {
        long minNumZeros = Integer.MAX_VALUE;
        int[] layerWithFewestZeroes = null;
        for (int[] layer: pixelLayers) {
            long numZeros = Arrays.stream(layer).filter(i -> i == 0).count();
            if (numZeros < minNumZeros) {
                minNumZeros = numZeros;
                layerWithFewestZeroes = layer;
            }
        }

        return layerWithFewestZeroes;
    }

    private int countNumberOfADigitInLayer(int[] layer, int digit) {
        return (int) Arrays.stream(layer).filter(i -> i == digit).count();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < pixelLayers.length; i++) {
            stringBuilder.append(formatLayer(i));
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    private String formatLayer(int layerNum) {
        StringBuilder stringBuilder = new StringBuilder(String.format("Layer %d: ", layerNum+1));
        String padding = repeat(SPACE, stringBuilder.length());

        int[] layer = pixelLayers[layerNum];
        stringBuilder.append(formatLayerRow(layer, 0));
        for (int i = 1; i < height; i++) {
            stringBuilder.append(padding);
            stringBuilder.append(formatLayerRow(layer, i));
        }

        return stringBuilder.toString();
    }

    private String formatLayerRow(int[] layer, int rowNum) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = rowNum * width; i < (rowNum + 1) * width; i++) {
            stringBuilder.append(layer[i]);
        }
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }

}
