package com.challenge.day15;

import com.challenge.day15.exception.DroidControllerCreationException;

import java.io.IOException;
import java.nio.file.Path;

public class DroidControllerFactory {
    private static final DroidControllerFactory INSTANCE = new DroidControllerFactory();

    public static DroidControllerFactory getInstance() { return INSTANCE;}

    public DroidController createDroidController(Path basePath, String file) throws DroidControllerCreationException {
        try {
            return RealDroidController.Builder.createDroidController().withCodeFile(basePath, file).build();
        } catch (IOException e) {
            throw new DroidControllerCreationException(basePath, file, e);
        }
    }

    public DroidController createDroidController(Droid droid) {
        return RealDroidController.Builder.createDroidController().withDroid(droid).build();
    }

    public DroidController createDroidController(Droid droid, Grid grid) {
        return RealDroidController.Builder.createDroidController().withDroid(droid).withGrid(grid).build();
    }
}
