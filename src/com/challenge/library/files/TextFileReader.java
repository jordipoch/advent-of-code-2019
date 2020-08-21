package com.challenge.library.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextFileReader {
    public static String readFirstLineFromFile(Path filePath) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))) {
            return bufferedReader.readLine();
        }
    }
}
