package main;

public enum TokenType 
{
    // KEYWORDS
    // VARIABLES
    CHAR, STRING, INT, DOUBLE, FLOAT, BOOL,

    // LOOPS
    FOR, WHILE,

    // CONDITIONALS
    IF, ELSE,

    // IDENTIFIERS
    IDENTIFIER, NUMBER, CHAR_LITERAL, STRING_LITERAL,
    FLOAT_LITERAL, BOOL_LITERAL, DOUBLE_LITERAL,

    // OPERATORS
    EQUALS, ADD,/*  ADD_LITERAL, */ SUBSTRAIN, DIVIDE, MULTIPLY, MODULE,

    // SEPARATORS
    SEMICOLON, COMMA, LEFT_PAREN, RIGHT_PAREN,
    // ; , ( ) { } : .
    LEFT_BRACE, RIGHT_BRACE, COLON, DOT,

    // FUNCTIONS
    PRINT, RETURN, // Console debug print : Example -> print("Hola Mundo!") -> $> Hola Mundo!

    // COMMENTS
    COMMENTS,

    // END OF LINE
    EOF
}
