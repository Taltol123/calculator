package org.example.calculator;


/**
 * Assignment Calculator - handles basic arithmetic + assignment operations
 */
public class AssignmentCalculator extends Calculator {

    public AssignmentCalculator() {
        super();
    }

    @Override
    protected void configureParsers() {
        factorParserConfigurator.configureAssignment(factorParserRegistry);
    }

    @Override
    protected void configureOperators() {

        // Register assignment operators first (longer patterns)
        CalculatorConfiguration.configureAssignmentOperators(operatorRegistry);

        // Register basic arithmetic operators
        CalculatorConfiguration.configureBasicOperators(operatorRegistry);
    }

    @Override
    protected void configureOperations() {
        // Register basic arithmetic operations
        CalculatorConfiguration.configureBasicOperations(operationRegistry);

        // Register assignment operations
        CalculatorConfiguration.configureAssignmentOperations(operationRegistry);
    }
}
