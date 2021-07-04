package com.challenge.day15;

import com.challenge.day15.exception.DroidEngineException;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;

import static com.challenge.day15.Droid.Builder.createDroid;

public class RealDroidController extends AbstractDroidController {
    private Droid droid;

    private RealDroidController(Grid grid, Droid droid) {
        super(grid);
        this.droid = droid;
    }

    @Override
    protected MovementResult doMoveDroid(DroidDirection direction) throws DroidEngineException {
        return droid.move(direction);
    }

    public static class Builder {
        private Droid droid;
        private Grid grid;

        private Builder() {
        }

        public static Builder createDroidController() {
            return new Builder();
        }

        public Builder withDroidCode(BigInteger[] droidCode) {
            droid = createDroid().withEngineCode(droidCode).build();
            return this;
        }

        public Builder withCodeFile(Path basePath, String file) throws IOException {
            droid = createDroid().withEngineCodeFile(basePath, file).build();
            return this;
        }

        public Builder withDroid(Droid droid) {
            this.droid = droid;
            return this;
        }

        public Builder withGrid(Grid grid) {
            this.grid = grid;
            return this;
        }

        public RealDroidController build() {
            return new RealDroidController(grid != null ? grid : new Grid(), droid);
        }
    }
}
