package com.challenge.day12;

import static com.challenge.day12.Moon.createMoon;
import static com.challenge.day12.SaturnMoonSystemSnapshot.createSaturnMoonSystemSnapshot;
import static com.challenge.day12.SaturnMoonSystem.LogConfiguration.createLogConfiguration;
import com.challenge.library.geometry.model.mutable.Int3DCoord;
import org.apache.commons.lang3.math.NumberUtils;
import static com.challenge.library.utils.NumberUtils.sign;
import org.apache.logging.log4j.Level;
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

    public long simulateAndFindRepeatingStateV2(long maxSteps, int sequenceLength) {
        logger.traceEntry();

        Map<Integer, Set<Integer>> energySequenceMap = new HashMap<>();
        int previousEnergy = -1;
        long repeatingStep = -1;
        long repeatingSequenceLength = 0;
        long maxRepeatingSequenceLength = 0;

        // For statistics
        int maxEnergy = 0;
        int totalEnergiesSaved = 0;
        int maxFollowingSequenceLength = 0;

        boolean repeatingStateFound = false;
        for (long i = 0; i < maxSteps && !repeatingStateFound; i++) {

            int energy = calculateTotalEnergy();
            assert(energy >= 0);
            logger.debug("Energy at step {}: {}", time, energy);
            maxEnergy = NumberUtils.max(energy, maxEnergy);

            if (isEnergyRepeated(energy, energySequenceMap)) {
                if (doesEnergyFollowPreviousSequence(previousEnergy, energy, energySequenceMap)) {
                    repeatingSequenceLength++;
                    if (repeatingSequenceLength == sequenceLength) {
                        maxRepeatingSequenceLength = repeatingSequenceLength;
                        repeatingStateFound = true;
                        logger.debug("Found repeating energy sequence of length {} starting at step {}!", repeatingSequenceLength, repeatingStep);
                    }
                } else {
                    repeatingStep = time;
                    maxRepeatingSequenceLength = NumberUtils.max(repeatingSequenceLength, maxRepeatingSequenceLength);
                    repeatingSequenceLength = 1;

                    int followingSequenceLength = addEnergyToFollowingSequence(previousEnergy, energy, energySequenceMap);
                    totalEnergiesSaved++;
                    maxFollowingSequenceLength = NumberUtils.max(followingSequenceLength, maxFollowingSequenceLength);

                    logger.trace("Energy sequence starts repeating at step {} (current energy does not flow previous one)", repeatingStep);
                }
            } else {
                maxRepeatingSequenceLength = NumberUtils.max(repeatingSequenceLength, maxRepeatingSequenceLength);
                repeatingSequenceLength = 0;
                repeatingStep = -1;

                addNewEnergyToSequence(energy, energySequenceMap);
                if (previousEnergy != -1) {
                    int followingSequenceLength = addEnergyToFollowingSequence(previousEnergy, energy, energySequenceMap);
                    totalEnergiesSaved++;
                    maxFollowingSequenceLength = NumberUtils.max(followingSequenceLength, maxFollowingSequenceLength);
                }

                logger.trace("Energy sequence broken with length {} (current energy not found)", repeatingSequenceLength);
            }

            previousEnergy = energy;
            applyGravityToMoons();

            if (logConfig.isShowLogAfterSimulatingNSteps(time)) {
                logger.debug("Status after step {}: ", time);
                logger.debug("\tCurrent sequence length: {} (from step {})", repeatingSequenceLength, repeatingStep);
                logger.debug("\tMax repeating sequence length: {}", maxRepeatingSequenceLength);
                logger.debug("\tBiggest energy found: {}", maxEnergy);
                logger.debug("\tEnergy sequence map statistics: size={}, # energy values saved={}, max set length={}", energySequenceMap.size(), totalEnergiesSaved, maxFollowingSequenceLength);
                if (!repeatingStateFound) {
                    printEnergySequenceMap(energySequenceMap, Level.TRACE);
                }
            }

            time++;
        }

        logger.debug("Status after step {}: ", time-1);
        logger.debug("\tCurrent sequence length: {} (from step {})", repeatingSequenceLength, repeatingStep);
        logger.debug("\tMax repeating sequence length: {}", maxRepeatingSequenceLength);
        logger.debug("\tBiggest energy found: {}", maxEnergy);
        logger.debug("\tEnergy sequence map statistics: size={}, # energy values saved={}, max set length={}", energySequenceMap.size(), totalEnergiesSaved, maxFollowingSequenceLength);


        printEnergySequenceMap(energySequenceMap, Level.TRACE);
        logger.info("Process finished. Max repeating sequence length found: {}", maxRepeatingSequenceLength);

        return logger.traceExit(repeatingStateFound ? repeatingStep : -1);
    }

    public long simulateAndFindRepeatingStateV3(long maxSteps, int sequenceLength) {
        logger.traceEntry();

        Set<Short> hashSequenceSet = new HashSet<>();

        long repeatingStep = -1;
        long repeatingSequenceLength = 0;

        // for statistics
        long maxRepeatingSequenceLength = 0;

        boolean repeatingStateFound = false;
        for (long l = 0; l < maxSteps && !repeatingStateFound; l++) {
            short hash = calculateHash();
            logger.printf(Level.TRACE, "Hash for step %d: 0x%X", time, hash);

            if (hashSequenceSet.contains(hash)) {
                if (repeatingSequenceLength == 0) {
                    repeatingStep = time;
                }

                repeatingSequenceLength++;

                if (repeatingSequenceLength == sequenceLength) {
                    maxRepeatingSequenceLength = NumberUtils.max(repeatingSequenceLength, maxRepeatingSequenceLength);
                    repeatingStateFound = true;
                }
            } else {
                maxRepeatingSequenceLength = NumberUtils.max(repeatingSequenceLength, maxRepeatingSequenceLength);
                repeatingSequenceLength = 0;
                hashSequenceSet.add(hash);
            }

            applyGravityToMoons();

            if (logConfig.isShowLogAfterSimulatingNSteps(time)) {
                logger.debug("Max repeating steps found after step {}: {}", time, maxRepeatingSequenceLength);
            }

            time++;
        }

        return logger.traceExit(repeatingStateFound ? repeatingStep : -1);
    }

    private short calculateHash() {
        return (short) moons.hashCode();
    }

    private boolean isEnergyRepeated(int energy, Map<Integer, Set<Integer>> energySequenceMap) {
        return energySequenceMap.containsKey(energy);
    }

    private boolean doesEnergyFollowPreviousSequence(int previousEnergy, int currentEnergy, Map<Integer, Set<Integer>> energySequenceMap) {
        Set<Integer> followingEnergiesInSequence = energySequenceMap.get(previousEnergy);
        return followingEnergiesInSequence != null && followingEnergiesInSequence.contains(currentEnergy);
    }

    private void addNewEnergyToSequence(int energy, Map<Integer, Set<Integer>> energySequenceMap) {
        energySequenceMap.put(energy, new HashSet<>());
    }

    private int addEnergyToFollowingSequence(int previousEnergy, int currentEnergy, Map<Integer, Set<Integer>> energySequenceMap) {
        Set<Integer> followingInSequenceSet = energySequenceMap.get(previousEnergy);
        followingInSequenceSet.add(currentEnergy);
        return followingInSequenceSet.size();
    }

    private void printEnergySequenceMap(Map<Integer, Set<Integer>> energySequenceMap, Level level) {
        logger.log(level, "Energy sequence map content:");
        for (Map.Entry<Integer, Set<Integer>> sequenceItem : energySequenceMap.entrySet()) {
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
        moons.forEach(moon -> deltaVelocityMap.put(moon, Int3DCoord.getOrigin()));

        for (int i = 0; i < moons.size(); i++) {
            for (int j = i+1; j < moons.size(); j++) {
                Moon moon1 = moons.get(i);
                Moon moon2 = moons.get(j);

                updateVelocityDelta(deltaVelocityMap, moon1, moon2);
                updateVelocityDelta(deltaVelocityMap, moon2, moon1);
            }
        }

        moons.forEach(moon -> moon.applyVelocityChange(deltaVelocityMap.get(moon)));
    }

    private void updateVelocityDelta(Map<Moon, Int3DCoord> deltaVelocityMap, Moon moon, Moon otherMoon) {
        Int3DCoord velocityDelta = deltaVelocityMap.get(moon);
        Int3DCoord moonPosition = moon.getPosition();
        Int3DCoord otherMoonPosition = otherMoon.getPosition();

        velocityDelta.addToX(sign(otherMoonPosition.getX() - moonPosition.getX()));
        velocityDelta.addToY(sign(otherMoonPosition.getY() - moonPosition.getY()));
        velocityDelta.addToZ(sign(otherMoonPosition.getZ() - moonPosition.getZ()));
    }

    public int calculateTotalEnergy() {
        logger.traceEntry();
        return logger.traceExit(moons.stream().mapToInt(Moon::calculateTotalEnergy).sum());
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
