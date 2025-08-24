package org.example.tests;

import org.example.App;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * End-to-end tests for the App class
 * Tests the app logic with different command line arguments and verifies output
 */
public class AppEndToEndTest {

    @TempDir
    Path tempDir;

    private Path inputFile;
    private Path outputFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create test input file with the exercise example (requires programming calculator)
        inputFile = tempDir.resolve("test_input.txt");
        outputFile = tempDir.resolve("test_output.txt");

        Files.write(inputFile,
            "i = 0\nj = ++i\nx = i++ + 5\ny = (5 + 3) * 10\ni += y\nexit".getBytes());
    }


    
    @Test
    void testInvalidCalculatorType() throws Exception {
        Path simpleInputFile = tempDir.resolve("simple_input.txt");
        Files.write(simpleInputFile, "x = 10\ny = x + 5\nexit".getBytes());
        
        String[] args = {"file", "invalid", simpleInputFile.toString(), outputFile.toString()};
        
        App.runApp(args);
        
        assertTrue(Files.exists(outputFile), "Output file should be created");
    }
    
    @Test
    void testInsufficientArguments() {
        // Test: java App (no arguments)
        String[] args = {};
        
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            App.runApp(args);
        });
    }

    
    @Test
    void testFileModeMissingPaths() {
        // Test: java App file basic (missing input/output paths)
        String[] args = {"file", "assignment"};
        
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            App.runApp(args);
        });
    }
}
