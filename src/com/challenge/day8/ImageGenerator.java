package com.challenge.day8;

import com.challenge.day8.exception.NoPixelColorException;
import com.challenge.library.arrays.exception.WrongArraySizeException;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

import static com.challenge.library.arrays.ArrayUtils.splitIntArray;

public class ImageGenerator {
    LayeredImage layeredImage;

    private ImageGenerator(LayeredImage layeredImage) {
        this.layeredImage = layeredImage;
    }

    public int calculateVerificationNumber() {
        return layeredImage.calculateVerificationNumber();
    }

    public Image calculateFinalImage() throws NoPixelColorException {
        return layeredImage.calculateFinalImage();
    }

    public LayeredImage getLayeredImage() {
        return layeredImage;
    }

    public static Builder createImageGenerator(String pixels) {
        return new Builder(pixels);
    }

    public static class Builder {
        String strPixels;
        int width, height;

        public Builder(String strPixels) {
            this.strPixels = strPixels;
        }

        public Builder withWidthAndHeight(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public ImageGenerator build() throws WrongArraySizeException {

            int[] pixelArray = Arrays.stream(ArrayUtils.toObject(strPixels.toCharArray()))
                    .mapToInt(c -> Character.digit(c, 10))
                    .toArray();

            LayeredImage layeredImage = new LayeredImage(splitIntArray(pixelArray, width * height), width, height);
            return new ImageGenerator(layeredImage);
        }
    }
}
