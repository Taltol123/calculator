package org.example;

import org.example.calculator.*;
import org.example.io.*;
import org.example.errors.ErrorMessages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for the text-based calculator
 * Now supports concurrent processing of multiple requests
 */
public class App {

    private static final String EXIT_COMMAND = "exit";

    public static void main(String[] args) {
        try {
            runApp(args);
        } catch (IllegalArgumentException e) {
            System.err.println(ErrorMessages.ERROR_PREFIX.getMessage() + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(ErrorMessages.IO_ERROR_PREFIX.getMessage() + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println(ErrorMessages.UNEXPECTED_ERROR_PREFIX.getMessage() + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Main application logic extracted for testing purposes
     *
     * @param args command line arguments
     * @throws IllegalArgumentException if arguments are invalid
     * @throws IOException              if IO operations fail
     */
    public static void runApp(String[] args) throws IllegalArgumentException, IOException {
        if (args.length < 2) {
            throw new IllegalArgumentException(ErrorMessages.INSUFFICIENT_ARGUMENTS.getMessage());
        }

        // Parse command line arguments
        String ioTypeStr = args[0];
        String calculatorTypeStr = args[1];

        IOType ioType = IOType.fromString(ioTypeStr);
        CalculatorType calculatorType = CalculatorType.fromString(calculatorTypeStr);

        // Create IO handler
        IOHandler ioHandler = null;
        if (args.length >= 4) {
            ioHandler = IOFactory.createIOHandler(ioType, args[2], args[3]);
        } else {
            ioHandler = IOFactory.createIOHandler(ioType);
        }
        // Create concurrent calculator service with auto-configured thread pool
        ConcurrentCalculatorService concurrentService = new ConcurrentCalculatorService(calculatorType);

        try {
            runCalculatorConcurrently(concurrentService, ioHandler);
        } finally {
            concurrentService.shutdown();
            ioHandler.close();
        }
    }

    private static void runCalculatorConcurrently(ConcurrentCalculatorService concurrentService, IOHandler ioHandler) {
        try {
            runContinuousMode(concurrentService, ioHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runContinuousMode(ConcurrentCalculatorService concurrentService, IOHandler ioHandler) {
        int requestNumber = 1;
        List<List<String>> processedRequests = new ArrayList<>();

        // Process initial requests immediately when starting
        List<List<String>> initialRequests = readAllRequests(ioHandler);

        // Check for exit command in initial requests
        if (initialRequests == null) {
            return; // Exit the method
        }

        // Process initial requests if they exist
        if (!initialRequests.isEmpty()) {
            // Queue the initial requests with correct sequential numbering
            for (int i = 0; i < initialRequests.size(); i++) {
                requestNumber++;
            }

            // Calculate the starting request number for this batch
            int startingRequestNumber = requestNumber - initialRequests.size();
            processRequestsInParallel(concurrentService, ioHandler, initialRequests, startingRequestNumber);

            // Add the initial requests to the processed list
            processedRequests.addAll(initialRequests);
        }

        // Now start continuous monitoring for new requests
        while (true) {
            try {
                // Check if there's new content available
                if (ioHandler.hasNewContent()) {
                    // Reset reader to start from beginning (useful for file mode)
                    ioHandler.resetReader();

                    // Read all requests from the current input
                    List<List<String>> allRequests = readAllRequests(ioHandler);

                    // Check for exit command
                    if (allRequests == null) {
                        return; // Exit the method
                    }

                    // Find only the new requests that haven't been processed
                    List<List<String>> newRequests = findNewRequests(allRequests, processedRequests);

                    if (!newRequests.isEmpty()) {
                        // Queue the new requests with correct sequential numbering
                        for (int i = 0; i < newRequests.size(); i++) {
                            requestNumber++;
                        }

                        // Calculate the starting request number for this batch
                        int startingRequestNumber = requestNumber - newRequests.size();
                        processRequestsInParallel(concurrentService, ioHandler, newRequests, startingRequestNumber);

                        // Add the new requests to the processed list
                        processedRequests.addAll(newRequests);
                    }
                }

                // Wait a bit before checking again (only for continuous monitoring handlers)
                if (ioHandler.supportsContinuousMonitoring()) {
                    Thread.sleep(1000);
                } else {
                    break;
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                ioHandler.writeLine(e.getMessage());
                break;
            }
        }

        // For non-continuous handlers (like console), read all requests at once after breaking from the loop
        if (!ioHandler.supportsContinuousMonitoring()) {
            List<List<String>> allRequests = readAllRequests(ioHandler);

            // Check for exit command
            if (allRequests == null) {
                return; // Exit the method
            }

            if (!allRequests.isEmpty()) {
                processRequestsInParallel(concurrentService, ioHandler, allRequests, 1);
            } else {
                ioHandler.writeLine("No expressions provided.");
            }
        }
    }

    /**
     * Find new requests that haven't been processed before
     * Uses content-based comparison to identify new requests
     */
    private static List<List<String>> findNewRequests(List<List<String>> allRequests, List<List<String>> processedRequests) {
        List<List<String>> newRequests = new ArrayList<>();

        for (List<String> request : allRequests) {
            if (!isRequestAlreadyProcessed(request, processedRequests)) {
                newRequests.add(request);
            }
        }

        return newRequests;
    }

    /**
     * Check if a request has already been processed by comparing its content
     */
    private static boolean isRequestAlreadyProcessed(List<String> request, List<List<String>> processedRequests) {
        for (List<String> processed : processedRequests) {
            if (areRequestsEqual(request, processed)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compare two requests for equality by comparing their expressions
     */
    private static boolean areRequestsEqual(List<String> request1, List<String> request2) {
        if (request1.size() != request2.size()) {
            return false;
        }

        for (int i = 0; i < request1.size(); i++) {
            if (!request1.get(i).equals(request2.get(i))) {
                return false;
            }
        }

        return true;
    }

    private static List<List<String>> readAllRequests(IOHandler ioHandler) {
        List<List<String>> allRequests = new ArrayList<>();
        int requestNumber = 1;

        while (true) {
            List<String> expressions = readSingleRequest(ioHandler);

            // Check for exit command
            if (expressions == null) {
                return null; // Return null to indicate exit
            }

            if (expressions.isEmpty()) {
                break;
            }

            allRequests.add(expressions);
            requestNumber++;
        }

        return allRequests;
    }

    private static List<String> readSingleRequest(IOHandler ioHandler) {
        List<String> expressions = new ArrayList<>();
        String line;

        // Read expressions until empty line, EOF, or exit command
        while ((line = ioHandler.readLine()) != null && !line.trim().isEmpty()) {
            String trimmedLine = line.trim();

            // Check for exit command
            if (trimmedLine.equalsIgnoreCase(EXIT_COMMAND)) {
                return null; // Return null to indicate exit
            }

            expressions.add(trimmedLine);
        }

        // If we hit EOF, return what we have
        if (line == null) {
            return expressions;
        }

        return expressions;
    }


    private static void processRequestsInParallel(ConcurrentCalculatorService concurrentService,
                                                  IOHandler ioHandler,
                                                  List<List<String>> allRequests,
                                                  int startingRequestNumber) {
        long startTime = System.currentTimeMillis();

        List<ConcurrentCalculatorService.CalculatorResult> results =
                concurrentService.processRequestsConcurrently(allRequests);

        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        displayResults(ioHandler, results, processingTime, startingRequestNumber);
    }


    private static void displayResults(IOHandler ioHandler,
                                       List<ConcurrentCalculatorService.CalculatorResult> results,
                                       long processingTime,
                                       int startingRequestNumber) {
        ioHandler.writeLine("Results:");

        for (int i = 0; i < results.size(); i++) {
            var result = results.get(i);
            if (result.hasError()) {
                ioHandler.writeLine("Request " + (startingRequestNumber + i) + " Error: " + result.getError());
            } else {
                ioHandler.writeLine("Request " + (startingRequestNumber + i) + " Result: " + result.getResult());
            }
        }
    }
}
