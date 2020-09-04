package com.challenge.day8;

import com.challenge.day8.exception.NoPixelColorException;
import com.challenge.library.utils.exception.WrongArraySizeException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ImageGeneratorTest {
    @Test
    public void testVerifyImage1() throws WrongArraySizeException {
        verifyImage("123456789012", 3, 2, 1);
    }

    @Test
    public void testVerifyImage2() throws WrongArraySizeException {
        String image =  "010011221" +
                        "101021122" +
                        "120111221";
        verifyImage(image, 3, 3, 15);
    }

    @Test
    public void testGenerateImage1() throws WrongArraySizeException, NoPixelColorException {
        String image =  "0222" +
                        "1122" +
                        "2212" +
                        "0000";

        generateImage(image, 2, 2, new int[] {0, 1, 1, 0});

    }

    @Test
    public void testGenerateImage2() throws WrongArraySizeException, NoPixelColorException {
        String image =  "010011221" +
                        "101021122" +
                        "120111201";

        generateImage(image, 3, 3, new int[] {0, 1, 0, 0, 1, 1, 1, 0, 1});

    }

    @Test(expectedExceptions = com.challenge.day8.exception.NoPixelColorException.class)
    public void testErrorGenerateImage() throws WrongArraySizeException, NoPixelColorException {
        String image =  "010011221" +
                        "101021122" +
                        "120111221";

        generateImage(image, 3, 3, new int[] {0, 1, 0, 0, 1, 1, 1, 0, 1});
    }


    private void verifyImage(String pixels, int width, int height, int expected) throws WrongArraySizeException {
            ImageGenerator imageGenerator = ImageGenerator.createImageGenerator(pixels)
                    .withWidthAndHeight(width, height)
                    .build();

            System.out.println(String.format("Image to verify:%n%n%s", imageGenerator.getLayeredImage()));

            int verificationNumber = imageGenerator.calculateVerificationNumber();
            System.out.println("Verification number = " + verificationNumber);

            assertEquals(verificationNumber, expected);
    }

    private void generateImage(String pixels, int width, int height, int[] expected) throws WrongArraySizeException, NoPixelColorException {
        ImageGenerator imageGenerator = ImageGenerator.createImageGenerator(pixels)
                .withWidthAndHeight(width, height)
                .build();

        Image image = imageGenerator.calculateFinalImage();
        System.out.println(String.format("Image generated%n%s", image));

        assertEquals(image.getImagePixels(), expected, "The generated image and the expected one don't match");
    }
}
