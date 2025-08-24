package org.example.parser;

import org.example.lexer.TokenType;
import static org.example.lexer.TokenTypes.BasicTokenType;
import static org.example.lexer.TokenTypes.AssignTokenType;

/**
 * Assignment operator
 */
public enum AssignmentOperator {
    ASSIGN(BasicTokenType.ASSIGN),
    PLUS_ASSIGN(AssignTokenType.PLUS_ASSIGN),
    MINUS_ASSIGN(AssignTokenType.MINUS_ASSIGN),
    MULTIPLY_ASSIGN(AssignTokenType.MULTIPLY_ASSIGN),
    DIVIDE_ASSIGN(AssignTokenType.DIVIDE_ASSIGN);
    
    private final TokenType tokenType;
    
    AssignmentOperator(TokenType tokenType) {
        this.tokenType = tokenType;
    }
    
    public TokenType getTokenType() {
        return tokenType;
    }
    
    /**
     * Convert TokenType to AssignmentOperator
     * @param tokenType the token type to convert
     * @return corresponding AssignmentOperator
     * @throws RuntimeException if tokenType is not an assignment operator
     */
    public static AssignmentOperator fromTokenType(TokenType tokenType) {
        for (AssignmentOperator op : values()) {
            if (op.tokenType == tokenType) {
                return op;
            }
        }
        throw new RuntimeException("Not an assignment operator: " + tokenType);
    }
    
    /**
     * Check if TokenType represents an assignment operator
     * @param tokenType the token type to check
     * @return true if it's an assignment operator, false otherwise
     */
    public static boolean isAssignmentOperator(TokenType tokenType) {
        for (AssignmentOperator op : values()) {
            if (op.tokenType == tokenType) {
                return true;
            }
        }
        return false;
    }
}
