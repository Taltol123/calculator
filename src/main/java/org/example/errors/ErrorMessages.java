package org.example.errors;

/**
 * Enum containing all error messages used throughout the application
 */
public enum ErrorMessages {
    // App error messages
    INSUFFICIENT_ARGUMENTS("Insufficient arguments"),

    // IO error messages
    FILE_IO_INVALID_PARAMS("FileIOHandler requires input and output file paths"),
    UNSUPPORTED_IO_TYPE("Unsupported IO type: "),
    ERROR_WRITING_FILE("Error writing to file: "),
    ERROR_CLOSING_FILE("Error closing file: "),
    INPUT_FILE_NOT_EXISTS("Input file does not exist: "),

    // Calculator error messages
    DIVISION_BY_ZERO("Division by zero"),
    UNEXPECTED_TOKEN("Unexpected token: "),
    CALCULATOR_TYPE_NULL("Calculator type cannot be null"),
    ERROR_CONCURRENT("Error processing concurrent requests"),

    // Operator registry error messages
    OPERATOR_PATTERN_NULL_OR_EMPTY("Operator pattern cannot be null or empty"),
    TOKEN_TYPE_NULL("TokenType cannot be null"),
    UNKNOWN_OPERATOR("Unknown operator: "),

    //Parser error messages
    ERROR_NULL_FACTOR_PARSER("FactorParser cannot be null"),

    // Generic error messages
    ERROR_PREFIX("Error: "),
    IO_ERROR_PREFIX("IO Error: "),
    UNEXPECTED_ERROR_PREFIX("Unexpected error: ");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Get formatted error message with additional context
     *
     * @param context additional context to append to the message
     * @return formatted error message
     */
    public String getMessage(String context) {
        return message + context;
    }

    /**
     * Get formatted error message with additional context
     *
     * @param context additional context to append to the message
     * @return formatted error message
     */
    public String getMessage(Object context) {
        return message + context;
    }
}
