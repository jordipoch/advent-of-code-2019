package com.challenge.day12;

import static com.challenge.day12.Moon.createMoon;
import com.challenge.library.geometry.model.Int3DCoord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaturnMoonSystem {
    private final List<Moon> moons;
    private int time;
    private final boolean showDebugInfo;
    private final int showDebugInfoSteps;

    private SaturnMoonSystem(List<Moon> moons, boolean showDebugInfo, int showDebugInfoSteps) {
        this.moons = moons;
        this.showDebugInfo = showDebugInfo;
        this.showDebugInfoSteps = showDebugInfoSteps;
    }

    public void simulateNSteps(int n) {
        for (int i = 0; i < n; i++) {
            applyGravityToMoons();
            time++;

            showDebugInformation();
        }
    }
    public int getTime() {
        return time;
    }

    private void showDebugInformation() {
        if (showDebugInfo && time % showDebugInfoSteps == 0)
            System.out.println(this.toString());
    }

    private void applyGravityToMoons() {
        final Map<Moon, Int3DCoord> deltaVelocityMap = new HashMap<>();
        moons.forEach(moon -> deltaVelocityMap.put(moon, Int3DCoord.ORIGIN));

        for (int i = 0; i < moons.size(); i++) {
            for (int j = i+1; j < moons.size(); j++) {
                Moon moon1 = moons.get(i);
                Moon moon2 = moons.get(j);

                deltaVelocityMap.put(moon1, deltaVelocityMap.get(moon1).add(moon1.calculateVelocityDelta(moon2)));
                deltaVelocityMap.put(moon2, deltaVelocityMap.get(moon2).add(moon2.calculateVelocityDelta(moon1)));
            }
        }

        moons.forEach(moon -> moon.applyVelocityChange(deltaVelocityMap.get(moon)));
    }

    public long calculateTotalEnergy() {
        return moons.stream().mapToLong(Moon::calculateTotalEnergy).sum();
    }

    private String getMoonsAsString() {
        final StringBuilder sbMoons = new StringBuilder();
        moons.forEach(moon -> sbMoons.append(moon.toString() + System.lineSeparator()));
        return sbMoons.toString();
    }

    @Override
    public String toString() {
        return String.format("After %d steps: %n%s", time, getMoonsAsString());
    }

    public static class Builder {
        private final List<Moon> moons = new ArrayList<>();
        private boolean showDebugInfo;
        private int showDebugInfoSteps;

        public static Builder createSaturnMoonSystem() {
            return new Builder();
        }

        public Builder withMoon(Int3DCoord position, Moon.MoonName name) {
            if (moonAlreadyExists(name)) {
                throw new IllegalArgumentException(String.format("A moon with a name %s already exists", name));
            }

            moons.add(createMoon(position, name));

            return this;
        }

        public Builder withDebugInfo(int steps) {
            showDebugInfo = true;
            showDebugInfoSteps = steps;
            return this;
        }

        public SaturnMoonSystem build() {
            return new SaturnMoonSystem(moons, showDebugInfo, showDebugInfoSteps);
        }

        private boolean moonAlreadyExists(Moon.MoonName name) {
            return moons.stream().map(Moon::getName).anyMatch(n -> n == name);
        }
    }
}
