package org.example.parser;

import org.example.lexer.Token;
import org.example.lexer.TokenType;
import java.util.List;

/**
 * Strategy interface for parsing different types of factors
 * to be added without modifying existing parsing logic
 */
public interface FactorParser {
    
    /**
     * Checks if this parser can handle the current token sequence
     * @param currentToken the current token being examined
     * @param tokens the complete token list for lookahead
     * @param currentIndex the current position in the token list
     * @return true if this parser can handle the current context
     */
    boolean canParse(Token currentToken, List<Token> tokens, int currentIndex);
    
    /**
     * Parses the factor and returns the corresponding AST node
     * @param parseContext the parsing context containing tokens and helper methods
     * @return the parsed AST node
     */
    ASTNode parse(ParseContext parseContext);
}
