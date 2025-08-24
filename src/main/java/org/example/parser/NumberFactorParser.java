package org.example.parser;

import org.example.lexer.Token;
import static org.example.lexer.TokenTypes.CoreTokenType;
import java.util.List;

/**
 * Parses numeric literals (e.g., "42", "123")
 */
public class NumberFactorParser implements FactorParser {

    @Override
    public boolean canParse(Token currentToken, List<Token> tokens, int currentIndex) {
        return currentToken.type == CoreTokenType.NUMBER;
    }

    @Override
    public ASTNode parse(ParseContext context) {
        Token token = context.getCurrentToken();
        context.advance();
        return new NumberNode(Integer.parseInt(token.value));
    }
}
