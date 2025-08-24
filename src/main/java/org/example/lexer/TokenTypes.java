package org.example.lexer;

/**
 * Consolidated token types for all calculator types
 * Simple enums without unnecessary complexity
 */
public class TokenTypes {

    /**
     * Core tokens that ALL calculators need
     */
    public enum CoreTokenType implements TokenType {
        NUMBER, IDENTIFIER, LPAREN, RPAREN, EOF
    }

    /**
     * Basic arithmetic tokens for simple calculators
     */
    public enum BasicTokenType implements TokenType {
        PLUS, MINUS, MULTIPLY, DIVIDE, ASSIGN
    }

    /**
     * Assignment tokens for calculators that handle assignments and increments
     */
    public enum AssignTokenType implements TokenType {
        INCREMENT, DECREMENT, PLUS_ASSIGN, MINUS_ASSIGN,
        MULTIPLY_ASSIGN, DIVIDE_ASSIGN, EQUAL, NOT_EQUAL
    }
}
