package com.challenge.day13;

import com.challenge.day13.exception.ArcadeCabinetException;
import com.challenge.library.intcodecomputer.IntCodeLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;

import static com.challenge.day13.ArcadeCabinet.Builder.createArcadeCabinet;

public class Day13 {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            long numBlocks = runDay13Part1();
            System.out.println("Num blocks = " + numBlocks);
        } catch (Exception e) {
            logger.error("Error executing day13 challenge, part 1", e);
        }
    }

    public static long runDay13Part1() throws IOException, ArcadeCabinetException {
        final ArcadeCabinet arcadeCabinet = createArcadeCabinet()
                .withGameCode(loadGameCode())
                .build();

        return arcadeCabinet.runGame();
    }

    private static BigInteger[] loadGameCode() throws IOException {
        return IntCodeLoader
                .getInstance()
                .loadBigIntCodeFromFile(Paths.get("resources", "com", "challenge", "day13"), "input.txt");


    }
}
