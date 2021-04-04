package com.challenge.day14;

import static com.challenge.day14.ReactionReader.readReactionsFromFile;
import static com.challenge.day14.Nanofactory.Builder.createNanofactory;
import static com.challenge.day14.Reaction.createReaction;
import static com.challenge.day14.ChemicalQuantity.of;
import static com.challenge.day14.Chemical.FUEL;
import static com.challenge.day14.Chemical.ORE;

import com.challenge.day14.exception.NanofactoryException;
import com.challenge.day14.exception.ReactionException;
import com.challenge.day14.exception.ReactionReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static java.util.Collections.singletonList;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NanofactoryTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void testCalculateMinimumAmountOfOREToProduce1FuelSimpleOneReactionTest() throws ReactionException, NanofactoryException {
        logger.info("Performing test...");

        final var expectedCost = 3L;

        var nanofactory = createNanofactory()
            .withReaction(createReaction(singletonList(of(ORE, 3)), of(FUEL, 1)))
            .build();

        long cost = nanofactory.calculateMinimumAmountOfOREToProduce1Fuel();

        logger.debug("Cost = {}", cost);
        assertThat(cost).as("Checking calculated cost...").isEqualTo(expectedCost);
        logger.info("Test OK");
    }

    @Test
    public void testCalculateMinimumAmountOfOREToProduce1FuelSimpleThreeReactionTest() throws ReactionException, NanofactoryException {
        logger.info("Performing test...");

        final var expectedCost = 48L;

        var nanofactory = createNanofactory()
            .withReaction(createReaction(singletonList(of(ORE, 10)), of(Chemical.of("A"), 4)))
            .withReaction(createReaction(singletonList(of(ORE, 7)), of(Chemical.of("B"), 3)))
            .withReaction(createReaction(asList(of(Chemical.of("A"), 7), of(Chemical.of("B"), 10)), of(FUEL, 2)))
            .build();

        long cost = nanofactory.calculateMinimumAmountOfOREToProduce1Fuel();



        logger.debug("Cost = {}", cost);
        assertThat(cost).as("Checking calculated cost...").isEqualTo(expectedCost);
        logger.info("Test OK");
    }

    @Test(dataProvider = "inputParams_calculateMinAmountOfORE")
    public void testCalculateMinimumAmountOfOREToProduce1FuelParametrized(String inputFile, long expectedCost) throws ReactionReaderException, NanofactoryException {
        logger.info("Performing test...");

        testFromInputFile(inputFile, expectedCost);

        logger.info("Test OK");
    }

    @Test
    public void testCalculateAmountOfFuelProducedByXOre() throws NanofactoryException {
        logger.info("Performing test...");

        NanofactoryEngine nanofactoryEngine = givenANanofactoryEngineWithTheseOREAmountComputationResults(new Long[] {83L, 500L, 800L, 950L, 1050L});
        Nanofactory nanofactory = andANanofactoryWithNanofactoryEngine(nanofactoryEngine);


        final long expectedFuelQtty = 21L;
        final long actualFuelQtty = nanofactory.calculateAmountOfFuelProducedByXOre(1000L);

        verify(nanofactoryEngine, times(5)).calculateMinimumAmountOfOREToProduceXFuel(anyLong());
        assertThat(actualFuelQtty).as("Checking fuel quatity").isEqualTo(expectedFuelQtty);

        logger.info("Test OK");
    }

    @Test(dataProvider = "inputParams_calculateMaxAmountOfFUEL")
    public void testCalculateAmountOfFuelProducedByXOre_aTrillionOREParametrized(String inputFile, long expectedFuel) throws NanofactoryException, ReactionReaderException {
        logger.info("Performing test...");

        final var A_TRILLION = 1_000_000_000_000L;
        testCalculateAmountOfFuelProducedByXOreFromInputFile(inputFile, A_TRILLION, expectedFuel);

        logger.info("Test OK");
    }

    @DataProvider(name = "inputParams_calculateMinAmountOfORE")
    private Object[][] createData1() {
        return new Object[][] {
                { "sampleInput1.txt", 165L},
                { "sampleInput2.txt", 13_312L},
                { "sampleInput3.txt", 180_697L},
                { "sampleInput4.txt", 2_210_736L}
        };
    }

    @DataProvider(name = "inputParams_calculateMaxAmountOfFUEL")
    private Object[][] createData2() {
        return new Object[][] {
                { "sampleInput2.txt", 82_892_753L},
                { "sampleInput3.txt", 5_586_022L},
                { "sampleInput4.txt", 460_664L}
        };
    }

    private NanofactoryEngine givenANanofactoryEngineWithTheseOREAmountComputationResults(Long[] oreQuantities) throws NanofactoryException {
        NanofactoryEngine nanofactoryEngine = mock(NanofactoryEngine.class);

        var methodStub = when(nanofactoryEngine.calculateMinimumAmountOfOREToProduceXFuel(anyLong()));
        for (Long oreQuantity: oreQuantities) {
            methodStub = methodStub.thenReturn(oreQuantity);
        }

        return nanofactoryEngine;
    }


    private Nanofactory andANanofactoryWithNanofactoryEngine(NanofactoryEngine nanofactoryEngine) {
        return createNanofactory()
                .withNanofactoryEngine(nanofactoryEngine)
                .build();
    }

    private void testFromInputFile(String filename, long expectedCost) throws ReactionReaderException, NanofactoryException {
        final var nanofactory = createNanofactory()
                .withReactions(readReactionsFromFile(filename))
                .build();

        long cost = nanofactory.calculateMinimumAmountOfOREToProduce1Fuel();

        logger.debug("Cost = {}", cost);
        assertThat(cost).as("Checking calculated cost...").isEqualTo(expectedCost);
    }

    private void testCalculateAmountOfFuelProducedByXOreFromInputFile(String filename, long oreQuantityAvailable, long expectedFuel) throws ReactionReaderException, NanofactoryException {
        final var nanofactory = createNanofactory()
                .withReactions(readReactionsFromFile(filename))
                .build();

        long actualFuel = nanofactory.calculateAmountOfFuelProducedByXOre(oreQuantityAvailable);

        logger.debug("Fuel amount = {}", actualFuel);
        assertThat(actualFuel).as("Checking calculated fuel amount...").isEqualTo(expectedFuel);
    }
}