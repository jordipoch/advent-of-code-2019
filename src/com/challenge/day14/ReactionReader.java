package com.challenge.day14;

import com.challenge.day14.exception.ReactionException;
import com.challenge.day14.exception.ReactionReaderException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.challenge.library.files.TextFileReader.readAllLinesFromFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReactionReader {
    private static final Logger logger = LogManager.getLogger();

    private static final Path BASE_PATH = Paths.get("resources", "com", "challenge", "day14");

    private ReactionReader() {
    }

    public static List<Reaction> readReactionsFromFile(String filename) throws ReactionReaderException {
        logger.traceEntry("filename = \"{}\"", filename);

        try {
            var strReactions = readAllLinesFromFile(BASE_PATH.resolve(filename));
            return logger.traceExit(parseReactions(strReactions));
        } catch (IOException e) {
            throw new ReactionReaderException("Can't read the reactions from file " + filename, e);
        }
    }

    private static List<Reaction> parseReactions(List<String> strReactions) throws ReactionReaderException {
        logger.traceEntry("Parsing list of reactions {}", strReactions);

        List<Reaction> reactions = new ArrayList<>();

        for (var strReaction : strReactions) {
            reactions.add(parseReaction(strReaction));
        }

        return logger.traceExit(reactions);
    }

    private static Reaction parseReaction(String strReaction) throws ReactionReaderException {
        logger.traceEntry("strReaction = \"{}\"", strReaction);

        String[] reactionComponents = strReaction.split("=>");

        checkReactionComponents(strReaction, reactionComponents);

        try {
            return logger.traceExit(Reaction.createReaction(parseReactionInput(reactionComponents[0]), parseChemicalQuantity(reactionComponents[1])));
        } catch (ReactionException e) {
            throw new ReactionReaderException(String.format("Error creating Reaction object from input=%s and output=%s", reactionComponents[0], reactionComponents[1]), e);
        }
    }

    private static void checkReactionComponents(String strReaction, String[] reactionComponents) throws ReactionReaderException {
        if (reactionComponents.length != 2) {
            throw new ReactionReaderException(String.format("Invalid reaction (%s). It should have the format \"[input elements] => [output element]", strReaction));
        }
    }

    private static List<ChemicalQuantity> parseReactionInput(String reactionInput) throws ReactionReaderException {
        logger.traceEntry("reactionInput = \"{}\"", reactionInput);

        String[] parts = reactionInput.trim().split(",");
        List<ChemicalQuantity> chemicalQuantities = new ArrayList<>();

        for(var part : parts) {
            chemicalQuantities.add(parseChemicalQuantity(part));
        }

        return logger.traceExit(chemicalQuantities);
    }


    private static ChemicalQuantity parseChemicalQuantity(String strChQtty) throws ReactionReaderException {
        logger.traceEntry("strChQtty = \"{}\"", strChQtty);

        String[] parts = strChQtty.trim().split(" ");

        if (parts.length != 2) {
            throw new ReactionReaderException(String.format("Bad chemical-quantity format (%s). The chemical-quantity is expected to have 2 components", strChQtty));
        }

        long quantity;
        try {
            quantity = Long.parseLong(parts[0].trim());
        } catch (NumberFormatException e) {
            throw new ReactionReaderException(String.format("Invalid quantity (%s) for chemical quantity \"%s\"", parts[0].trim(), strChQtty));
        }

        var chemical = Chemical.of(parts[1].trim());

        return logger.traceExit(ChemicalQuantity.of(chemical, quantity));
    }
}
