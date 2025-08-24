package org.example.parser;

import org.example.lexer.Token;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import org.example.errors.ErrorMessages;

/**
 * Simple registry for factor parsers
 */
public class FactorParserRegistry {
    private final List<FactorParser> parsers = new ArrayList<>();
    private boolean needsSorting = false;

    /**
     * Register a new factor parser
     */
    public void register(FactorParser parser) {
        if (parser == null) {
            throw new IllegalArgumentException(ErrorMessages.ERROR_NULL_FACTOR_PARSER.getMessage());
        }

        parsers.add(parser);
        needsSorting = true;
    }

    /**
     * Clear all registered parsers
     */
    public void clear() {
        parsers.clear();
        needsSorting = false;
    }

    /**
     * Find and use the appropriate parser for the current token context
     */
    public ASTNode parse(ParseContext context) {
        Token currentToken = context.getCurrentToken();
        List<Token> tokens = context.getTokens();
        int currentIndex = context.getCurrentTokenIndex();

        // Try each parser in priority order (highest priority first)
        for (FactorParser parser : parsers) {
            if (parser.canParse(currentToken, tokens, currentIndex)) {
                return parser.parse(context);
            }
        }

        // No parser found
        throw new RuntimeException(ErrorMessages.UNEXPECTED_TOKEN.getMessage() + currentToken.type + " at position " + currentToken.position);
    }

    /**
     * Get count of registered parsers (for testing/debugging)
     */
    public int getRegisteredCount() {
        return parsers.size();
    }
}