package main;

import java.util.List;

public class Token {
    private TokenType type;
    private String lexeme;

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public static void printTokens(List<Token> tokens) {
        System.out.println("");
        for(Token token : tokens)
        {
            System.out.println(token);
        }
    }

    @Override
    public String toString() 
    {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lexeme + '\'' +
                '}';
    }
}
