package org.example.parser;

// Same package, no import needed
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import static org.example.lexer.TokenTypes.CoreTokenType;
import java.util.List;

/**
 * Parses parenthesized expressions
 * Examples: "(2 + 3)", "((x * y) + z)"
 */
public class ParenthesesFactorParser implements FactorParser {

    @Override
    public boolean canParse(Token currentToken, List<Token> tokens, int currentIndex) {
        return currentToken.type == CoreTokenType.LPAREN;
    }

    @Override
    public ASTNode parse(ParseContext context) {
        context.advance(); // consume '('
        ASTNode node = context.parseExpression();
        context.expect(CoreTokenType.RPAREN);
        return node;
    }
}
