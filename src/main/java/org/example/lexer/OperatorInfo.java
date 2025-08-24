package org.example.lexer;

/**
 * Metadata about an operator for the registry
 * Stores the pattern to match and corresponding token type
 */
public class OperatorInfo {
    public final String pattern;
    public final TokenType tokenType;
    
    public OperatorInfo(String pattern, TokenType tokenType) {
        this.pattern = pattern;
        this.tokenType = tokenType;
    }
    
    @Override
    public String toString() {
        return String.format("OperatorInfo(pattern: '%s', type: %s)", pattern, tokenType);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OperatorInfo that = (OperatorInfo) obj;
        return pattern.equals(that.pattern) && tokenType == that.tokenType;
    }
    
    @Override
    public int hashCode() {
        return pattern.hashCode() * 31 + tokenType.hashCode();
    }
}
