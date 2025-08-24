package org.example.parser;

import org.example.calculator.VariableStore;
import org.example.operations.OperationRegistry;

// AST Node hierarchy - Base class
public abstract class ASTNode {
    public abstract int evaluate(VariableStore store, OperationRegistry operationRegistry);
}
