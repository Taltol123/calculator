package org.example.parser;

import org.example.lexer.Token;
import org.example.lexer.TokenType;

import java.util.List;

/**
 * This encapsulates the parsing state and common operations
 */
public class ParseContext {
    private final List<Token> tokens;
    private int currentTokenIndex;
    private Token currentToken;
    private final Parser parentParser;

    public ParseContext(List<Token> tokens, int currentTokenIndex, Token currentToken, Parser parentParser) {
        this.tokens = tokens;
        this.currentTokenIndex = currentTokenIndex;
        this.currentToken = currentToken;
        this.parentParser = parentParser;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public int getCurrentTokenIndex() {
        return currentTokenIndex;
    }

    public void advance() {
        currentTokenIndex++;
        if (currentTokenIndex < tokens.size()) {
            currentToken = tokens.get(currentTokenIndex);
        }
    }

    public void expect(TokenType expectedType) {
        if (currentToken.type != expectedType) {
            throw new RuntimeException("Expected " + expectedType + " but got " + currentToken.type + " at position " + currentToken.position);
        }
        advance();
    }

    public ASTNode parseExpression() {
        return parentParser.parseExpression();
    }
}
