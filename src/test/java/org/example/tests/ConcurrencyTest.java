package org.example.tests;

import org.example.calculator.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Test class for concurrent calculator functionality
 */
public class ConcurrencyTest {
    
    private ConcurrentCalculatorService concurrentService;
    
    @BeforeEach
    void setUp() {
        concurrentService = new ConcurrentCalculatorService(CalculatorType.ASSIGNMENT);
    }
    
    @AfterEach
    void tearDown() {
        if (concurrentService != null) {
            concurrentService.shutdown();
        }
    }
    
    @Test
    void testSingleRequest() {
        List<String> expressions = List.of("x = 5", "y = 10", "z = x + y");
        
        var result = concurrentService.processRequestAsync(expressions);
        var calculatorResult = result.join();
        
        assertFalse(calculatorResult.hasError());
        assertEquals("(x=5,y=10,z=15)", calculatorResult.getResult());
        assertEquals(1, calculatorResult.getRequestId());
    }
    
    @Test
    void testMultipleRequestsConcurrently() {
        List<List<String>> requests = new ArrayList<>();
        
        // Request 1: Simple arithmetic
        requests.add(List.of("a = 10", "b = 20", "c = a + b"));
        
        // Request 2: Increment operations
        requests.add(List.of("x = 5", "x++", "y = x * 2"));
        
        // Request 3: Compound assignments
        requests.add(List.of("p = 100", "p += 50", "p *= 2"));
        
        // Process all requests concurrently
        List<ConcurrentCalculatorService.CalculatorResult> results = 
            concurrentService.processRequestsConcurrently(requests);
        
        // Verify all requests completed
        assertEquals(3, results.size());
        
        // Verify results
        assertFalse(results.get(0).hasError());
        assertEquals("(a=10,b=20,c=30)", results.get(0).getResult());
        
        assertFalse(results.get(1).hasError());
        assertEquals("(x=6,y=12)", results.get(1).getResult());
        
        assertFalse(results.get(2).hasError());
        assertEquals("(p=300)", results.get(2).getResult());
        
        // Verify request IDs are sequential
        assertEquals(1, results.get(0).getRequestId());
        assertEquals(2, results.get(1).getRequestId());
        assertEquals(3, results.get(2).getRequestId());
    }
    
    @Test
    void testErrorHandling() {
        List<String> expressions = List.of("x = 5", "y = 0", "z = x / y"); // Division by zero
        
        var result = concurrentService.processRequestAsync(expressions);
        var calculatorResult = result.join();
        
        assertTrue(calculatorResult.hasError());
        assertTrue(calculatorResult.getError().contains("Division by zero"));
        assertEquals(1, calculatorResult.getRequestId());
    }

    
    @Test
    void testConcurrentVariableIsolation() {
        // This test verifies that different threads don't interfere with each other's variables
        
        List<List<String>> requests = new ArrayList<>();
        
        // Request 1: Uses variable 'x'
        requests.add(List.of("x = 100", "result1 = x + 50"));
        
        // Request 2: Also uses variable 'x' but should be isolated
        requests.add(List.of("x = 200", "result2 = x * 2"));
        
        // Request 3: Uses different variable names
        requests.add(List.of("y = 300", "z = 400", "result3 = y + z"));
        
        List<ConcurrentCalculatorService.CalculatorResult> results = 
            concurrentService.processRequestsConcurrently(requests);
        
        // Each request should have its own isolated VariableStore
        assertEquals("(result1=150,x=100)", results.get(0).getResult());
        assertEquals("(result2=400,x=200)", results.get(1).getResult());
        assertEquals("(result3=700,y=300,z=400)", results.get(2).getResult());
    }
}
