package org.example.lexer;

public class Token {
    public final TokenType type;
    public final String value;
    public final int position;
    public final int length;

    public Token(TokenType type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
        this.length = value.length();
    }

    public Token(TokenType type, String value, int position, int length) {
        this.type = type;
        this.value = value;
        this.position = position;
        this.length = length;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, '%s', pos:%d, len:%d)", type, value, position, length);
    }
}
