package org.example.io;

/**
 * Interface defining the contract for all IO handler implementations
 */
public interface IOHandler {
    /**
     * Reads a line of input
     *
     * @return the line read from input, or null if end of input
     */
    String readLine();

    /**
     * Writes a line of output
     *
     * @param line the line to write
     */
    void writeLine(String line);

    /**
     * Closes the IO stream or resource
     */
    void close();

    /**
     * Checks if there's new content available to read
     *
     * @return true if new content is available, false otherwise
     */
    default boolean hasNewContent() {
        return false;
    }

    /**
     * Resets the reader to start reading from the beginning
     * Useful for re-reading content when new data is available
     */
    default void resetReader() {
        // Default implementation does nothing
    }

    /**
     * Determines if this IO handler supports continuous monitoring
     * File-based handlers return true, console-based handlers return false
     *
     * @return true if continuous monitoring is supported, false otherwise
     */
    default boolean supportsContinuousMonitoring() {
        return false;
    }
}
