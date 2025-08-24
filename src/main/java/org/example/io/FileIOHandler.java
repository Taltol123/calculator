package org.example.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

import org.example.errors.ErrorMessages;

/**
 * Concrete implementation of the IOHandler interface for file-based input and output
 */
public class FileIOHandler implements IOHandler {
    private BufferedReader reader;
    private BufferedWriter writer;
    private List<String> lines;
    private int currentLineIndex;
    private String inputPath;
    private long lastModifiedTime; // Added to track last modified time

    public FileIOHandler(String inputPath, String outputPath) throws IOException {
        this.inputPath = inputPath;

        // Check if input file exists
        if (!Files.exists(Paths.get(inputPath))) {
            throw new IOException(ErrorMessages.INPUT_FILE_NOT_EXISTS.getMessage() + inputPath);
        }

        // Read all lines from input file
        this.lines = Files.readAllLines(Paths.get(inputPath));
        this.currentLineIndex = 0;
        this.lastModifiedTime = Files.getLastModifiedTime(Paths.get(inputPath)).toMillis(); // Initialize lastModifiedTime

        // Prepare output file
        this.writer = Files.newBufferedWriter(Paths.get(outputPath));
    }

    @Override
    public String readLine() {
        if (currentLineIndex < lines.size()) {
            return lines.get(currentLineIndex++);
        }
        return null;
    }

    @Override
    public void writeLine(String line) {
        try {
            writer.write(line);
            writer.newLine();
            writer.flush(); // Force flush to ensure real-time output
        } catch (IOException e) {
            System.err.println(ErrorMessages.ERROR_WRITING_FILE.getMessage() + e.getMessage());
        }
    }

    /**
     * Check if there's new content available by comparing file modification time
     */
    @Override
    public boolean hasNewContent() {
        try {
            long currentModified = Files.getLastModifiedTime(Paths.get(inputPath)).toMillis();
            boolean hasNew = currentModified > lastModifiedTime;
            if (hasNew) {
                lastModifiedTime = currentModified; // Update the last modified time
            }
            return hasNew;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reset the reader to start reading from the beginning
     */
    @Override
    public void resetReader() {
        this.currentLineIndex = 0;
        try {
            // Re-read the file to get any new content
            this.lines = Files.readAllLines(Paths.get(inputPath));
            // Update the last modified time to current time to avoid re-processing the same content
            this.lastModifiedTime = Files.getLastModifiedTime(Paths.get(inputPath)).toMillis();
        } catch (IOException e) {
            System.err.println("Error re-reading input file: " + e.getMessage());
        }
    }

    /**
     * File-based handlers support continuous monitoring
     */
    @Override
    public boolean supportsContinuousMonitoring() {
        return true;
    }

    /**
     * Read all requests from the input file
     */
    public List<List<String>> readAllRequests() {
        try {
            // Re-read the file to get any new content
            this.lines = Files.readAllLines(Paths.get(inputPath));
            this.currentLineIndex = 0;

            List<List<String>> allRequests = new ArrayList<>();
            List<String> currentRequest = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    if (!currentRequest.isEmpty()) {
                        allRequests.add(new ArrayList<>(currentRequest));
                        currentRequest.clear();
                    }
                } else {
                    currentRequest.add(line.trim());
                }
            }

            // Add the last request if it's not empty
            if (!currentRequest.isEmpty()) {
                allRequests.add(currentRequest);
            }

            return allRequests;
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println(ErrorMessages.ERROR_CLOSING_FILE.getMessage() + e.getMessage());
        }
    }
}
