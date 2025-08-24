package org.example.parser;

// Same package, no import needed
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import static org.example.lexer.TokenTypes.CoreTokenType;
import static org.example.lexer.TokenTypes.AssignTokenType;
import java.util.List;

/**
 * Parses pre-increment and pre-decrement expressions
 * Examples: "++x", "--counter"
 */
public class PreIncrementFactorParser implements FactorParser {

    @Override
    public boolean canParse(Token currentToken, List<Token> tokens, int currentIndex) {
        return currentToken.type == AssignTokenType.INCREMENT || 
               currentToken.type == AssignTokenType.DECREMENT;
    }

    @Override
    public ASTNode parse(ParseContext context) {
        Token operatorToken = context.getCurrentToken();
        boolean isIncrement = operatorToken.type == AssignTokenType.INCREMENT;
        context.advance();

        if (context.getCurrentToken().type != CoreTokenType.IDENTIFIER) {
            String operator = isIncrement ? "++" : "--";
            throw new RuntimeException("Expected identifier after " + operator + " at position " + context.getCurrentToken().position);
        }

        String identifier = context.getCurrentToken().value;
        context.advance();
        return new PreIncrementNode(identifier, isIncrement);
    }
}
