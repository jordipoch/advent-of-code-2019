package com.challenge.day12;

import static com.challenge.day12.Moon.createMoon;
import static com.challenge.day12.SaturnMoonSystemSnapshot.createSaturnMoonSystemSnapshot;
import static com.challenge.day12.SaturnMoonSystem.LogConfiguration.createLogConfiguration;
import com.challenge.library.geometry.model.Int3DCoord;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SaturnMoonSystem {
    private static final Logger logger = LogManager.getLogger();
    private static final long SEQUENCE_LENGTH_TO_FIND = 1_000;

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

    public long getTime() {
        return time;
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

    public long simulateAndFindRepeatingStateV2(long maxSteps) {
        logger.traceEntry();

        Map<Long, Set<Long>> energySequenceMap = new HashMap<>();
        long previousEnergy = -1;
        long repeatingStep = -1;
        long repeatingSequenceLength = 0;
        long maxRepeatingSequenceLength = 0;

        boolean repeatingStateFound = false;
        for (long i = 0; i < maxSteps && !repeatingStateFound; i++) {
            if (logConfig.isShowLogAfterSimulatingNSteps(time)) {
                logger.debug("Simulating step {}", time);
            }

            long energy = calculateTotalEnergy();
            if (isEnergyRepeated(energy, energySequenceMap)) {
                if (doesEnergyFollowPreviousSequence(previousEnergy, energy, energySequenceMap)) {
                    repeatingSequenceLength++;
                    if (repeatingSequenceLength == SEQUENCE_LENGTH_TO_FIND) {
                        maxRepeatingSequenceLength = repeatingSequenceLength;
                        repeatingStateFound = true;
                        logger.debug("Found repeating energy sequence of length {} starting at step {}!", repeatingSequenceLength, repeatingStep);
                    }
                } else {
                    repeatingStep = time;
                    maxRepeatingSequenceLength = NumberUtils.max(repeatingSequenceLength, maxRepeatingSequenceLength);
                    repeatingSequenceLength = 1;

                    addEnergyToFollowingSequence(previousEnergy, energy, energySequenceMap);

                    logger.trace("Energy sequence starts repeating at step {} (current energy does not flow previous one)", repeatingStep);
                }
            } else {
                maxRepeatingSequenceLength = NumberUtils.max(repeatingSequenceLength, maxRepeatingSequenceLength);
                repeatingSequenceLength = 0;
                repeatingStep = -1;

                addNewEnergyToSequence(energy, energySequenceMap);
                if (previousEnergy != -1) {
                    addEnergyToFollowingSequence(previousEnergy, energy, energySequenceMap);
                }

                logger.trace("Energy sequence broken with length {} (current energy not found)", repeatingSequenceLength);
            }

            previousEnergy = energy;
            applyGravityToMoons();

            time++;

            if (logConfig.isShowLogAfterSimulatingNSteps(time)) {
                logger.debug("After step {}: energy={}, repeating step={}, sequence length={}, max sequence length={})", time, energy, repeatingStep, repeatingSequenceLength, maxRepeatingSequenceLength);
                if (!repeatingStateFound) {
                    printEnergySequenceMap(energySequenceMap, Level.TRACE);
                }
            }
        }

        printEnergySequenceMap(energySequenceMap, Level.TRACE);
        logger.info("Process finished. Max repeating sequence length found: {}", maxRepeatingSequenceLength);

        return logger.traceExit(repeatingStateFound ? repeatingStep : -1);
    }

    private boolean isEnergyRepeated(long energy, Map<Long, Set<Long>> energySequenceMap) {
        return energySequenceMap.containsKey(energy);
    }

    private boolean doesEnergyFollowPreviousSequence(long previousEnergy, long currentEnergy, Map<Long, Set<Long>> energySequenceMap) {
        Set<Long> followingEnergiesInSequence = energySequenceMap.get(previousEnergy);
        return followingEnergiesInSequence != null && followingEnergiesInSequence.contains(currentEnergy);
    }

    private void addNewEnergyToSequence(long energy, Map<Long, Set<Long>> energySequenceMap) {
        energySequenceMap.put(energy, new HashSet<>());
    }

    private void addEnergyToFollowingSequence(long previousEnergy, long currentEnergy, Map<Long, Set<Long>> energySequenceMap) {
        energySequenceMap.get(previousEnergy).add(currentEnergy);
    }

    private void printEnergySequenceMap(Map<Long, Set<Long>> energySequenceMap, Level level) {
        logger.log(level, "Energy sequence map content:");
        for (Map.Entry<Long, Set<Long>> sequenceItem : energySequenceMap.entrySet()) {
            logger.log(level, "{} -> {}", sequenceItem.getKey(), sequenceItem.getValue());
        }
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
