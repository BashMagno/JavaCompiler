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
            if(currentChar == '=' && currentChar+1 == '=')
            {
                tokens.add(new Token(TokenType.IS_EQUALS, "=="));
            }else if(currentChar == '=' && currentChar + 1 != '=')
            {
                tokens.add(new Token(TokenType.EQUALS, "="));
            }
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
                        break;
                    case '<':
                        tokens.add(new Token(TokenType.LESS_THAN, "<"));
                        break;
                    case '>':
                        tokens.add(new Token(TokenType.GREATER_THAN, ">"));
                        break;
                    
                    default:
                        // Manejar otros casos o lanzar una excepción si se encuentra un carácter no reconocido
                        // También puedes intentar escanear un BOOL_LITERAL aquí
                        Token boolToken = scanBool();
                        if (boolToken != null) 
                        {
                            tokens.add(boolToken);
                            break;
                        } 

                }
                currentPos++;
            }
        }
        
        // Verificar si se alcanzó el final del archivo y agregar el token correspondiente
        if (currentPos == input.length()) {
            tokens.add(new Token(TokenType.END_OF_FILE, "EOF"));
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
        String keyword = lexeme.toString();
        // Identificamos si es una palabra clave
        switch (keyword) 
        {
            case "var":
                return new Token(TokenType.VAR, lexeme.toString());
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
            case "print":
                return new Token(TokenType.PRINT, lexeme.toString());
            case "ASSEMBLYDEBUG":
                return new Token(TokenType.ASSEMBLYDEBUG, lexeme.toString());
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
