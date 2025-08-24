package org.example.io;

import org.example.errors.ErrorMessages;

/**
 * Enum representing different types of IO operations
 */
public enum IOType {
    FILE("file");

    private final String value;

    IOType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Static utility method to convert a string representation into an IOType enum value
     */
    public static IOType fromString(String value) {
        for (IOType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(ErrorMessages.UNSUPPORTED_IO_TYPE + value);
    }
}
