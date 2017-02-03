package parser;

import scanner.MyScanner;
import scanner.Token;
import scanner.Type;

import java.io.*;

import static scanner.Type.*;

/**
 * Bob Laskowski
 * Compilers II
 * Dr. Erik Steinmetz
 * February 3rd, 2017
 * <p>
 * The parser recognizes whether an input string of tokens is a valid Mini-Pascal program as defined in the grammar.
 * <p>
 * To use a parser, create an instance pointing at a file or a String of code, and then call the top-level function,
 * <code>program()</code>. If the functions returns without an error, the file contains an acceptable expression.
 * <p>
 * The terminal symbols described in the grammar rule are denoted in <b>bold</b> and the non-terminal symbols are
 * regular text. Options are denoted with a vertical bar |. An empty option is denoted "lambda." See the grammar in
 * the documentation folder for more information on definitions.
 *
 * @author Bob Laskowski
 */
public class Parser {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private Token lookahead;
    private MyScanner scanny;

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Creates a new Parser object to parse either a file or a String input. The boolean value passed in is true
     * if a file path is being passed in as the String and false if pascal code is passed in as the String.
     *
     * @param input Either a filename in src/parser/test or a String to parse
     * @param file  Make true if using file as input, false if using String
     */
    public Parser(String input, boolean file) {
        InputStreamReader isr;

        if (file) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(input);
            } catch (FileNotFoundException ex) {
                error("No file");
            }
            isr = new InputStreamReader(fis);
            scanny = new MyScanner(isr);
        } else {
            scanny = new MyScanner(new StringReader(input));
        }

        try {
            lookahead = scanny.nextToken();
        } catch (IOException ex) {
            error("Scan error");
        }
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Determines whether the given Type passed in is a relational operator. Relational operators include:
     * =, !=, <, <=, >, >=
     * <p>
     * Function is normally called by getting Type of a token: isRelOp(someToken.getType())
     *
     * @param t The Type of a Token from the Type Enum
     * @return True if Type is a relop, false otherwise
     */
    private static boolean isRelOp(Type t) {
        return t == EQUAL || t == NOTEQ || t == LTHAN || t == LTHANEQ || t == GTHANEQ || t == GTHAN;
    }

    /**
     * Determines whether the given Type passed in is a add operator. Add operators include: +, -, or
     * <p>
     * Function is normally called by getting Type of a token: isAddOp(someToken.getType())
     *
     * @param t The Type of a Token from the Type Enum
     * @return True if Type is a addop, false otherwise
     */
    private static boolean isAddOp(Type t) {
        return t == PLUS || t == MINUS || t == OR;
    }

    /**
     * Determines whether the given Type passed in is a multiplication operator. Multiplication operators include:
     * *, /, div, mod, and
     * <p>
     * Function is normally called by getting Type of a token: isMulOp(someToken.getType())
     *
     * @param t The Type of a Token from the Type Enum
     * @return True if Type is a mulop, false otherwise
     */
    private static boolean isMulOp(Type t) {
        return t == ASTERISK || t == FSLASH || t == DIV || t == MOD || t == AND;
    }

    /**
     * A program contains the following:
     * <p>
     * <b>program id ;</b>
     * declarations
     * subprogram_declarations
     * compound_statement
     * <b>.</b>
     */
    public void program() {
        match(PROGRAM);
        match(ID);
        match(SEMI);
        declarations();
        subprogram_declarations();
        compound_statement();
        match(PERIOD);
    }

    /**
     * An identifier_list contains the following:
     * <p>
     * <b>id</b> | <b>id , </b> identifier_list
     */
    public void identifier_list() {
        match(ID);
        if (lookahead.getType() == COMMA) {
            match(COMMA);
            identifier_list();
        }
        // else lambda case
    }

    /**
     * Declarations contain the following:
     * <p>
     * <b>var</b> identifier_list <b>:</b> type <b>;</b> declarations | lambda
     */
    public void declarations() {
        if (lookahead.getType() == VAR) {
            match(VAR);
            identifier_list();
            match(COLON);
            type();
            match(SEMI);
            declarations();
        }
        // else lambda case
    }

    /**
     * A type contains the following:
     * <p>
     * standard_type | <b>array[num:num] of</b> standard_type
     */
    public void type() {
        if (lookahead.getType() == ARRAY) {
            match(ARRAY);
            match(LBRACE);
            match(NUMBER);
            match(COLON);
            match(NUMBER);
            match(RBRACE);
            match(OF);
            standard_type();
        } else if (lookahead.getType() == INTEGER || lookahead.getType() == REAL) standard_type();
        else error("type 7");
    }

    /**
     * A standard_type contains the following:
     * <p>
     * <b>integer</b> | <b>real</b>
     */
    public void standard_type() {
        if (lookahead.getType() == INTEGER) match(INTEGER);
        else if (lookahead.getType() == REAL) match(REAL);
        else error("standard_type");
    }

    /**
     * Subprogram_declarations contain the following:
     * <p>
     * subprogram_declaration <b>;</b> subprogram_declarations | lambda
     */
    public void subprogram_declarations() {
        if (lookahead.getType() == FUNCTION || lookahead.getType() == PROCEDURE) {
            subprogram_declaration();
            match(SEMI);
            subprogram_declarations();
        }
        // else lambda case
    }

    /**
     * A subprogram_declaration contains the following:
     * <p>
     * subprogram_head
     * declarations
     * subprogram_declarations
     * compound_statement
     */
    public void subprogram_declaration() {
        subprogram_head();
        declarations();
        subprogram_declarations();
        compound_statement();
    }

    /**
     * A subprogram_head contains the following:
     * <p>
     * <b>function id</b> arguments <b>:</b> standard_type <b>;</b> |
     * <b>procedure id</b> arguments <b>;</b>
     */
    public void subprogram_head() {
        if (lookahead.getType() == FUNCTION) {
            match(FUNCTION);
            match(ID);
            arguments();
            match(COLON);
            standard_type();
            match(SEMI);
        } else if (lookahead.getType() == PROCEDURE) {
            match(PROCEDURE);
            match(ID);
            arguments();
            match(SEMI);
        } else error("subprogram_head");
    }

    /**
     * Arguments contain the following:
     * <p>
     * <b>(</b> parameter_list <b>)</b> | lambda
     */
    public void arguments() {
        if (lookahead.getType() == LPAREN) {
            match(LPAREN);
            parameter_list();
            match(RPAREN);
        }
        // else lambda case
    }

    /**
     * A parameter_list contains the following:
     * <p>
     * identifier_list <b>:</b> type |
     * identifier_list <b>:</b> type <b>;</b> parameter_list
     */
    public void parameter_list() {
        identifier_list();
        match(COLON);
        type();
        if (lookahead.getType() == SEMI) {
            match(SEMI);
            parameter_list();
        }
    }

    /**
     * A compound_statement contains the following:
     * <p>
     * <b>begin</b> optional_statements <b>end</b>
     */
    public void compound_statement() {
        match(BEGIN);
        optional_statements();
        match(END);
    }

    /**
     * Optional_statements contain the following:
     * <p>
     * statement_list | lambda
     */
    public void optional_statements() {
        if (lookahead.getType() == ID || lookahead.getType() == BEGIN || lookahead.getType() == IF || lookahead.getType() == WHILE)
            statement_list();
        // else lambda case
    }

    /**
     * A statement_list contains the following:
     * <p>
     * statement_list | statement <b>;</b> statement_list
     */
    public void statement_list() {
        statement();
        if (lookahead.getType() == SEMI) {
            match(SEMI);
            statement_list();
        }
        // else lambda case
    }

    /**
     * A statement contains the following:
     * <p>
     * variable <b>assignop</b> expression |
     * procedure_statement |
     * compound_statement |
     * <b>if</b> expression <b>then</b> statement <b>else</b> statement |
     * <b>while</b> expression <b>do</b> statement |
     * read(id) |
     * write(expression)
     * <p>
     * Note: for the time being we have not included procedure_statement, read(id) or write(id) in our parser. It will
     * not recognize these in Pascal code and will throw an error. This has been done until we have a way to eliminate
     * ambiguity and recognize built-in functions.
     */
    public void statement() {
        if (lookahead.getType() == ID) {
            variable();
            match(ASSIGN);
            expression();
        } else if (lookahead.getType() == BEGIN) compound_statement();
        else if (lookahead.getType() == IF) {
            match(IF);
            expression();
            match(THEN);
            statement();
            match(ELSE);
            statement();
        } else if (lookahead.getType() == WHILE) {
            match(WHILE);
            expression();
            match(DO);
            statement();
        } else {
            error("statement");
        }
    }

    /**
     * A variable includes the following:
     * <p>
     * <b>id</b> |
     * <b>id [</b> expression <b>]</b>
     */
    public void variable() {
        match(ID);
        if (lookahead.getType() == LBRACE) {
            match(LBRACE);
            expression();
            match(RBRACE);
        }
        // else lambda case
    }

    /**
     * A procedure_statement contains the following:
     * <p>
     * <b>id</b> |
     * <b>id (</b> expression_list <b>)</b>
     * <p>
     * Note: this has not yet been implemented due to ambiguity in the grammar (no way to choose between variable and
     * procedure_statement at the moment).
     */
    public void procedure_statement() {
        match(ID);
        if (lookahead.getType() == LPAREN) {
            match(LPAREN);
            expression_list();
            match(RPAREN);
        }
    }

    /**
     * An expression_list contains the following:
     * <p>
     * expression | expression <b>,</b> expression_list
     */
    public void expression_list() {
        expression();
        if (lookahead.getType() == COMMA) {
            match(COMMA);
            expression_list();
        }
    }

    /**
     * An expression contains the following:
     * <p>
     * simple_expression | simple_expression <b>relop</b> simple_expression
     */
    public void expression() {
        simple_expression();
        if (isRelOp(lookahead.getType())) {
            match(lookahead.getType());
            simple_expression();
        }
    }

    /**
     * A simple_expression contains the following:
     * <p>
     * term simple_part | sign term simple_part
     */
    public void simple_expression() {
        if (lookahead.getType() == ID || lookahead.getType() == NUMBER || lookahead.getType() == LPAREN || lookahead.getType() == NOT) {
            term();
            simple_part();
        } else if (lookahead.getType() == PLUS || lookahead.getType() == MINUS) {
            sign();
            term();
            simple_part();
        } else error("simple_expression");
    }

    /**
     * A simple_part contains the following:
     * <p>
     * <b>addop</b> term simple_part | lambda
     */
    public void simple_part() {
        if (isAddOp(lookahead.getType())) {
            match(lookahead.getType());
            term();
            simple_part();
        }
        // else lambda case
    }

    /**
     * A term contains the following:
     * <p>
     * factor term_part
     */
    public void term() {
        factor();
        term_part();
    }

    /**
     * A term_part contains the following:
     * <p>
     * <b>mulop</b> factor term_part | lambda
     */
    public void term_part() {
        if (isMulOp(lookahead.getType())) {
            match(lookahead.getType());
            factor();
            term_part();
        }
        // else lambda case
    }

    /**
     * A factor contains the following:
     * <p>
     * <b>id</b> | <b>id [</b> expression <b>]</b> | <b>id (</b> expression_list <b>)</b> |
     * <b>num</b> | <b>(</b> expression <b>)</b> | <b>not</b> factor
     */
    public void factor() {
        if (lookahead.getType() == ID) {
            match(ID);
            if (lookahead.getType() == LBRACE) {
                match(LBRACE);
                expression();
                match(RBRACE);
            } else if (lookahead.getType() == LPAREN) {
                match(LPAREN);
                expression_list();
                match(RPAREN);
            }
        } else if (lookahead.getType() == NUMBER) match(NUMBER);
        else if (lookahead.getType() == LPAREN) {
            match(LPAREN);
            expression();
            match(RPAREN);
        } else if (lookahead.getType() == NOT) {
            match(NOT);
            factor();
        } else error("factor");
    }

    /**
     * A sign contains the following:
     * <p>
     * <b>+</b> | <b>-</b>
     */
    public void sign() {
        if (lookahead.getType() == PLUS) match(PLUS);
        else if (lookahead.getType() == MINUS) match(MINUS);
        else error("sign");
    }

    /**
     * Matches the expected token. If the current token in the input stream from the scanner matches the token that is
     * expected, the current token is consumed and the scanner will move on to the next token in the input.
     * The null at the end of the file returned by the scanner is replaced with a fake token containing no
     * type.
     *
     * @param expected The expected token type.
     */
    private void match(Type expected) {
        System.out.println("Match " + expected + " " + lookahead.getLexeme());
        if (this.lookahead.getType() == expected) {
            try {
                this.lookahead = scanny.nextToken();
                if (this.lookahead == null) {
                    this.lookahead = new Token("End of File", null);
                }
            } catch (IOException ex) {
                error("Scanner exception");
            }
        } else {
            error("Match of " + expected + " found " + this.lookahead.getType() + " instead.");
        }
    }

    /**
     * Errors out of the parser. Prints an error message and then exits the program.
     *
     * @param message The error message to print.
     */
    private void error(String message) {
        System.out.println("Error " + message);
        System.exit(1);
    }
}
