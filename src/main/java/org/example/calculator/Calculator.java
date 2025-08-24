package org.example.calculator;

import org.example.lexer.*;
import org.example.parser.*;
import org.example.operations.*;

import java.util.*;

// Main Calculator class - Abstract base class for all calculator types
public abstract class Calculator {
    private VariableStore variableStore;

    protected OperatorRegistry operatorRegistry;
    protected FactorParserRegistry factorParserRegistry;
    protected OperationRegistry operationRegistry;
    protected FactorParserConfigurator factorParserConfigurator;

    public Calculator() {
        this.variableStore = new VariableStore();

        // Initialize instance-based registries
        this.operatorRegistry = new OperatorRegistry();
        this.factorParserRegistry = new FactorParserRegistry();
        this.operationRegistry = new OperationRegistry();
        this.factorParserConfigurator = new FactorParserConfigurator();

        // Configure all components for this calculator type
        configureParsers();
        configureOperators();
        configureOperations();
    }

    /**
     * Template method - subclasses can override to configure their specific factor parsers
     * This method should configure which parsing capabilities this calculator supports
     */
    protected void configureParsers() {
        factorParserConfigurator.configureDefault(factorParserRegistry);
    }

    /**
     * Template method - subclasses MUST override to register their specific operators
     * This method should clear existing registries and register only needed operators
     */
    protected abstract void configureOperators();

    /**
     * Template method - subclasses can override to configure their specific operations
     */
    protected void configureOperations() {
        // Default: no additional functions beyond basic arithmetic
    }

    public String processExpressions(List<String> expressions) {
        for (String expr : expressions) {
            processExpression(expr.trim());
        }
        String res = variableStore.getFormattedOutput();
        variableStore.clear(); // Clear state after processing
        return res;
    }

    private void processExpression(String expression) {
        if (expression.isEmpty()) return;

        Lexer lexer = new Lexer(expression, operatorRegistry);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens, factorParserRegistry);
        ASTNode ast = parser.parseAssignment();

        ast.evaluate(variableStore, operationRegistry);
    }
}
