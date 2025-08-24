package org.example.operations;

import org.example.errors.ErrorMessages;
import org.example.lexer.TokenType;
import java.util.HashMap;
import java.util.Map;

// Registry for operations - allows extension without modification
public class OperationRegistry {
    private final Map<TokenType, BinaryOperation> binaryOperations = new HashMap<>();
    private final Map<TokenType, AssignmentOperation> assignmentOperations = new HashMap<>();
    
    public void registerBinaryOperation(TokenType tokenType, BinaryOperation operation) {
        binaryOperations.put(tokenType, operation);
    }
    
    public void registerAssignmentOperation(TokenType tokenType, AssignmentOperation operation) {
        assignmentOperations.put(tokenType, operation);
    }
    
    public BinaryOperation getBinaryOperation(TokenType tokenType) {
        BinaryOperation operation = binaryOperations.get(tokenType);
        if (operation == null) {
            throw new RuntimeException(ErrorMessages.UNKNOWN_OPERATOR.getMessage() + tokenType);
        }
        return operation;
    }
    
    public AssignmentOperation getAssignmentOperation(TokenType tokenType) {
        AssignmentOperation operation = assignmentOperations.get(tokenType);
        if (operation == null) {
            throw new RuntimeException(ErrorMessages.UNKNOWN_OPERATOR.getMessage() + tokenType);
        }
        return operation;
    }
}
