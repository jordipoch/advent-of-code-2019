package com.challenge.day13;

import com.challenge.day13.exception.ArcadeCabinetException;
import com.challenge.library.intcodecomputer.IntCodeLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;

import static com.challenge.day13.ArcadeCabinet.Builder.createArcadeCabinetDemo;
import static com.challenge.day13.ArcadeCabinet.Builder.createArcadeCabinetFullGame;

public class Day13 {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws ArcadeCabinetException, IOException {
        try {
            long numBlocks = runDay13Part1();
            System.out.println("Num blocks = " + numBlocks);
        } catch (Exception e) {
            logger.error("Error executing day13 challenge, part 1", e);
            throw e;
        }

        try {
            long score = runDay13Part2();
            System.out.println("Final score = " + score);
        } catch (Exception e) {
            logger.error("Error executing day13 challenge, part 2", e);
            throw e;
        }
    }

    public static long runDay13Part1() throws IOException, ArcadeCabinetException {
        final ArcadeCabinet arcadeCabinet = createArcadeCabinetDemo()
                .withGameCode(loadGameCode())
                .build();

        return arcadeCabinet.runGame();
    }

    public static long runDay13Part2() throws IOException, ArcadeCabinetException {
        final ArcadeCabinet arcadeCabinet = createArcadeCabinetFullGame()
                .withGameCode(loadGameCodePlayFree())
                .withScreenSize(45, 26)
                .withLoggingEnabled(100)
                .build();

        return arcadeCabinet.runGame();
    }


    private static BigInteger[] loadGameCode() throws IOException {
        return IntCodeLoader
                .getInstance()
                .loadBigIntCodeFromFile(Paths.get("resources", "com", "challenge", "day13"), "input.txt");


    }

    private static BigInteger[] loadGameCodePlayFree() throws IOException {
        BigInteger[] gameCode = loadGameCode();
        gameCode[0] = BigInteger.valueOf(2);
        return gameCode;
    }
}
