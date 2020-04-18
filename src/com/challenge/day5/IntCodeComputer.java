package com.challenge.day5;

import com.challenge.day5.exception.EndOfCodeException;
import com.challenge.day5.exception.ExecutionException;
import com.challenge.day5.exception.TestComputationException;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import static com.challenge.day5.DiagnosticTestRunner.Builder.createDiagnosticTestRunner;

public class IntCodeComputer {
    private long[] code;
    private long input;
    private int position;

    private IntCodeComputer(long[] code, long input) {
        this.code = code;
        this.input = input;
    }

    public List<Long> computeTests() throws TestComputationException {
        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        List<Long> testResults = new ArrayList<>();

        try {
            diagnosticTestRunner.setInput(input);

            OptionalLong result;
            do {
                result = diagnosticTestRunner.runDiagnosticTest();
                if (result.isPresent())
                    testResults.add(result.getAsLong());
            } while (result.isPresent());
        } catch (ExecutionException e) {
            throw new TestComputationException("Error computing tests: " + e.getMessage(), e);
        } catch (EndOfCodeException e) {
            throw new TestComputationException("Unexpected end of code", e);
        }

        return testResults;
    }

    public static class Builder {
        private long[] code;
        private long input;

        private Builder(long[] code) {
            this.code = code;
        }

        public static Builder createIntCodeComputer(long[] code) {
            return new Builder(code);
        }

        public Builder withInput(long input) {
            this.input = input;
            return this;
        }

        public IntCodeComputer build() {
            return new IntCodeComputer(this.code, this.input);
        }
    }
}
