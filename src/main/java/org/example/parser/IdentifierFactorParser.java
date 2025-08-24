package org.example.parser;

// Same package, no import needed
import org.example.lexer.Token;

import static org.example.lexer.TokenTypes.CoreTokenType;
import static org.example.lexer.TokenTypes.AssignTokenType;
import java.util.List;

/**
 * Parses identifiers and related constructs:
 * - Simple variables (e.g., "x", "variable")
 * - Function calls (e.g., "sin(x)", "sqrt(25)")
 * - Post-increment/decrement (e.g., "x++", "y--")
 */
public class IdentifierFactorParser implements FactorParser {

    @Override
    public boolean canParse(Token currentToken, List<Token> tokens, int currentIndex) {
        return currentToken.type == CoreTokenType.IDENTIFIER;
    }

    @Override
    public ASTNode parse(ParseContext context) {
        String identifier = context.getCurrentToken().value;
        context.advance();

        // Check for post-increment/decrement
        if (context.getCurrentToken().type == AssignTokenType.INCREMENT) {
            context.advance();
            return new PostIncrementNode(identifier, true);
        } 
        else if (context.getCurrentToken().type == AssignTokenType.DECREMENT) {
            context.advance();
            return new PostIncrementNode(identifier, false);
        }

        // Simple variable reference
        return new VariableNode(identifier);
    }
}
