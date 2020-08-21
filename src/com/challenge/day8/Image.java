package com.challenge.day8;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.repeat;

public class Image {
    int[] imagePixels;
    int width, height;

    public Image(int[] imagePixels, int width, int height) {
        this.imagePixels = imagePixels;
        this.width = width;
        this.height = height;
    }

    public int[] getImagePixels() {
        return imagePixels;
    }

    @Override
    public String toString() {
//        StringBuilder stringBuilder = new StringBuilder(String.format("Image: "));
//        String padding = repeat(SPACE, stringBuilder.length());

        //stringBuilder.append(formatImageRow(imagePixels, 0));

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            formatImageRow(i, stringBuilder);
        }

        return stringBuilder.toString();



       // return Arrays.stream(imagePixels).mapToObj(i -> Integer.toString(i)).collect(Collectors.joining());
    }

    private void formatImageRow(int rowNum, StringBuilder stringBuilder) {
        for (int i = rowNum * width; i < (rowNum + 1) * width; i++) {
            stringBuilder.append(imagePixels[i] == 1 ? "X": " ");
        }
        stringBuilder.append(System.lineSeparator());
    }
}
