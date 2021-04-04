package com.challenge.day14;

import com.challenge.day14.exception.FuelComputationHelperException;
import com.challenge.day14.exception.NanofactoryException;
import static com.challenge.day14.Chemical.FUEL;
import static com.challenge.day14.Chemical.ORE;
import static com.challenge.day14.Chemical.NAME_FUEL;
import static com.challenge.day14.Chemical.NAME_ORE;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Nanofactory {
    private static final Logger logger = LogManager.getLogger();

    private final NanofactoryEngine nanofactoryEngine;

    private Nanofactory(NanofactoryEngine nanofactoryEngine) {
        this.nanofactoryEngine = nanofactoryEngine;
    }

    public long calculateMinimumAmountOfOREToProduce1Fuel() throws NanofactoryException {
        logger.traceEntry();

        return logger.traceExit(nanofactoryEngine.calculateMinimumAmountOfOREToProduceXFuel(1));
    }

    public long calculateAmountOfFuelProducedByXOre(long oreQuantityAvail) throws NanofactoryException {
        logger.traceEntry("oreQuantityAvail = {}", oreQuantityAvail);

        final long oreFor1Fuel = nanofactoryEngine.calculateMinimumAmountOfOREToProduceXFuel(1);
        logger.info("{} ORE -> 1 FUEL", oreFor1Fuel);

        long currentFuelQtty = oreQuantityAvail / oreFor1Fuel;
        FuelComputationHelper fuelComputationHelper = new FuelComputationHelper(currentFuelQtty, oreQuantityAvail);

        boolean found = false;
        while (!found) {
            currentFuelQtty = fuelComputationHelper.getCurrentFuelQtty();
            long oreQtty = nanofactoryEngine.calculateMinimumAmountOfOREToProduceXFuel(currentFuelQtty);
            logger.info("{} ORE -> {} FUEL", oreQtty, currentFuelQtty);


            try {
                found = fuelComputationHelper.updateFuelAmountsAndCheck(oreQtty);
            } catch (FuelComputationHelperException e) {
                throw new NanofactoryException(e);
            }
            logger.debug(fuelComputationHelper);
        }

        return logger.traceExit(fuelComputationHelper.getCurrentFuelQtty());
    }

    public static class Builder {
        private final Map<Chemical, Reaction> reactions = new HashMap<>();
        private NanofactoryEngine nanofactoryEngine;

        private Builder() {
        }

        public static Builder createNanofactory() {
            return new Builder();
        }

        public Builder withReaction(Reaction reaction) throws NanofactoryException {
            logger.traceEntry();

            performReactionValidations(reaction);
            reactions.put(reaction.getOutputChemical().getChemical(), reaction);

            return logger.traceExit(this);
        }

        public Builder withReactions(List<Reaction> reactions) throws NanofactoryException {
            logger.traceEntry();

            for(var reaction: reactions) {
                withReaction(reaction);
            }

            return logger.traceExit(this);
        }

        public Builder withNanofactoryEngine(NanofactoryEngine nanofactoryEngine) {
            logger.traceEntry();

            this.nanofactoryEngine = nanofactoryEngine;

            return logger.traceExit(this);
        }

        public Nanofactory build() {
            logger.traceEntry();

            if (nanofactoryEngine == null) {
                nanofactoryEngine = new NanofactoryEngine(reactions);
            }
            return logger.traceExit(new Nanofactory(nanofactoryEngine));
        }

        private void performReactionValidations(Reaction reaction) throws NanofactoryException {
            Objects.requireNonNull(reaction);
            Chemical output = reaction.getOutputChemical().getChemical();

            if (ORE.equals(reaction.getOutputChemical().getChemical())) {
                throw new NanofactoryException(String.format("The %s chemical cannot be produced", NAME_ORE));
            }

            if (reaction.isInputChemicalInReaction(FUEL)) {
                throw new NanofactoryException(String.format("The %s chemical can only be produced", NAME_FUEL));
            }

            if(reactions.containsKey(output)) {
                throw new NanofactoryException("A reaction already exists to produce the chemical " + output);
            }
        }
    }
}
