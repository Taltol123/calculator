package org.example.parser;

import org.example.lexer.*;

import static org.example.lexer.TokenTypes.CoreTokenType;

import java.util.*;

// Recursive descent parser
public class Parser {
    private final List<Token> tokens;
    private ParseContext context;
    private final FactorParserRegistry factorParserRegistry;

    public Parser(List<Token> tokens, FactorParserRegistry factorParserRegistry) {
        this.tokens = tokens;
        this.factorParserRegistry = factorParserRegistry;
        this.context = new ParseContext(tokens, 0, tokens.get(0), this);
    }

    private void advance() {
        context.advance();
    }


    public ASTNode parseAssignment() {
        // Check if this is an assignment (identifier followed by assignment operator)
        Token currentToken = context.getCurrentToken();
        if (currentToken.type == CoreTokenType.IDENTIFIER && context.getCurrentTokenIndex() + 1 < tokens.size()) {
            Token nextToken = tokens.get(context.getCurrentTokenIndex() + 1);
            if (isAssignmentOperator(nextToken.type)) {
                String variable = currentToken.value;
                advance(); // consume identifier

                TokenType assignType = context.getCurrentToken().type;
                AssignmentOperator assignOperator = AssignmentOperator.fromTokenType(assignType);
                advance(); // consume assignment operator

                ASTNode expression = parseExpression();
                return new AssignmentNode(variable, assignOperator, expression);
            }
        }

        // If not an assignment, parse as expression
        return parseExpression();
    }

    private boolean isAssignmentOperator(TokenType type) {
        return AssignmentOperator.isAssignmentOperator(type);
    }

    public ASTNode parseExpression() {
        return parseExpressionWithPrecedence(Integer.MAX_VALUE);
    }

    private ASTNode parseExpressionWithPrecedence(int minPrecedence) {
        ASTNode node = parseFactor();

        while (BinaryOperator.isBinaryOperator(context.getCurrentToken().type) &&
                BinaryOperator.getPrecedence(context.getCurrentToken().type) < minPrecedence) {
            TokenType operator = context.getCurrentToken().type;
            int precedence = BinaryOperator.getPrecedence(operator);
            advance();
            ASTNode right = parseExpressionWithPrecedence(precedence);
            node = new BinaryOpNode(node, operator, right);
        }

        return node;
    }

    private ASTNode parseFactor() {
        return factorParserRegistry.parse(context);
    }
}
