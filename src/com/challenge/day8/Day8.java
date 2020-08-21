package com.challenge.day8;

import static com.challenge.library.files.TextFileReader.readFirstLineFromFile;

import java.nio.file.Paths;

public class Day8 {
    public static void main(String[] args) {
        try {
            String input = readFirstLineFromFile(Paths.get("resources", "com", "challenge", "day8", "input.txt"));

            ImageGenerator imageGenerator = ImageGenerator.createImageGenerator(input).withWidthAndHeight(25, 6).build();
            System.out.println(String.format("Layered image to verify:%n%n%s", imageGenerator.getLayeredImage()));

            System.out.println("Verification number = " + imageGenerator.calculateVerificationNumber());

            System.out.println(String.format("Final image: %n%s", imageGenerator.calculateFinalImage()));
            // Image code -> CFCUG
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}