package com.challenge.day12;

import static com.challenge.day12.Moon.createMoon;
import static com.challenge.day12.SaturnMoonSystemSnapshot.createSaturnMoonSystemSnapshot;
import static com.challenge.day12.SaturnMoonSystem.LogConfiguration.createLogConfiguration;

import com.challenge.library.geometry.model.Dimension;
import com.challenge.library.geometry.model.Int3DCoord;
import static com.challenge.library.utils.NumberUtils.lcm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public long simulateAndFindRepeatingState(long maxSteps) {
        logger.traceEntry();

        SaturnMoonSystemSnapshot snapshotAtTime0 = createSaturnMoonSystemSnapshot(moons, time);
        RepeatingStateStatus status = new RepeatingStateStatus();

        logger.debug(this::toString);

        for (long i = 0; i < maxSteps && !status.isRepeatingStateFound(); i++) {
            applyGravityToMoons();
            time++;

            checkRepeatingStatus(snapshotAtTime0, status);

            if (logConfig.isShowLogAfterSimulatingNSteps(time)) {
                logger.debug("Status at step {}:", time);
                logger.debug("Repeating status {}:", status);
                logger.debug(this::toString);
            }

            if (status.isRepeatingStateFound()) {
                logger.info("repeating state found: {} ", status.getRepeatingStateNum());
                logger.debug(status);
                return logger.traceExit(status.getRepeatingStateNum());
            }
        }

        logger.info("repeating state not found!");
        logger.debug(status);

        return logger.traceExit(-1L);
    }

    private void checkRepeatingStatus(SaturnMoonSystemSnapshot snapshot, RepeatingStateStatus status) {
        if (!status.isXDimFound()) {
            boolean found = areMoonsEqualIn1Dimension(snapshot, Dimension.X);
            if (found) {
                status.setXDimFound(true);
                status.setRepStepXDim(time);

                logger.debug("Matching step found for dimension {} at step {}", Dimension.X, time);
            }
        }

        if (!status.isYDimFound()) {
            boolean found = areMoonsEqualIn1Dimension(snapshot, Dimension.Y);
            if (found) {
                status.setYDimFound(true);
                status.setRepStepYDim(time);

                logger.debug("Matching step found for dimension {} at step {}", Dimension.Y, time);
            }
        }

        if (!status.isZDimFound()) {
            boolean found = areMoonsEqualIn1Dimension(snapshot, Dimension.Z);
            if (found) {
                status.setZDimFound(true);
                status.setRepStepZDim(time);

                logger.debug("Matching step found for dimension {} at step {}", Dimension.Z, time);
            }
        }
    }

    private boolean areMoonsEqualIn1Dimension(SaturnMoonSystemSnapshot snapshot, Dimension dimension) {
        List<Integer> systemPosDimList = getMoonsDimensionValues(moons, dimension, Moon::getPosition);
        List<Integer> snapshotPosDimList = getMoonsDimensionValues(snapshot.getMoons(), dimension, Moon::getPosition);

        List<Integer> systemVelDimList = getMoonsDimensionValues(moons, dimension, Moon::getVelocity);
        List<Integer> snapshotVelDimList = getMoonsDimensionValues(snapshot.getMoons(), dimension, Moon::getVelocity);

        return systemPosDimList.equals(snapshotPosDimList) && systemVelDimList.equals(snapshotVelDimList);
    }

    private List<Integer> getMoonsDimensionValues(List<Moon> moons, Dimension dimension, Function<Moon, Int3DCoord> coordFunction) {
        return moons.stream()
                .map(coordFunction)
                .map(coord -> coord.getDimensionValue(dimension))
                .collect(Collectors.toList());
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

    private static class RepeatingStateStatus {
        boolean xDimFound;
        boolean yDimFound;
        boolean zDimFound;
        long repStepXDim = -1;
        long repStepYDim = -1;
        long repStepZDim = -1;

        boolean isXDimFound() {
            return xDimFound;
        }

        void setXDimFound(boolean xDimFound) {
            this.xDimFound = xDimFound;
        }

        boolean isYDimFound() {
            return yDimFound;
        }

        void setYDimFound(boolean yDimFound) {
            this.yDimFound = yDimFound;
        }

        boolean isZDimFound() {
            return zDimFound;
        }

        void setZDimFound(boolean zDimFound) {
            this.zDimFound = zDimFound;
        }

        long getRepStepXDim() {
            return repStepXDim;
        }

        void setRepStepXDim(long repStepXDim) {
            this.repStepXDim = repStepXDim;
        }

        long getRepStepYDim() {
            return repStepYDim;
        }

        void setRepStepYDim(long repStepYDim) {
            this.repStepYDim = repStepYDim;
        }

        long getRepStepZDim() {
            return repStepZDim;
        }

        void setRepStepZDim(long repStepZDim) {
            this.repStepZDim = repStepZDim;
        }

        public boolean isRepeatingStateFound() {
            return xDimFound && yDimFound && zDimFound;
        }

        public long getRepeatingStateNum() {
            return lcm(repStepXDim, repStepYDim, repStepZDim);
        }

        @Override
        public String toString() {
            return "RepeatingStateStatus{" +
                    "repStepXDim=" + repStepXDim +
                    ", repStepYDim=" + repStepYDim +
                    ", repStepZDim=" + repStepZDim +
                    '}';
        }
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
