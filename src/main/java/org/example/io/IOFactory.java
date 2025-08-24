package org.example.io;

import java.io.IOException;

import org.example.errors.ErrorMessages;

/**
 * Factory class responsible for creating IO handler objects
 */
public class IOFactory {

    /**
     * Creates an IO handler based on the specified IO type
     *
     * @param ioType the type of IO handler to create
     * @param params variable number of String parameters (e.g., input path, output path for file handler)
     * @return an IOHandler instance
     * @throws IllegalArgumentException if invalid parameters are provided
     * @throws IOException              if file operations fail
     */
    public static IOHandler createIOHandler(IOType ioType, String... params) throws IOException {
        switch (ioType) {

            case FILE:
                if (params.length < 2) {
                    throw new IllegalArgumentException(ErrorMessages.FILE_IO_INVALID_PARAMS.getMessage());
                }
                return new FileIOHandler(params[0], params[1]);

            default:
                throw new IllegalArgumentException(ErrorMessages.UNSUPPORTED_IO_TYPE.getMessage() + ioType);
        }
    }
}
