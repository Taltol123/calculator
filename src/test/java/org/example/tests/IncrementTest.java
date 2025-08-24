package org.example.tests;

import org.example.calculator.AssignmentCalculator;
import org.example.calculator.Calculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class IncrementTest {

    @Test
    void testIncrementOperations() {
        Calculator calc = new AssignmentCalculator();

        // Define test cases
        List<TestCase> testCases = Arrays.asList(
                new TestCase(
                        Arrays.asList("i = 0", "j = ++i", "x = i++ + 5", "y = (5 + 3) * 10", "i += y"),
                        "(i=82,j=1,x=6,y=80)"
                ),
                new TestCase(
                        Arrays.asList("a = 10", "b = a++", "c = ++a", "a += c"),
                        "(a=24,b=10,c=12)"
                ),
                new TestCase(
                        Arrays.asList("m = 5", "n = m++ + ++m", "m *= 2", "n += m"),
                        "(m=14,n=26)"
                ),
                new TestCase(
                        Arrays.asList("x = 1", "y = 2", "z = x++ + y++ + ++x + ++y"),
                        "(x=3,y=4,z=10)"
                )
        );

        // Run all test cases
        for (int i = 0; i < testCases.size(); i++) {
            TestCase test = testCases.get(i);
            System.out.println("=== Test Case " + (i + 1) + " ===");
            runTest(calc, test.expressions, test.expected);
            System.out.println();
        }
    }

    private void runTest(Calculator calc, List<String> expressions, String expected) {
        System.out.println("Input expressions:");
        for (String expr : expressions) {
            System.out.println("  " + expr);
        }

        try {
            String result = calc.processExpressions(expressions);
            System.out.println("\nActual output:");
            System.out.println(result);

            System.out.println("\nExpected output:");
            System.out.println(expected);

            assertEquals(expected, result, "Test case failed");
            System.out.println("\nTEST PASSED!");
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage(), e);
        }
        System.out.println("=".repeat(50));
    }

    // Small helper class to hold test case data
    private static class TestCase {
        List<String> expressions;
        String expected;

        TestCase(List<String> expressions, String expected) {
            this.expressions = expressions;
            this.expected = expected;
        }
    }
}
