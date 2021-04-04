package com.challenge.day14.exception;

public class FuelComputationHelperException extends Exception {
    private final ErrorType errorType;
    private final long oreQtty;

    public FuelComputationHelperException(ErrorType errorType, long oreQtty) {
        this.errorType = errorType;
        this.oreQtty = oreQtty;
    }

    @Override
    public String getMessage() {
        return String.format("%s. oreQtty=%d", errorType.getMessage(), oreQtty);
    }

    public enum ErrorType {
        REPEATED_ORE_QTTY ("The ORE quantity is the same as the previous");

        ErrorType(String message) {
            this.message = message;
        }

        private final String message;

        public String getMessage() {
            return message;
        }
    }
}
