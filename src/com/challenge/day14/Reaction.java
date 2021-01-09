package com.challenge.day14;

import com.challenge.day14.exception.ReactionException;

import static com.challenge.day14.Chemical.ORE;

import java.util.List;
import java.util.Objects;

public class Reaction {
    private final List<ChemicalQuantity> inputChemicals;
    private final ChemicalQuantity outputChemical;

    private Reaction(List<ChemicalQuantity> inputChemicals, ChemicalQuantity outputChemical) {
        this.inputChemicals = inputChemicals;
        this.outputChemical = outputChemical;
    }

    public static Reaction createReaction(List<ChemicalQuantity> inputChemicals, ChemicalQuantity outputChemical) throws ReactionException {
        performChecks(inputChemicals, outputChemical);

        return new Reaction(inputChemicals, outputChemical);
    }

    public List<ChemicalQuantity> getInputChemicals() {
        return inputChemicals;
    }

    public ChemicalQuantity getInputChemical(Chemical chemical) {
        return inputChemicals.stream()
                .filter(chq -> chq.getChemical().equals(chemical))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The chemical is not in the input chemicals list!"));
    }

    public ChemicalQuantity getOutputChemical() {
        return outputChemical;
    }

    public boolean isInputChemicalInReaction(Chemical chemical) {
        return inputChemicals.stream()
                .map(ChemicalQuantity::getChemical)
                .anyMatch(ch -> ch.equals(chemical));
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder("(");
        inputChemicals.stream().forEach(chq -> { sb.append(chq); sb.append(", ");} );
        sb.delete(sb.length()-2, sb.length());
        sb.append(" => ");
        sb.append(outputChemical);
        sb.append(")");

        return sb.toString();
    }

    private static void performChecks(List<ChemicalQuantity> inputChemicals, ChemicalQuantity outputChemical) throws ReactionException {
        Objects.requireNonNull(inputChemicals);
        Objects.requireNonNull(outputChemical);
        if (inputChemicals.isEmpty()) {
            throw new IllegalArgumentException("At least one input chemical is needed.");
        }

        if (ORE.equals(outputChemical.getChemical())) {
            throw new ReactionException("The ORE chemical can't be the output of the reaction");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reaction reaction = (Reaction) o;
        return inputChemicals.equals(reaction.inputChemicals) && outputChemical.equals(reaction.outputChemical);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputChemicals, outputChemical);
    }
}
