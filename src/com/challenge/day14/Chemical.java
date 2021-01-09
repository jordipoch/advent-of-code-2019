package com.challenge.day14;

import static org.apache.commons.lang3.StringUtils.isBlank;
import java.util.Objects;

public class Chemical {
    public static final String NAME_ORE = "ORE";
    public static final String NAME_FUEL = "FUEL";

    public static final Chemical ORE = new Chemical(NAME_ORE);
    public static final Chemical FUEL = new Chemical(NAME_FUEL);

    private final String name;

    private Chemical(String name) {
        this.name = name;
    }

    public static Chemical of(String name) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("The chemical name can't be a blank screen");
        }

        return switch (name) {
            case NAME_ORE -> ORE;
            case NAME_FUEL -> FUEL;
            default -> new Chemical(name);
        };
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chemical chemical = (Chemical) o;
        return name.equals(chemical.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
