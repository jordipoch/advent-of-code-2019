package com.challenge.day12;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class SaturnMoonSystemSnapshot {
    private final List<Moon> moons;
    private final long time;

    private SaturnMoonSystemSnapshot(List<Moon> moons, long time) {
        this.moons = moons;
        this.time = time;
    }

    public List<Moon> getMoons() {
        return moons;
    }

    public static SaturnMoonSystemSnapshot createSaturnMoonSystemSnapshot(List<Moon> moons, long time) {
        List<Moon> moonsCopy = new ArrayList<>();
        moons.forEach( moon -> moonsCopy.add(Moon.createMoon(moon)));

        return new SaturnMoonSystemSnapshot(moonsCopy, time);
    }

    public long getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaturnMoonSystemSnapshot that = (SaturnMoonSystemSnapshot) o;
        return moons.equals(that.moons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moons);
    }
}
