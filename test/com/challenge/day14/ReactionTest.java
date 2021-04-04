package com.challenge.day14;

import static com.challenge.day14.Reaction.createReaction;

import com.challenge.day14.exception.ReactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactionTest {
    private static final Logger logger = LogManager.getLogger();

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testCreateReactionChecks_noInputChemicalsError() throws ReactionException {
        logger.info("Performing test...");

        try {
            createReaction(Collections.emptyList(), ChemicalQuantity.of(Chemical.FUEL, 1));
        } catch (IllegalArgumentException e) {
            logger.debug(e);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test(expectedExceptions = {ReactionException.class})
    public void testCreateReactionChecks_OREAsOutputError() throws ReactionException {
        logger.info("Performing test...");

        try {
            createReaction(Collections.singletonList(ChemicalQuantity.of(Chemical.of("A"), 1)), ChemicalQuantity.of(Chemical.ORE, 1));
        } catch (ReactionException e) {
            logger.debug(e);
            logger.info("Test OK");
            throw e;
        }
    }

    @Test
    public void testToStringMethod_1InputChemical() throws ReactionException {
        logger.info("Performing test...");

        final var expectedString = "(1 ORE => 1 FUEL)";

        var reaction = createReaction(Collections.singletonList(ChemicalQuantity.of(Chemical.ORE, 1)), ChemicalQuantity.of(Chemical.FUEL, 1));
        var actualString = reaction.toString();
        logger.debug("Actual string: {}", actualString);

        assertThat(actualString).as("Checking reaction text...").isEqualTo(expectedString);

        logger.info("Test OK");
    }

    @Test
    public void testToStringMethod_3InputChemicals() throws ReactionException {
        logger.info("Performing test...");

        final var expectedString = "(12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ)";

        final List<ChemicalQuantity> inputChemicals = Arrays.asList(
                ChemicalQuantity.of(Chemical.of("HKGWZ"), 12),
                ChemicalQuantity.of(Chemical.of("GPVTF"), 1),
                ChemicalQuantity.of(Chemical.of("PSHF"), 8)
        );

        var reaction = createReaction(inputChemicals, ChemicalQuantity.of(Chemical.of("QDVJ"), 9));
        var actualString = reaction.toString();
        logger.debug("Actual string: {}", actualString);

        assertThat(actualString).as("Checking reaction text...").isEqualTo(expectedString);

        logger.info("Test OK");
    }
}