package com.challenge.day14;

import java.util.Objects;

public class ChemicalQuantity {
    private final Chemical chemical;
    private final long quantity;

    private ChemicalQuantity(Chemical chemical, long quantity) {
        this.chemical = chemical;
        this.quantity = quantity;
    }

    public static ChemicalQuantity of(Chemical chemical, long quantity) {
        Objects.requireNonNull(chemical);

        return new ChemicalQuantity(chemical, quantity);
    }

    public Chemical getChemical() {
        return chemical;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return quantity + " " + chemical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChemicalQuantity that = (ChemicalQuantity) o;
        return quantity == that.quantity && chemical.equals(that.chemical);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chemical, quantity);
    }
}
