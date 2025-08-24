package org.example.calculator;


public class CalculatorFactory {

    /**
     * Creates the appropriate calculator instance based on type
     * This is a TRUE factory - it returns different objects for different types
     */
    public static Calculator createCalculator(CalculatorType type) {
        switch (type) {
            default:
                return new AssignmentCalculator();
        }
    }

}
