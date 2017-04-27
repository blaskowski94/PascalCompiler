/**
 * Bob Laskowski
 * Compilers II
 * Dr. Erik Steinmetz
 * January 17th, 2017
 *
 * This is a JFlex lexer definition for a Mini-Pascal scanner
 */

/* Import statements */
package scanner;

import java.util.HashMap;

%%

%class  MyScanner   /* Names the produced java file */
%function nextToken /* Renames the yylex() function */
%unicode            /* defines the set of characters the scanner will work on, uses 7.0 */
%public             /* Makes generated class public */
%type   Token       /* Defines the return type of the scanning function */
/* The value returned when the scanner reaches the end of the file */
%eofval{
  return null;
%eofval}
/* Declare member variables for scanner class */
%{
    // Hash map to hold all token types for lookup
    private HashMap<String, Type> tokenTypes;
    int lineNumber = 1;
%}
/* Code is copied into constructor of MyScanner, initializes the tokenTypes hash map with all Type values */
%init{
    tokenTypes = new HashMap<>();
    tokenTypes.put("and", Type.AND);
    tokenTypes.put("array", Type.ARRAY);
    tokenTypes.put("begin", Type.BEGIN);
    tokenTypes.put("div", Type.DIV);
    tokenTypes.put("do", Type.DO);
    tokenTypes.put("else", Type.ELSE);
    tokenTypes.put("end", Type.END);
    tokenTypes.put("function", Type.FUNCTION);
    tokenTypes.put("if", Type.IF);
    tokenTypes.put("integer", Type.INTEGER);
    tokenTypes.put("mod", Type.MOD);
    tokenTypes.put("not", Type.NOT);
    tokenTypes.put("of", Type.OF);
    tokenTypes.put("or", Type.OR);
    tokenTypes.put("procedure", Type.PROCEDURE);
    tokenTypes.put("program", Type.PROGRAM);
    tokenTypes.put("real", Type.REAL);
    tokenTypes.put("then", Type.THEN);
    tokenTypes.put("var", Type.VAR);
    tokenTypes.put("while", Type.WHILE);
    tokenTypes.put(";", Type.SEMI);
    tokenTypes.put(",", Type.COMMA);
    tokenTypes.put(".", Type.PERIOD);
    tokenTypes.put(":", Type.COLON);
    tokenTypes.put("[", Type.LBRACE);
    tokenTypes.put("]", Type.RBRACE);
    tokenTypes.put("(", Type.LPAREN);
    tokenTypes.put(")", Type.RPAREN);
    tokenTypes.put("+", Type.PLUS);
    tokenTypes.put("-", Type.MINUS);
    tokenTypes.put("=", Type.EQUAL);
    tokenTypes.put("<>", Type.NOTEQ);
    tokenTypes.put("<", Type.LTHAN);
    tokenTypes.put("<=", Type.LTHANEQ);
    tokenTypes.put(">", Type.GTHAN);
    tokenTypes.put(">=", Type.GTHANEQ);
    tokenTypes.put("*", Type.ASTERISK);
    tokenTypes.put("/", Type.FSLASH);
    tokenTypes.put(":=", Type.ASSIGN);
    tokenTypes.put("read", Type.READ);
    tokenTypes.put("write", Type.WRITE);
%init}

/* Patterns */
other               = .
letter              = [A-Za-z]
digit               = [0-9]
digits              = {digit}{digit}*
optional_fraction   = (\.{digits})?
optional_exponent   = ((E[\+\-]?){digits})?
num                 = {digits}{optional_fraction}{optional_exponent}
id                  = {letter}({letter} | {digit})*
symbol              = [=<>+\-*/;,.\[\]():]
symbols             = {symbol}|:=|<=|>=|<>
commentContent      = [^\{\}]
comment             = \{{commentContent}*\}
whitespace          = [ \n\t\r\f]|{comment}

%%

/* Lexical Rules */
{id}        {
                String lexeme = yytext();
                Type type = tokenTypes.get(lexeme);
                // If lexeme found in hashmap, lexeme is a keyword
                if(type != null)
                    return (new Token(lexeme, type, lineNumber));
                // Otherwise lexeme is an ID
                return(new Token(lexeme, Type.ID, lineNumber));
            }
            
{symbols}   {
                return(new Token(yytext(), tokenTypes.get(yytext()), lineNumber));
            }
            
{num}       {
                return(new Token(yytext(), Type.NUMBER, lineNumber));
            }

{whitespace} {
                /* Ignore Whitespace */
                if(yytext().equals("\n"))
                    lineNumber++;
            }

{other}     {
                /* Display an error message and exit program when error found */
                System.out.println("Invalid syntax found.");
                System.exit(1);
            }