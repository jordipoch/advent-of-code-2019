package com.challenge.day14;

import static com.challenge.day14.ReactionReader.readReactionsFromFile;

import com.challenge.day14.exception.ReactionException;
import com.challenge.day14.exception.ReactionReaderException;
import static com.challenge.day14.Reaction.createReaction;
import static com.challenge.day14.ChemicalQuantity.of;
import static com.challenge.day14.Chemical.FUEL;
import static com.challenge.day14.Chemical.ORE;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Arrays;
import static java.util.Collections.singletonList;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactionReaderTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void readReactionsFromFileSimpleTest() throws ReactionReaderException, ReactionException {
        logger.info("Performing test...");

        var reactions = readReactionsFromFile("readerInputSimpleTest.txt");

        var expectedReactions = Arrays.asList(
                createReaction(singletonList(of(ORE, 6)), of(Chemical.of("A"), 2)),
                createReaction(singletonList(of(Chemical.of("A"), 3)), of(FUEL, 1)));

        logger.debug("Reactions: {}", reactions);
        assertThat(reactions).as("Checking read reactions...").isEqualTo(expectedReactions);

        logger.info("Test OK");
    }

    @Test
    public void readReactionsFromFileComplexTest() throws ReactionReaderException, ReactionException {
        logger.info("Performing test...");

        var reactions = readReactionsFromFile("readerInputComplexTest.txt");

        final Chemical A = Chemical.of("A");
        final Chemical B = Chemical.of("B");
        final Chemical C = Chemical.of("C");
        var expectedReactions = Arrays.asList(
                createReaction(singletonList(of(ORE, 3)), of(A, 1)),
                createReaction(singletonList(of(ORE, 5)), of(B, 2)),
                createReaction(Arrays.asList(of(ORE, 10), of(A, 4)), of(C, 1)),
                createReaction(Arrays.asList(of(A, 3), of(B, 5), of(C, 4)), of(FUEL, 1))
        );

        logger.debug("Reactions: {}", reactions);
        assertThat(reactions).as("Checking read reactions...").isEqualTo(expectedReactions);

        logger.info("Test OK");
    }

    @Test(expectedExceptions = {ReactionReaderException.class})
    public void readReactionsFromFileErrorTest1() throws ReactionReaderException {
        logger.info("Performing test...");

        try {
            readReactionsFromFile("readerInputErrorTest1.txt");
        } catch (ReactionReaderException e) {
            logger.debug("Exception: {}", e.toString());
            assertThat(e.getMessage()).as("Checking exception message...").contains("It should have the format");
            logger.info("Test OK");
            throw e;
        }
    }

    @Test(expectedExceptions = {ReactionReaderException.class})
    public void readReactionsFromFileErrorTest2() throws ReactionReaderException {
        logger.info("Performing test...");

        try {
            readReactionsFromFile("readerInputErrorTest2.txt");
        } catch (ReactionReaderException e) {
            logger.debug("Exception: {}", e.toString());
            assertThat(e.getMessage()).as("Checking exception message...").startsWith("Bad chemical-quantity format");
            logger.info("Test OK");
            throw e;
        }
    }

    @Test(expectedExceptions = {ReactionReaderException.class})
    public void readReactionsFromFileErrorTest3() throws ReactionReaderException {
        logger.info("Performing test...");

        try {
            readReactionsFromFile("readerInputErrorTest3.txt");
        } catch (ReactionReaderException e) {
            logger.debug("Exception: {}", e.toString());
            assertThat(e.getMessage()).as("Checking exception message...").startsWith("Invalid quantity");
            logger.info("Test OK");
            throw e;
        }
    }
}
