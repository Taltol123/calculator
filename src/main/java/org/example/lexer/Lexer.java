package org.example.lexer;

import static org.example.lexer.TokenTypes.CoreTokenType;

import java.util.*;

// Lexer for tokenizing input
public class Lexer {
    private final String input;
    private int position;
    private char currentChar;
    private final OperatorRegistry operatorRegistry;

    public Lexer(String input, OperatorRegistry operatorRegistry) {
        this.input = input;
        this.operatorRegistry = operatorRegistry;
        this.position = 0;
        this.currentChar = position < input.length() ? input.charAt(position) : '\0';
    }

    private void advance() {
        position++;
        currentChar = position < input.length() ? input.charAt(position) : '\0';
    }

    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private String readNumber() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }

    private boolean isValidIdentifierChar(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '_';
    }

    private boolean isValidIdentifierStart(char c) {
        return Character.isAlphabetic(c) || c == '_';
    }

    private String readIdentifier() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && isValidIdentifierChar(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (currentChar != '\0') {
            skipWhitespace();
            if (currentChar == '\0') break;

            int tokenStart = position;

            if (Character.isDigit(currentChar)) {
                String number = readNumber();
                tokens.add(new Token(CoreTokenType.NUMBER, number, tokenStart));
            } else if (isValidIdentifierStart(currentChar)) {
                String identifier = readIdentifier();
                tokens.add(new Token(CoreTokenType.IDENTIFIER, identifier, tokenStart));
            } else {
                // Try to recognize an operator using the instance registry
                Token operatorToken = operatorRegistry.recognizeOperator(input, position);
                if (operatorToken != null) {
                    // Advance position by the length of the matched operator
                    for (int i = 0; i < operatorToken.length; i++) {
                        advance();
                    }
                    tokens.add(operatorToken);
                } else if (currentChar == '(') {
                    advance();
                    tokens.add(new Token(CoreTokenType.LPAREN, "(", tokenStart));
                } else if (currentChar == ')') {
                    advance();
                    tokens.add(new Token(CoreTokenType.RPAREN, ")", tokenStart));
                } else {
                    throw new RuntimeException("Unexpected character: " + currentChar + " at position " + position);
                }
            }
        }

        tokens.add(new Token(CoreTokenType.EOF, "", position));
        return tokens;
    }

}
