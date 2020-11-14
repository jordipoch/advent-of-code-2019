package com.challenge.day12;

import static com.challenge.day12.Moon.createMoon;
import static com.challenge.day12.SaturnMoonSystemSnapshot.createSaturnMoonSystemSnapshot;
import static com.challenge.day12.SaturnMoonSystem.LogConfiguration.createLogConfiguration;
import com.challenge.library.geometry.model.Int3DCoord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SaturnMoonSystem {
    private static final Logger logger = LogManager.getLogger();

    private final List<Moon> moons;
    private long time;
    private final LogConfiguration logConfig;

    private SaturnMoonSystem(List<Moon> moons, LogConfiguration logConfig) {
        this.moons = moons;
        this.logConfig = logConfig;
    }

    public void simulateNSteps(int n) {
        logger.traceEntry();

        for (long i = 0; i < n; i++) {
            applyGravityToMoons();
            time++;

            if (logConfig.isShowLogAfterSimulatingNSteps(time))
                logger.debug(this::toString);
        }

        logger.traceExit();
    }

    public Optional<RepeatingStateInfo> simulateAndFindRepeatingState(long maxSteps) {
        logger.traceEntry();

        Map<Long, List<SaturnMoonSystemSnapshot>> saturnMoonSystemHistory = new HashMap<>();

        for (long i = 0; i < maxSteps; i++) {
            if (logConfig.isShowLogAfterSimulatingNSteps(time))
                logger.debug("Simulating step {}", time);

            applyGravityToMoons();
            long repeatedStep = checkAndUpdateHistory(saturnMoonSystemHistory);

            if (repeatedStep >= 0) {
                return logger.traceExit(Optional.of(new RepeatingStateInfo(time, repeatedStep)));
            }
            time++;
        }

        return logger.traceExit(Optional.empty());
    }

    private long checkAndUpdateHistory(Map<Long, List<SaturnMoonSystemSnapshot>> saturnMoonSystemHistory) {
        logger.traceEntry("Size of saturnMoonSystemHistory: {}", saturnMoonSystemHistory.size());

        long totalEnergy = calculateTotalEnergy();
        List<SaturnMoonSystemSnapshot> snapshotList = saturnMoonSystemHistory.get(totalEnergy);
        final SaturnMoonSystemSnapshot currentSnapshot = createSaturnMoonSystemSnapshot(moons, time);
        if (snapshotList != null) {
            Optional<SaturnMoonSystemSnapshot> matchingSnapshot = snapshotList.stream().filter(snap -> snap.equals(currentSnapshot)).findFirst();
            if (matchingSnapshot.isPresent()) {
                return logger.traceExit(matchingSnapshot.get().getTime());
            } else {
                snapshotList.add(currentSnapshot);
            }
        } else {
            snapshotList = new ArrayList<>();
            snapshotList.add(currentSnapshot);
            saturnMoonSystemHistory.put(totalEnergy, snapshotList);
        }

        return logger.traceExit(-1);
    }


    public long getTime() {
        return time;
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
        logger.traceEntry();
        return logger.traceExit(moons.stream().mapToLong(Moon::calculateTotalEnergy).sum());
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
        private LogConfiguration logConfig;

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

        public Builder withLogConfig(LogConfiguration logConfig) {
            this.logConfig = logConfig;
            return this;
        }

        public SaturnMoonSystem build() {
            fillMissingInfo();
            return new SaturnMoonSystem(moons, logConfig);
        }

        private void fillMissingInfo() {
            if (logConfig == null) logConfig = createLogConfiguration(false);
        }

        private boolean moonAlreadyExists(Moon.MoonName name) {
            return moons.stream().map(Moon::getName).anyMatch(n -> n == name);
        }
    }

    public static class LogConfiguration {
        private boolean logEnabled = true;
        private long nSteps = 10;

        private LogConfiguration() {}

        private LogConfiguration(boolean logEnabled) {
            this.logEnabled = logEnabled;
        }

        public static LogConfiguration createLogConfiguration() {
            return new LogConfiguration();
        }

        public static LogConfiguration createLogConfiguration(boolean enabled) {
            return new LogConfiguration(enabled);
        }

        public LogConfiguration everyNSteps(long nSteps) {
            this.nSteps = nSteps;
            return this;
        }

        public boolean isLogEnabled() {
            return logEnabled;
        }

        public long getnSteps() {
            return nSteps;
        }

        public boolean isShowLogAfterSimulatingNSteps(long time) {
            return logEnabled && time % nSteps == 0;
        }
    }
}
