package com.challenge.library.intcodecomputer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class IntCodeLoader {
    private static IntCodeLoader instance = new IntCodeLoader();

    private IntCodeLoader() {}

    public static IntCodeLoader getInstance() { return instance; }

    public long[] loadIntCodeFromFile(Path basePath, String fileName) throws IOException {

        Path filePath = basePath.resolve(Paths.get(fileName));

        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))) {
            line = bufferedReader.readLine();
        }

        String[] strCode = line.split(",");
        long[] intCode = new long[strCode.length];

        for (int i = 0; i < strCode.length; i++) {
            intCode[i] = Long.parseLong(strCode[i]);
        }

        return intCode;
    }

    public BigInteger[] loadBigIntCodeFromFile(Path basePath, String fileName) throws IOException {

        Path filePath = basePath.resolve(Paths.get(fileName));

        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))) {
            line = bufferedReader.readLine();
        }

        String[] strCode = line.split(",");
        return Arrays.stream(strCode).map(BigInteger::new).toArray(BigInteger[]::new);
    }
}
