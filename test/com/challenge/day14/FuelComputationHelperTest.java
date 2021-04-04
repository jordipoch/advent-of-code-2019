package com.challenge.day14;

import com.challenge.day14.exception.FuelComputationHelperException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FuelComputationHelperTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void testOreQttyMatchesAvailableOre() throws FuelComputationHelperException {
        logger.info("Performing test...");

        final long expectedFuelQtty = 12L;
        final var helper = new FuelComputationHelper(12L, 1000L);
        final boolean found = helper.updateFuelAmountsAndCheck(1000L);

        logger.debug(helper);
        assertThat(found).as("Checking if max fuel found...").isTrue();
        assertThat(helper.getCurrentFuelQtty()).as("Checking max fuel found is...").isEqualTo(expectedFuelQtty);

        logger.info("Test OK");
    }

    @Test
    public void testOreQttyAboveAvailableOre() throws FuelComputationHelperException {
        logger.info("Performing test...");

        final long expectedFuelQtty = 6L;
        final var helper = new FuelComputationHelper(12L, 1000L);
        final boolean found = helper.updateFuelAmountsAndCheck(1600L);

        logger.debug(helper);
        assertThat(found).as("Checking if max fuel found...").isFalse();
        assertThat(helper.getCurrentFuelQtty()).as("Checking max fuel found is...").isEqualTo(expectedFuelQtty);

        logger.info("Test OK");
    }

    @Test
    public void testOreQttyBelowAvailableOre() throws FuelComputationHelperException {
        logger.info("Performing test...");

        final long expectedFuelQtty = 18L;
        final var helper = new FuelComputationHelper(12L, 1000L);
        final boolean found = helper.updateFuelAmountsAndCheck(600L);

        logger.debug(helper);
        assertThat(found).as("Checking if max fuel found...").isFalse();
        assertThat(helper.getCurrentFuelQtty()).as("Checking max fuel found is...").isEqualTo(expectedFuelQtty);

        logger.info("Test OK");
    }

    @Test
    public void testFoundFromBelowToAbove() throws FuelComputationHelperException {
        logger.info("Performing test...");

        final long[] oreQttyInputs = {500, 800, 950, 1_050};
        final long expectedFuelQtty = 21L;
        final boolean[] expectedFounds = {false, false, false, true};

        final var helper = new FuelComputationHelper(12L, 1000L);

        performUpdatesAndCheck(helper, oreQttyInputs, expectedFounds);

        assertThat(helper.getCurrentFuelQtty()).as("Checking max fuel found is...").isEqualTo(expectedFuelQtty);

        logger.info("Test OK");
    }

    @Test
    public void testFoundFromAboveToBelow() throws FuelComputationHelperException {
        logger.info("Performing test...");

        final long[] oreQttyInputs = {500, 800, 1_050, 950};
        final long expectedFuelQtty = 20L;
        final boolean[] expectedFounds = {false, false, false, true};

        final var helper = new FuelComputationHelper(12L, 1000L);

        performUpdatesAndCheck(helper, oreQttyInputs, expectedFounds);

        assertThat(helper.getCurrentFuelQtty()).as("Checking max fuel found is...").isEqualTo(expectedFuelQtty);

        logger.info("Test OK");
    }

    private void performUpdatesAndCheck(FuelComputationHelper helper, long[] oreQttyInputs, boolean[] expectedFounds) throws FuelComputationHelperException {

        for (int i = 0; i < oreQttyInputs.length; i++) {
            boolean found = helper.updateFuelAmountsAndCheck(oreQttyInputs[i]);
            assertThat(found).as("Checking if max fuel found after call nº" + i).isEqualTo(expectedFounds[i]);
            logger.debug(helper);
        }
    }
}