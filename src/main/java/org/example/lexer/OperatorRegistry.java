package org.example.lexer;

import org.example.errors.ErrorMessages;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry for operators
 * Allows adding new operators without modifying the lexer
 */
public class OperatorRegistry {
    private final List<OperatorInfo> sortedOperators = new ArrayList<>();

    /**
     * Register a new operator pattern
     *
     * @param pattern   the operator symbol(s) to recognize
     * @param tokenType the token type to create when this pattern is found
     */
    public void register(String pattern, TokenType tokenType) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.OPERATOR_PATTERN_NULL_OR_EMPTY.getMessage());
        }
        if (tokenType == null) {
            throw new IllegalArgumentException(ErrorMessages.TOKEN_TYPE_NULL.getMessage());
        }

        OperatorInfo operatorInfo = new OperatorInfo(pattern, tokenType);

        // Insert into sorted list in correct position (longest first)
        insertInSortedOrder(operatorInfo);
    }

    /**
     * Insert an operator into the sorted list maintaining longest-first order
     *
     * @param operatorInfo the operator to insert
     */
    private void insertInSortedOrder(OperatorInfo operatorInfo) {
        int insertPosition = 0;

        // Find correct position: insert before first operator that is shorter
        for (int i = 0; i < sortedOperators.size(); i++) {
            if (sortedOperators.get(i).pattern.length() < operatorInfo.pattern.length()) {
                insertPosition = i;
                break;
            }
            insertPosition = i + 1;
        }

        sortedOperators.add(insertPosition, operatorInfo);
    }

    /**
     * Try to recognize an operator at the given position in the input
     *
     * @param input    the input string
     * @param position the current position in the input
     * @return Token if an operator was found, null otherwise
     */
    public Token recognizeOperator(String input, int position) {
        for (OperatorInfo op : sortedOperators) {
            if (position + op.pattern.length() <= input.length() &&
                    input.regionMatches(position, op.pattern, 0, op.pattern.length())) {
                return new Token(op.tokenType, op.pattern, position, op.pattern.length());
            }
        }
        return null;
    }
}
