package com.challenge.day14;

import com.challenge.day14.exception.NanofactoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.math.NumberUtils.max;

import static com.challenge.day14.Chemical.FUEL;
import static com.challenge.day14.Chemical.ORE;

class NanofactoryEngine {
    private static final Logger logger = LogManager.getLogger();

    private final Map<Chemical, Reaction> reactions;
    private Map<Chemical, ChemicalStock> nanofactoryStock;
    private Deque<Chemical> controlStack;

    NanofactoryEngine(Map<Chemical, Reaction> reactions) {
        this.reactions = reactions;
    }

    public long calculateMinimumAmountOfOREToProduceXFuel(long fuelQuantity) throws NanofactoryException {
        logger.traceEntry();

        nanofactoryStock = new HashMap<>();
        controlStack = new ArrayDeque<>();

        updateStockToProduceChemicalQuantity(
                ChemicalQuantity.of(Chemical.FUEL, fuelQuantity));

        long amount = nanofactoryStock.get(ORE).getQuantityNeeded();

        return logger.traceExit(amount);
    }

    public String getReactionsAsString() {
        var sb = new StringBuilder();

        for (var reaction: reactions.values()) {
            sb.append(reaction + System.lineSeparator());
        }

        return sb.toString();
    }

    private void updateStockToProduceChemicalQuantity(ChemicalQuantity chemicalQuantity) throws NanofactoryException {
        logger.traceEntry();

        final Chemical chemical = chemicalQuantity.getChemical();
        final long quantity = chemicalQuantity.getQuantity();

        logger.debug("Chemical to produce: {}. Control Stack: {}", chemicalQuantity, controlStack);

        if (ORE.equals(chemical)) {
            addToNanofactoryStock(chemical, ChemicalStock.of(quantity, 0L));
            return;
        }

        if (controlStack.contains(chemical)) {
            throw new NanofactoryException(String.format("Loop found in the reaction chain (Chemical %s)", chemical));
        }
        controlStack.addFirst(chemical);

        long actualQttyNeeded;
        if(FUEL.equals(chemical)) {
            actualQttyNeeded = quantity;
        } else {
            actualQttyNeeded = getActualQuantityNeeded(chemicalQuantity);
        }

        long actualQttyProduced;
        if (actualQttyNeeded == 0) {
            actualQttyProduced = 0;
        } else {

            var reaction = reactions.get(chemical);
            if (reaction == null) {
                throw new NanofactoryException(String.format("No reaction produce %s!", chemical));
            }

            logger.debug("Reaction to produce {}: {}", chemical, reaction);

            long multiplier = (long) Math.ceil((double) actualQttyNeeded / reaction.getOutputChemical().getQuantity());
            actualQttyProduced = reaction.getOutputChemical().getQuantity() * multiplier;

            for (var input : reaction.getInputChemicals()) {
                long inputQuantityNeeded = input.getQuantity() * multiplier;
                updateStockToProduceChemicalQuantity(ChemicalQuantity.of(input.getChemical(), inputQuantityNeeded));
            }
        }
        controlStack.removeFirst();

        if (!FUEL.equals(chemical)) {
            addToNanofactoryStock(chemical, ChemicalStock.of(quantity, actualQttyProduced));
        }

        logger.traceExit();
    }

    private void addToNanofactoryStock(Chemical chemical, ChemicalStock stock) {
        final var currentStock = nanofactoryStock.get(chemical);
        if (currentStock == null) {
            nanofactoryStock.put(chemical, stock);
        } else {
            nanofactoryStock.put(chemical, currentStock.add(stock));
        }
        logger.debug("Stock modified for {}. Current: {}. Addition: {}. New: {}.",
                chemical, currentStock != null ? currentStock : "(Non existent)", stock, nanofactoryStock.get(chemical));
    }

    private long getActualQuantityNeeded(ChemicalQuantity chemicalQuantity) {
        final var chemicalStock = nanofactoryStock.get(chemicalQuantity.getChemical());
        long qtty;
        if (chemicalStock == null) {
            qtty = chemicalQuantity.getQuantity();
        } else {
            qtty = max(0L, chemicalQuantity.getQuantity() + chemicalStock.getQuantityNeeded() - chemicalStock.getQuantityProduced());
        }

        logger.debug("Actual quantity needed for ({}): {}. Current stock: {}", chemicalQuantity, qtty, chemicalStock != null ? chemicalStock : "(Non existent)");

        return qtty;
    }

    private static class ChemicalStock {
        private final long quantityNeeded;
        private final long quantityProduced;

        public ChemicalStock(long quantityNeeded, long quantityProduced) {
            this.quantityNeeded = quantityNeeded;
            this.quantityProduced = quantityProduced;
        }

        public static ChemicalStock of(long quantityNeeded, long quantityProduced) {
            return new ChemicalStock(quantityNeeded, quantityProduced);
        }

        public ChemicalStock add(ChemicalStock otherStock) {
            return of(quantityNeeded + otherStock.getQuantityNeeded(), quantityProduced + otherStock.getQuantityProduced());
        }

        public long getQuantityNeeded() {
            return quantityNeeded;
        }

        public long getQuantityProduced() {
            return quantityProduced;
        }

        @Override
        public String toString() {
            return String.format("(N:%d, P:%d)", quantityNeeded, quantityProduced);
        }
    }
}
