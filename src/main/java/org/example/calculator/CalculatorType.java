package org.example.calculator;

import org.example.errors.ErrorMessages;

// Enum defining different calculator types
public enum CalculatorType {
    ASSIGNMENT("Assignment Calculator");

    private final String displayName;

    CalculatorType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


    public static CalculatorType fromString(String type) {
        if (type == null) {
            throw new IllegalArgumentException(ErrorMessages.CALCULATOR_TYPE_NULL.getMessage());
        }

        switch (type.toLowerCase()) {
            case "assignment":
            case "assign":
            default:
                return ASSIGNMENT;
        }
    }
}
