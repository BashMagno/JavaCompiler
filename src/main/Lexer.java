package main;

import java.util.ArrayList;
import java.util.List;

public class Lexer 
{
    private String input;
    private int currentPos;

    public Lexer(String input) 
    {
        this.input = input;
        this.currentPos = 0;
    }

    public List<Token> tokenize() 
    {
        List<Token> tokens = new ArrayList<>();
    
        while (currentPos < input.length()) 
        {
            char currentChar = input.charAt(currentPos);
    
            if (Character.isLetter(currentChar)) 
            {
                tokens.add(scanIdentifierOrKeyword());
            } else if (Character.isDigit(currentChar)) 
            {
                tokens.add(scanNumber());
            } else if (currentChar == '\'') 
            {
                tokens.add(scanCharLiteral());
            } else if (currentChar == '"') 
            {
                tokens.add(scanStringLiteral());
            } 
            else 
            {
                switch (currentChar) 
                {
                    case '=':
                        tokens.add(new Token(TokenType.EQUALS, "="));
                        break;
                    case ';':
                        tokens.add(new Token(TokenType.SEMICOLON, ";"));
                        break;
                    case ',':
                        tokens.add(new Token(TokenType.COMMA, ","));
                        break;
                    case '(':
                        tokens.add(new Token(TokenType.LEFT_PAREN, "("));
                        break;
                    case ')':
                        tokens.add(new Token(TokenType.RIGHT_PAREN, ")"));
                        break;
                    case '{':
                        tokens.add(new Token(TokenType.LEFT_BRACE, "{"));
                        break;
                    case '}':
                        tokens.add(new Token(TokenType.RIGHT_BRACE, "}"));
                        break;
                    case ':':
                        tokens.add(new Token(TokenType.COLON, ":"));
                        break;
                    case '.':
                        tokens.add(new Token(TokenType.DOT, "."));
                        break;
                    case '/':
                        tokens.add(new Token(TokenType.COMMENTS, "//"));
                        break;
                    case '+':
                        tokens.add(new Token(TokenType.ADD, "+"));
                        break;
                    case '-':
                        tokens.add(new Token(TokenType.SUBSTRAIN, "-"));
                        break;
                    case '*':
                        tokens.add(new Token(TokenType.MULTIPLY, "*"));
                        break;
                    case '$':
                        tokens.add(new Token(TokenType.DIVIDE, "$"));
                        break;
                    case '%':
                        tokens.add(new Token(TokenType.MODULE, "%"));
                    default:
                        // Manejar otros casos o lanzar una excepción si se encuentra un carácter no reconocido
                        // También puedes intentar escanear un BOOL_LITERAL aquí
                        Token boolToken = scanBool();
                        if (boolToken != null) 
                        {
                            tokens.add(boolToken);
                        } 
                        else 
                        {
                            // Manejar otros caracteres
                        }
                        break;
                }
                currentPos++;
            }
        }
    
        return tokens;
    }
    
    /*
     * KEYWORDS AND VARIABLES
     */

    private Token scanIdentifierOrKeyword() 
    {
        StringBuilder lexeme = new StringBuilder();

        while (currentPos < input.length() && (Character.isLetterOrDigit(input.charAt(currentPos)) || input.charAt(currentPos) == '_')) 
        {
            lexeme.append(input.charAt(currentPos));
            currentPos++;
        }

        // Identificamos si es una palabra clave
        switch (lexeme.toString()) 
        {
            case "char":
                return new Token(TokenType.CHAR, lexeme.toString());
            case "string":
                return new Token(TokenType.STRING, lexeme.toString());
            case "int":
                return new Token(TokenType.INT, lexeme.toString());
            case "double":
                return new Token(TokenType.DOUBLE, lexeme.toString());
            case "float":
                return new Token(TokenType.FLOAT, lexeme.toString());
            case "bool":
                return new Token(TokenType.BOOL, lexeme.toString());
            case "true":
                return new Token(TokenType.BOOL_LITERAL, lexeme.toString());
            case "false":
                return new Token(TokenType.BOOL_LITERAL, lexeme.toString());
            case "for":
                return new Token(TokenType.FOR, lexeme.toString());
            case "while":
                return new Token(TokenType.WHILE, lexeme.toString());
            case "if":
                return new Token(TokenType.IF, lexeme.toString());
            case "else":
                return new Token(TokenType.ELSE, lexeme.toString());
            case "return":
                return new Token(TokenType.RETURN, lexeme.toString());
            default:
                return new Token(TokenType.IDENTIFIER, lexeme.toString());
                
        }
    }

    private Token scanNumber() 
    {
        StringBuilder lexeme = new StringBuilder();

        while (currentPos < input.length() && (Character.isDigit(input.charAt(currentPos)) || input.charAt(currentPos) == '.')) {
            lexeme.append(input.charAt(currentPos));
            currentPos++;
        }

        return new Token(TokenType.NUMBER, lexeme.toString());
    }

    private Token scanCharLiteral() 
    {
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(input.charAt(currentPos));        // Agrega la primera comilla simple

        currentPos++;                                   // Avanza al siguiente caracter
        while (currentPos < input.length() && input.charAt(currentPos) != '\'') 
        {
            lexeme.append(input.charAt(currentPos));
            currentPos++;
        }

        lexeme.append(input.charAt(currentPos));        // Agrega el segundo comilla simple
        currentPos++;                                   

        return new Token(TokenType.CHAR_LITERAL, lexeme.toString());
    }
    private Token scanBool() 
    {
        StringBuilder lexeme = new StringBuilder();

        while (currentPos < input.length() && Character.isLetter(input.charAt(currentPos))) 
        {
            lexeme.append(input.charAt(currentPos));
            currentPos++;
        }

        String boolLiteral = lexeme.toString();
        if (boolLiteral.equals("true") || boolLiteral.equals("false")) 
        {
            return new Token(TokenType.BOOL_LITERAL, boolLiteral);
        } else {
            // Manejar el caso donde no es ni "true" ni "false"
            return null;
        }
    }

    // Resto de las funciones de escaneo (scanNumber, scanCharLiteral, scanStringLiteral)...

    private boolean isNextWord(String word) 
    {
        int wordLength = word.length();
        if (currentPos + wordLength > input.length()) 
        {
            return false;
        }

        String nextWord = input.substring(currentPos, currentPos + wordLength);
        return nextWord.equals(word);
    }

    private Token scanStringLiteral() 
    {
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(input.charAt(currentPos)); // Agrega la comilla doble inicial

        currentPos++; // Avanza al siguiente caracter
        
        while (currentPos < input.length() && input.charAt(currentPos) != '"') 
        {
            lexeme.append(input.charAt(currentPos));
            currentPos++;
        }

        lexeme.append(input.charAt(currentPos)); // Agrega la comilla doble final
        currentPos++; // Avanza al siguiente caracter

        return new Token(TokenType.STRING_LITERAL, lexeme.toString());
    }
}
