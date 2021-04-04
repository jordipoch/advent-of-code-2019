package com.challenge.day14;

import com.challenge.day14.exception.FuelComputationHelperException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class FuelComputationHelper {
    private static final Logger logger = LogManager.getLogger();

    private long currentFuelQtty;
    private long previousFuelQtty;
    private final long oreQttyAvailable;
    private long previousOreQtty;
    private long offset;
    private boolean lastCheckOreWasBelow = false;

    public FuelComputationHelper(long initialFuelQtty, long oreQttyAvailable) {
        this.currentFuelQtty = initialFuelQtty;
        this.oreQttyAvailable = oreQttyAvailable;

        this.offset = this.currentFuelQtty;
        this.previousFuelQtty = this.currentFuelQtty;
    }

    public boolean updateFuelAmountsAndCheck(long oreQtty) throws FuelComputationHelperException {
        if (oreQtty == previousOreQtty) {
            throw new FuelComputationHelperException(FuelComputationHelperException.ErrorType.REPEATED_ORE_QTTY, oreQtty);
        }

        if (oreQtty == oreQttyAvailable) {
            return true;
        }

        /*if (previousOreQtty == 0) {
            offset = (long) ((currentFuelQtty / (oreQttyAvailable / (Math.abs(oreQttyAvailable - oreQtty)))) * 1.5);
            logger.info("initial offset: {}", offset);
        }*/

        boolean found = false;
        if (oreQtty > oreQttyAvailable) {
            if (offset == 1) {
                found = withOffset1AndOreQttyAbove();
            } else {
                offset /= 2;
                addFuelQtty(-offset);
            }
            lastCheckOreWasBelow = false;
        } else { // <
            if (offset == 1) {
                found = withOffset1AndOreQttyBelow();
            } else {
                offset /= 2;
                addFuelQtty(offset);
            }
            lastCheckOreWasBelow = true;
        }

        previousOreQtty = oreQtty;

        return found;
    }

    private boolean withOffset1AndOreQttyBelow() {
        if (lastCheckOreWasBelow) {
            addFuelQtty(1L);
            return false;
        } else {
            return true;
        }
    }

    private boolean withOffset1AndOreQttyAbove() {
        if (lastCheckOreWasBelow) {
            currentFuelQtty = previousFuelQtty;
            return true;
        } else {
            addFuelQtty(-1L);
            return false;
        }
    }

    private void addFuelQtty(long qtty) {
        previousFuelQtty = currentFuelQtty;
        currentFuelQtty += qtty;
    }

    public long getCurrentFuelQtty() {
        return currentFuelQtty;
    }

    @Override
    public String toString() {
        return "FuelComputationHelper{" +
                "currentFuelQtty=" + currentFuelQtty +
                ", previousFuelQtty=" + previousFuelQtty +
                ", offset=" + offset +
                ", lastCheckOreWasBelow=" + lastCheckOreWasBelow +
                '}';
    }
}
