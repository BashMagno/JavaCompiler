package main;

public enum TokenType 
{
    // KEYWORDS
    // VARIABLES
    VAR,

    // LOOPS
    FOR, WHILE,

    // CONDITIONALS
    IF, ELSE,

    // IDENTIFIERS
    IDENTIFIER, NUMBER, CHAR_LITERAL, STRING_LITERAL,
    FLOAT_LITERAL, BOOL_LITERAL, DOUBLE_LITERAL,

    // OPERATORS
    EQUALS, ADD, SUBSTRAIN, DIVIDE, MULTIPLY, MODULE,

    // SEPARATORS
    SEMICOLON, COMMA, LEFT_PAREN, RIGHT_PAREN,
    // ; , ( ) { } : .
    LEFT_BRACE, RIGHT_BRACE, COLON, DOT,

    //LOGIC OPERATORS
    GREATER_THAN, LESS_THAN,

    // FUNCTIONS
    PRINT,      // Console debug print : Example -> print("Hola Mundo!") -> $> Hola Mundo!
    RETURN, 
    
    // COMMENTS
    COMMENTS,

    // END OF LINE
    END_OF_FILE
}
