package com.challenge.day12;

import java.util.Objects;

public class RepeatingStateInfo {
    private final long step;
    private final long repeatedStep;

    public RepeatingStateInfo(long step, long repeatedStep) {
        this.step = step;
        this.repeatedStep = repeatedStep;
    }

    public long getStep() {
        return step;
    }

    public long getRepeatedStep() {
        return repeatedStep;
    }

    @Override
    public String toString() {
        return String.format("{step: %d, repeated step: %d}", step, repeatedStep);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepeatingStateInfo that = (RepeatingStateInfo) o;
        return step == that.step &&
                repeatedStep == that.repeatedStep;
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, repeatedStep);
    }
}
