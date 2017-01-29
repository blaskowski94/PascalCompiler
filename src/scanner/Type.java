package scanner;

/**
 * Bob Laskowski
 * Compilers II
 * Dr. Erik Steinmetz
 * January 17th, 2017
 * <p>
 * This enum class contains all of the different Types a token can be. There is a type for every keyword and symbol as
 * well as two types for ID and number.
 */
public enum Type {
    // Keywords
    AND, ARRAY, BEGIN, DIV, DO, ELSE, END, FUNCTION, IF, INTEGER, MOD, NOT, OF, OR, PROCEDURE, PROGRAM, REAL, THEN, VAR, WHILE,

    // Types
    ID, NUMBER,

    // Symbols
    SEMI, COMMA, PERIOD, COLON, LBRACE, RBRACE, LPAREN, RPAREN, PLUS, MINUS, EQUAL, NOTEQ, LTHAN, LTHANEQ, GTHAN, GTHANEQ, ASTERISK, FSLASH, ASSIGN
}
