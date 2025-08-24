package org.example.parser;

import org.example.lexer.TokenType;
import org.example.operations.*;
import org.example.calculator.VariableStore;

// AST Node hierarchy - All node types in one file for better cohesion
// Base ASTNode class is now in ASTNode.java

class NumberNode extends ASTNode {
    private final int value;

    public NumberNode(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(VariableStore store, OperationRegistry operationRegistry) {
        return value;
    }
}

class VariableNode extends ASTNode {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(VariableStore store, OperationRegistry operationRegistry) {
        return store.getValue(name);
    }

    public String getName() {
        return name;
    }
}

class BinaryOpNode extends ASTNode {
    private final ASTNode left;
    private final ASTNode right;
    private final BinaryOperator operator;

    public BinaryOpNode(ASTNode left, TokenType tokenType, ASTNode right) {
        this.left = left;
        this.operator = BinaryOperator.fromTokenType(tokenType);  // Convert TokenType to BinaryOperator
        this.right = right;
    }

    @Override
    public int evaluate(VariableStore store, OperationRegistry operationRegistry) {
        int leftVal = left.evaluate(store, operationRegistry);
        int rightVal = right.evaluate(store, operationRegistry);

        BinaryOperation operation = operationRegistry.getBinaryOperation(operator.getTokenType());
        return operation.execute(leftVal, rightVal);
    }

    public BinaryOperator getOperator() {
        return operator;
    }
}

class AssignmentNode extends ASTNode {
    private final String variable;
    private final ASTNode expression;
    private final AssignmentOperator assignOperator;

    public AssignmentNode(String variable, AssignmentOperator assignOperator, ASTNode expression) {
        this.variable = variable;
        this.assignOperator = assignOperator;  // Direct assignment, no conversion needed
        this.expression = expression;
    }

    @Override
    public int evaluate(VariableStore store, OperationRegistry operationRegistry) {
        int value = expression.evaluate(store, operationRegistry);
        int current = store.getValue(variable);

        AssignmentOperation operation = operationRegistry.getAssignmentOperation(assignOperator.getTokenType());
        int newValue = operation.execute(current, value);

        store.assign(variable, newValue);
        return newValue;
    }

    public AssignmentOperator getAssignmentOperator() {
        return assignOperator;
    }
}

class PreIncrementNode extends ASTNode {
    private final String variable;
    private final boolean isIncrement;

    public PreIncrementNode(String variable, boolean isIncrement) {
        this.variable = variable;
        this.isIncrement = isIncrement;
    }

    @Override
    public int evaluate(VariableStore store, OperationRegistry operationRegistry) {
        int current = store.getValue(variable);
        int newValue = isIncrement ? current + 1 : current - 1;
        store.assign(variable, newValue);
        return newValue;
    }
}

class PostIncrementNode extends ASTNode {
    private final String variable;
    private final boolean isIncrement;

    public PostIncrementNode(String variable, boolean isIncrement) {
        this.variable = variable;
        this.isIncrement = isIncrement;
    }

    @Override
    public int evaluate(VariableStore store, OperationRegistry operationRegistry) {
        int current = store.getValue(variable);
        int newValue = isIncrement ? current + 1 : current - 1;
        store.assign(variable, newValue);
        return current; // Return original value for post-increment
    }
}

