package com.challenge.day5;

import com.challenge.day5.exception.EndOfCodeException;
import com.challenge.day5.exception.ExecutionException;
import static com.challenge.day5.DiagnosticTestRunner.Builder.*;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.OptionalLong;

import static org.testng.Assert.*;

public class DiagnosticTestRunnerTest {
    @Test
    public void testRunInputOk() throws ExecutionException, EndOfCodeException {
        long[] code = {3, 3, 0, 9};
        final long inputValue = 1;

        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        diagnosticTestRunner.setInput(1);

        assertEquals(code[3], inputValue);
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.ExecutionException.class})
    public void testRunInputKo_InvalidOperation() throws ExecutionException, EndOfCodeException  {
        long[] code = {2, 3, 0, 9};
        final long inputValue = 1;

        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        diagnosticTestRunner.setInput(inputValue);
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.ExecutionException.class})
    public void testRunInputKo_InvalidPosition() throws ExecutionException, EndOfCodeException  {
        long[] code = {3, 4, 0, 9};
        final long inputValue = 1;

        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        diagnosticTestRunner.setInput(inputValue);
    }

    @Test
    public void testRunDiagnosticTestOk_AddImmediateMode() throws ExecutionException, EndOfCodeException  {
        long[] code = {1101, 6, 7, 8, 4, 8, 10, 5, 0};
        System.out.println("Code before running diagnostic test: " + Arrays.toString(code));

        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        OptionalLong output = diagnosticTestRunner.runDiagnosticTest();

        assertTrue(output.isPresent());
        assertEquals(output.getAsLong(), 13L);
        assertTrue(Arrays.equals(code, new long[] {1101, 6, 7, 8, 4, 8, 10, 5, 13}));

        System.out.println("Code after running diagnostic test: " + Arrays.toString(code));
    }

    @Test
    public void testRunDiagnosticTestOk_AddPositionMode() throws ExecutionException, EndOfCodeException  {
        long[] code = {1, 6, 7, 8, 4, 8, 10, 5, 0};
        System.out.println("Code before running diagnostic test: " + Arrays.toString(code));

        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        OptionalLong output = diagnosticTestRunner.runDiagnosticTest();

        assertTrue(output.isPresent());
        assertEquals(output.getAsLong(), 15L);
        assertTrue(Arrays.equals(code, new long[] {1, 6, 7, 8, 4, 8, 10, 5, 15}));

        System.out.println("Code after running diagnostic test: " + Arrays.toString(code));
    }

    @Test
    public void testRunDiagnosticTestOk_MultiplyMixedMode() throws ExecutionException, EndOfCodeException  {
        long[] code = {1002, 6, 7, 8, 4, 8, 10, 5, 0};
        System.out.println("Code before running diagnostic test: " + Arrays.toString(code));

        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        OptionalLong output = diagnosticTestRunner.runDiagnosticTest();

        assertTrue(output.isPresent());
        assertEquals(output.getAsLong(), 70L);
        assertTrue(Arrays.equals(code, new long[] {1002, 6, 7, 8, 4, 8, 10, 5, 70}));

        System.out.println("Code after running diagnostic test: " + Arrays.toString(code));
    }

    @Test
    public void testRunDiagnosticTestOk_OutputImmediateMode() throws ExecutionException, EndOfCodeException  {
        long[] code = {1101, 6, 7, 5, 104, 0, 10, 5};
        System.out.println("Code before running diagnostic test: " + Arrays.toString(code));

        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        OptionalLong output = diagnosticTestRunner.runDiagnosticTest();

        assertTrue(output.isPresent());
        assertEquals(output.getAsLong(), 13L);
        assertTrue(Arrays.equals(code, new long[] {1101, 6, 7, 5, 104, 13, 10, 5}));

        System.out.println("Code after running diagnostic test: " + Arrays.toString(code));
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.ExecutionException.class})
    public void testRunDiagnosticTestKo_UnexpectedHaltInstruction() throws ExecutionException, EndOfCodeException  {
        long[] code = {1, 6, 7, 8, 99, 8, 10, 5, 0};

        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        diagnosticTestRunner.runDiagnosticTest();
    }

    @Test(expectedExceptions = {com.challenge.day5.exception.ExecutionException.class})
    public void testRunDiagnosticTestKo_UnexpectedInputInstruction() throws ExecutionException, EndOfCodeException  {
        long[] code = {1, 6, 7, 8, 3, 8, 10, 5, 0};

        DiagnosticTestRunner diagnosticTestRunner = createDiagnosticTestRunner(code).build();
        diagnosticTestRunner.runDiagnosticTest();
    }
}
