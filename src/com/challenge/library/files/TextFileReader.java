package com.challenge.library.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TextFileReader {
    public static String readFirstLineFromFile(Path filePath) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))) {
            return bufferedReader.readLine();
        }
    }

    public static List<String> readAllLinesFromFile(Path filePath) throws IOException {
        return Files.lines(filePath).collect(Collectors.toList());
    }
}
