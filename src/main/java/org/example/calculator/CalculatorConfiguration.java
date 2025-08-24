package org.example.calculator;

import org.example.lexer.*;

import static org.example.lexer.TokenTypes.BasicTokenType;
import static org.example.lexer.TokenTypes.AssignTokenType;

import org.example.operations.*;
import org.example.errors.ErrorMessages;

/**
 * Abstract base class for configuring different types of calculators.
 * Provides methods to set up common operators and operations.
 */
public abstract class CalculatorConfiguration {

    /**
     * Configure basic arithmetic operators that are common to all calculators
     */
    protected static void configureBasicOperators(OperatorRegistry operatorRegistry) {
        operatorRegistry.register("+", BasicTokenType.PLUS);
        operatorRegistry.register("-", BasicTokenType.MINUS);
        operatorRegistry.register("*", BasicTokenType.MULTIPLY);
        operatorRegistry.register("/", BasicTokenType.DIVIDE);
        operatorRegistry.register("=", BasicTokenType.ASSIGN);
    }

    /**
     * Configure basic arithmetic operations that are common to all calculators
     */
    protected static void configureBasicOperations(OperationRegistry operationRegistry) {
        operationRegistry.registerBinaryOperation(BasicTokenType.PLUS, (left, right) -> left + right);
        operationRegistry.registerBinaryOperation(BasicTokenType.MINUS, (left, right) -> left - right);
        operationRegistry.registerBinaryOperation(BasicTokenType.MULTIPLY, (left, right) -> left * right);
        operationRegistry.registerBinaryOperation(BasicTokenType.DIVIDE, (left, right) -> {
            if (right == 0) throw new RuntimeException(ErrorMessages.DIVISION_BY_ZERO.getMessage());
            return left / right;
        });
        operationRegistry.registerAssignmentOperation(BasicTokenType.ASSIGN, (current, value) -> value);
    }

    /**
     * Configure assignment operators (increment, decrement, compound assignments)
     */
    protected static void configureAssignmentOperators(OperatorRegistry operatorRegistry) {
        operatorRegistry.register("++", AssignTokenType.INCREMENT);
        operatorRegistry.register("--", AssignTokenType.DECREMENT);
        operatorRegistry.register("+=", AssignTokenType.PLUS_ASSIGN);
        operatorRegistry.register("-=", AssignTokenType.MINUS_ASSIGN);
        operatorRegistry.register("*=", AssignTokenType.MULTIPLY_ASSIGN);
        operatorRegistry.register("/=", AssignTokenType.DIVIDE_ASSIGN);
        operatorRegistry.register("==", AssignTokenType.EQUAL);
        operatorRegistry.register("!=", AssignTokenType.NOT_EQUAL);
    }

    /**
     * Configure assignment operations
     */
    protected static void configureAssignmentOperations(OperationRegistry operationRegistry) {
        operationRegistry.registerAssignmentOperation(AssignTokenType.PLUS_ASSIGN, (current, value) -> current + value);
        operationRegistry.registerAssignmentOperation(AssignTokenType.MINUS_ASSIGN, (current, value) -> current - value);
        operationRegistry.registerAssignmentOperation(AssignTokenType.MULTIPLY_ASSIGN, (current, value) -> current * value);
        operationRegistry.registerAssignmentOperation(AssignTokenType.DIVIDE_ASSIGN, (current, value) -> {
            if (value == 0) throw new RuntimeException(ErrorMessages.DIVISION_BY_ZERO.getMessage());
            return current / value;
        });
    }
}
