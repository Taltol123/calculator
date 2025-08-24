package org.example.parser;

import org.example.lexer.TokenType;

import static org.example.lexer.TokenTypes.BasicTokenType;

/**
 * Binary operators with built-in precedence and type safety
 */
public enum BinaryOperator {
    // Highest precedence (evaluated first) - lower numbers = higher precedence

    // Higher precedence  
    MULTIPLY(1, BasicTokenType.MULTIPLY),
    DIVIDE(1, BasicTokenType.DIVIDE),

    // Lower precedence (evaluated later)  
    PLUS(2, BasicTokenType.PLUS),
    MINUS(2, BasicTokenType.MINUS);

    private final int precedence;
    private final TokenType tokenType;

    BinaryOperator(int precedence, TokenType tokenType) {
        this.precedence = precedence;
        this.tokenType = tokenType;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Convert TokenType to BinaryOperator
     *
     * @param tokenType the token type to convert
     * @return corresponding BinaryOperator
     * @throws RuntimeException if tokenType is not a binary operator
     */
    public static BinaryOperator fromTokenType(TokenType tokenType) {
        for (BinaryOperator op : values()) {
            if (op.tokenType == tokenType) {
                return op;
            }
        }
        throw new RuntimeException("Not a binary operator: " + tokenType);
    }

    /**
     * Check if TokenType represents a binary operator
     *
     * @param tokenType the token type to check
     * @return true if it's a binary operator, false otherwise
     */
    public static boolean isBinaryOperator(TokenType tokenType) {
        for (BinaryOperator op : values()) {
            if (op.tokenType == tokenType) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get precedence for a TokenType
     *
     * @param tokenType the token type
     * @return precedence value (lower = higher precedence)
     */
    public static int getPrecedence(TokenType tokenType) {
        return fromTokenType(tokenType).precedence;
    }
}
