package org.example.calculator;

import org.example.errors.ErrorMessages;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Concurrent calculator service that processes multiple requests simultaneously
 * Each thread gets its own calculator instance for complete isolation
 */
public class ConcurrentCalculatorService {
    private final ExecutorService executorService;
    private final ThreadLocal<Calculator> calculatorThreadLocal;
    private final AtomicInteger requestIdCounter = new AtomicInteger(0);

    /**
     * Create a concurrent calculator service with auto-configured thread pool
     *
     * @param calculatorType the type of calculator to use for all requests
     */
    public ConcurrentCalculatorService(CalculatorType calculatorType) {
        // Auto-configure thread pool based on available processors
        int processors = Runtime.getRuntime().availableProcessors();
        int corePoolSize = Math.max(2, processors);
        int maxPoolSize = processors * 2;

        this.executorService = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // ThreadLocal calculator factory - each thread gets its own calculator instance
        this.calculatorThreadLocal = ThreadLocal.withInitial(() -> {
            System.out.println("Creating new calculator instance for thread: " + Thread.currentThread().getName());
            return CalculatorFactory.createCalculator(calculatorType);
        });
    }

    /**
     * Process a single request concurrently
     *
     * @param expressions the expressions to process
     * @return CompletableFuture containing the result
     */
    public CompletableFuture<CalculatorResult> processRequestAsync(List<String> expressions) {
        int requestId = requestIdCounter.incrementAndGet();

        return CompletableFuture.supplyAsync(() -> {
            try {
                Calculator calculator = calculatorThreadLocal.get();
                String result = calculator.processExpressions(expressions);

                return new CalculatorResult(requestId, result, null);
            } catch (Exception e) {
                return new CalculatorResult(requestId, null, e.getMessage());
            }
        }, executorService);
    }

    /**
     * Process multiple requests concurrently and collect results
     *
     * @param requests list of expression lists (each list is one request)
     * @return list of results in the same order as requests
     */
    public List<CalculatorResult> processRequestsConcurrently(List<List<String>> requests) {
        List<CompletableFuture<CalculatorResult>> futures = requests.stream()
                .map(this::processRequestAsync)
                .toList();

        // Wait for all to complete and collect results
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        try {
            allFutures.get(); // Wait for all to complete

            // Extract results in order
            List<CalculatorResult> results = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            return results;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(ErrorMessages.ERROR_CONCURRENT.getMessage(), e);
        }
    }

    /**
     * Shutdown the service and release resources
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Result container for calculator requests
     */
    public static class CalculatorResult {
        private final int requestId;
        private final String result;
        private final String error;

        public CalculatorResult(int requestId, String result, String error) {
            this.requestId = requestId;
            this.result = result;
            this.error = error;
        }

        public int getRequestId() {
            return requestId;
        }

        public String getResult() {
            return result;
        }

        public String getError() {
            return error;
        }

        public boolean hasError() {
            return error != null;
        }

        @Override
        public String toString() {
            if (hasError()) {
                return String.format("Request %d Error: %s", requestId, error);
            } else {
                return String.format("Request %d Result: %s", requestId, result);
            }
        }
    }
}
