package org.example.parser;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration class for factor parsers that follows the Open-Closed Principle
 * New parsers can be added by modifying only this configuration class
 * or by using external configuration files/annotations
 * <p>
 * Now instance-based instead of static to support concurrent calculator instances
 */
public class FactorParserConfigurator {

    /**
     * Get parsers for assignment-capable calculators
     * Includes basic parsers + increment/decrement operators
     */
    public List<FactorParser> getAssignmentParsers() {
        return Arrays.asList(
                new NumberFactorParser(),
                new IdentifierFactorParser(),      // Handles variables, functions, post-increment
                new PreIncrementFactorParser(),    // Handles ++var, --var
                new ParenthesesFactorParser()
        );
    }

    /**
     * Get the default set of factor parsers (assignment parsers for backward compatibility)
     */
    public List<FactorParser> getDefaultParsers() {
        return getAssignmentParsers();
    }

    /**
     * Configure registry for assignment-capable calculator
     */
    public void configureAssignment(FactorParserRegistry registry) {
        registry.clear();
        getAssignmentParsers().forEach(registry::register);
    }

    /**
     * Auto-configure the registry with default parsers
     * This method can be called once during calculator initialization
     */
    public void configureDefault(FactorParserRegistry registry) {
        if (registry.getRegisteredCount() == 0) {
            getDefaultParsers().forEach(registry::register);
        }
    }
}
