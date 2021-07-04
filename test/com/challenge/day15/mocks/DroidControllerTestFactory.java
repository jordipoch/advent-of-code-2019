package com.challenge.day15.mocks;

import com.challenge.day15.DroidController;
import com.challenge.day15.mocks.exception.DroidControllerMockCreationException;
import com.challenge.day15.mocks.exception.TestGridMapCreationException;

public class DroidControllerTestFactory {
    private static final DroidControllerTestFactory INSTANCE = new DroidControllerTestFactory();

    public static DroidControllerTestFactory getInstance() { return INSTANCE;}

    public DroidController createDroidController(String gridFileName) throws DroidControllerMockCreationException {
        try {
            return DroidControllerMock.createDroidControllerMock(gridFileName);
        } catch (TestGridMapCreationException e) {
            throw new DroidControllerMockCreationException(e);
        }
    }
}
