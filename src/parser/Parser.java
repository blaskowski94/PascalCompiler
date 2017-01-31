package parser;

import scanner.MyScanner;
import scanner.Token;
import scanner.Type;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The parser recognizes whether an input string of tokens
 * is a valid Mini-Pascal program as defined in the grammar.
 * <p>
 * To use a parser, create an instance pointing at a file,
 * and then call the top-level function, <code>exp()</code>.
 * If the functions returns without an error, the file
 * contains an acceptable expression.
 *
 * @author Bob Laskowski
 */
public class Parser {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private Token lookahead;

    private MyScanner scanner;

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    public Parser(String filename) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException ex) {
            error("No file");
        }
        InputStreamReader isr = new InputStreamReader(fis);
        scanner = new MyScanner(isr);

        try {
            lookahead = scanner.nextToken();
        } catch (IOException ex) {
            error("Scan error");
        }

    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    public void program() {
        // Program header: program id;
        if (lookahead.getType() == Type.PROGRAM)
            match(Type.PROGRAM);
        else
            error("program 1");
        if (lookahead.getType() == Type.ID)
            match(Type.ID);
        else
            error("program 2");
        if (lookahead.getType() == Type.SEMI)
            match(Type.SEMI);
        else
            error("program 3");

        declarations();
        subprogram_declarations();
        compound_statement();

        if (lookahead.getType() == Type.PERIOD)
            match(Type.PERIOD);
        else
            error("program 4");
    }

    public void identifier_list() {
        if (lookahead.getType() == Type.ID) {
            match(Type.ID);
            if (lookahead.getType() == Type.COMMA) {
                match(Type.COMMA);
                identifier_list();
            }
        } else
            error("identifier_list 2");
    }

    public void declarations() {
        if (lookahead.getType() == Type.VAR) {
            match(Type.VAR);
            identifier_list();
            if (lookahead.getType() == Type.COLON) {
                match(Type.COLON);
                type();
                if (lookahead.getType() == Type.SEMI) {
                    match(Type.SEMI);
                    declarations();
                } else
                    error("declarations 2");
            } else
                error("declarations 1");
        } else {
            // lambda case
        }
    }

    public void type() {
        if (lookahead.getType() == Type.ARRAY) {
            match(Type.ARRAY);
            if (lookahead.getType() == Type.LBRACE) {
                match(Type.LBRACE);
                if (lookahead.getType() == Type.NUMBER) {
                    match(Type.NUMBER);
                    if (lookahead.getType() == Type.COLON) {
                        match(Type.COLON);
                        if (lookahead.getType() == Type.NUMBER) {
                            match(Type.NUMBER);
                            if (lookahead.getType() == Type.RBRACE) {
                                match(Type.RBRACE);
                                if (lookahead.getType() == Type.OF) {
                                    match(Type.OF);
                                    standard_type();
                                } else
                                    error("type 6");
                            } else
                                error("type 5");
                        } else
                            error("type 4");
                    } else
                        error("type 3");
                } else
                    error("type 2");
            } else
                error("type 1");
        } else if (lookahead.getType() == Type.INTEGER || lookahead.getType() == Type.REAL)
            standard_type();
        else
            error("type 7");
    }

    public void standard_type() {
        if (lookahead.getType() == Type.INTEGER)
            match(Type.INTEGER);
        else if (lookahead.getType() == Type.REAL)
            match(Type.REAL);
        else
            error("standard_type");
    }

    public void subprogram_declarations() {
        if (lookahead.getType() == Type.FUNCTION || lookahead.getType() == Type.PROCEDURE) {
            subprogram_declaration();
            if (lookahead.getType() == Type.SEMI) {
                match(Type.SEMI);
                subprogram_declarations();
            } else
                error("subprogram_declarations");
        } else {
            // lambda case
        }
    }

    public void subprogram_declaration() {
        subprogram_head();
        declarations();
        subprogram_declarations();
        compound_statement();
    }

    public void subprogram_head() {
        if (lookahead.getType() == Type.FUNCTION) {
            match(Type.FUNCTION);
            if (lookahead.getType() == Type.ID) {
                match(Type.ID);
                arguments();
                if (lookahead.getType() == Type.COLON) {
                    match(Type.COLON);
                    standard_type();
                    if (lookahead.getType() == Type.SEMI)
                        match(Type.SEMI);
                    else
                        error("subprogram_head 4");
                } else
                    error("subprogram_head 3");
            } else
                error("subprogram_head 2");
        } else if (lookahead.getType() == Type.PROCEDURE) {
            match(Type.PROCEDURE);
            if (lookahead.getType() == Type.ID) {
                match(Type.ID);
                arguments();
                if (lookahead.getType() == Type.SEMI)
                    match(Type.SEMI);
                else
                    error("subprogram_head 6");
            } else
                error("subprogram_head 5");
        } else
            error("subprogram_head 1");
    }

    public void arguments() {
        if (lookahead.getType() == Type.LPAREN) {
            match(Type.LPAREN);
            parameter_list();
            if (lookahead.getType() == Type.RPAREN)
                match(Type.RPAREN);
            else
                error("arguments");
        } else {
            // lambda case
        }
    }

    public void parameter_list() {
        if (lookahead.getType() == Type.ID) {
            identifier_list();
            if (lookahead.getType() == Type.COLON) {
                match(Type.COLON);
                type();
                if (lookahead.getType() == Type.SEMI) {
                    match(Type.SEMI);
                    parameter_list();
                }
            } else
                error("parameter_list 1");
        } else
            error("parameter_list 3");
    }

    public void compound_statement() {
        if (lookahead.getType() == Type.BEGIN) {
            match(Type.BEGIN);
            optional_statements();
            if (lookahead.getType() == Type.END)
                match(Type.END);
            else
                error("compound_statement 2");
        } else
            error("compound_statement 1");
    }

    public void optional_statements() {
        if (lookahead.getType() == Type.ID || lookahead.getType() == Type.BEGIN || lookahead.getType() == Type.IF || lookahead.getType() == Type.WHILE)
            statement_list();
        else {
            // Lambda case
        }
    }

    public void statement_list() {
        if (lookahead.getType() == Type.ID || lookahead.getType() == Type.IF || lookahead.getType() == Type.WHILE || lookahead.getType() == Type.BEGIN)
            statement();
        if (lookahead.getType() == Type.SEMI) {
            match(Type.SEMI);
            statement_list();
        }
    }

    public void statement() {
        if (lookahead.getType() == Type.ID) {
            variable();
            if (lookahead.getType() == Type.ASSIGN)
                match(Type.ASSIGN);
            else
                error("statement 1");
            expression();
        } else if (lookahead.getType() == Type.BEGIN)
            compound_statement();
        else if (lookahead.getType() == Type.IF) {
            match(Type.IF);
            expression();
            if (lookahead.getType() == Type.THEN) {
                match(Type.THEN);
                statement();
                if (lookahead.getType() == Type.ELSE) {
                    match(Type.ELSE);
                    statement();
                } else
                    error("statement 3");
            } else
                error("statement 2");
        } else
            error("statement 4");
    }

    public void variable() {
        if (lookahead.getType() == Type.ID) {
            match(Type.ID);
            if (lookahead.getType() == Type.LBRACE) {
                match(Type.LBRACE);
                expression();
                if (lookahead.getType() == Type.RBRACE)
                    match(Type.RBRACE);
                else
                    error("variable 2");
            }

        } else
            error("variable 1");
    }


    // Not used yet
    public void procedure_statement() {
        if (lookahead.getType() == Type.ID) {
            match(Type.ID);
            if (lookahead.getType() == Type.LPAREN) {
                match(Type.LPAREN);
                expression_list();
                if (lookahead.getType() == Type.RPAREN)
                    match(Type.RPAREN);
                else
                    error("procedure_statement 2");
            }

        } else
            error("procedure_statement 1");
    }

    public void expression_list() {
        if (lookahead.getType() == Type.PLUS || lookahead.getType() == Type.MINUS || lookahead.getType() == Type.ID || lookahead.getType() == Type.NUMBER || lookahead.getType() == Type.LPAREN || lookahead.getType() == Type.NOT) {
            expression();
            if (lookahead.getType() == Type.COMMA) {
                match(Type.COMMA);
                expression_list();
            }
        }
    }

    // How do we match relop??
    public void expression() {
        if (lookahead.getType() == Type.PLUS || lookahead.getType() == Type.MINUS || lookahead.getType() == Type.ID || lookahead.getType() == Type.NUMBER || lookahead.getType() == Type.LPAREN || lookahead.getType() == Type.NOT) {
            simple_expression();
            if (isRelOp(lookahead.getType())) {
                match(lookahead.getType());
                simple_expression();
            }
        }
    }

    public void simple_expression() {
        if (lookahead.getType() == Type.ID || lookahead.getType() == Type.NUMBER || lookahead.getType() == Type.LPAREN || lookahead.getType() == Type.NOT) {
            term();
            simple_part();
        } else if (lookahead.getType() == Type.PLUS || lookahead.getType() == Type.MINUS) {
            sign();
            term();
            simple_part();
        }
    }

    // How do we match addop??
    public void simple_part() {
        if (isAddOp(lookahead.getType())) {
            match(lookahead.getType());
            term();
            simple_part();
        } else {
            // lambda case
        }
    }

    public void term() {
        factor();
        term_part();
    }

    // How do we match mulop??
    public void term_part() {
        if (isMulOp(lookahead.getType())) {
            match(lookahead.getType());
            factor();
            term_part();
        } else {
            // lambda case
        }
    }

    public void factor() {
        if (lookahead.getType() == Type.ID) {
            match(Type.ID);
            if (lookahead.getType() == Type.LBRACE) {
                match(Type.LBRACE);
                expression();
                if (lookahead.getType() == Type.RBRACE) {
                    match(Type.RBRACE);
                } else
                    error("factor 1");
            } else if (lookahead.getType() == Type.LPAREN) {
                match(Type.LPAREN);
                expression_list();
                if (lookahead.getType() == Type.RPAREN)
                    match(Type.RPAREN);
                else
                    error("factor 2");
            }
        } else if (lookahead.getType() == Type.NUMBER)
            match(Type.NUMBER);
        else if (lookahead.getType() == Type.LPAREN) {
            match(Type.LPAREN);
            expression();
            if (lookahead.getType() == Type.RPAREN)
                match(Type.RPAREN);
            else
                error("factor 3");
        } else if (lookahead.getType() == Type.NOT) {
            match(Type.NOT);
            factor();
        } else
            error("factor 4");
    }

    public void sign() {
        if (lookahead.getType() == Type.PLUS)
            match(Type.PLUS);
        else if (lookahead.getType() == Type.MINUS)
            match(Type.MINUS);
        else
            error("sign");
    }


    public boolean isRelOp(Type t) {
        if (t == Type.EQUAL || t == Type.NOTEQ || t == Type.LTHAN || t == Type.LTHANEQ || t == Type.GTHANEQ || t == Type.GTHAN)
            return true;
        return false;
    }

    public boolean isAddOp(Type t) {
        if (t == Type.PLUS || t == Type.MINUS || t == Type.OR)
            return true;
        return false;
    }

    public boolean isMulOp(Type t) {
        if (t == Type.ASTERISK || t == Type.FSLASH || t == Type.DIV || t == Type.MOD || t == Type.AND)
            return true;
        return false;
    }

    /**
     * Matches the expected token.
     * If the current token in the input stream from the scanner
     * matches the token that is expected, the current token is
     * consumed and the scanner will move on to the next token
     * in the input.
     * The null at the end of the file returned by the
     * scanner is replaced with a fake token containing no
     * type.
     *
     * @param expected The expected token type.
     */
    public void match(Type expected) {
        System.out.println("Match " + expected + " " + lookahead.getLexeme());
        if (this.lookahead.getType() == expected) {
            try {
                this.lookahead = scanner.nextToken();
                if (this.lookahead == null) {
                    this.lookahead = new Token("End of File", null);
                }
            } catch (IOException ex) {
                error("Scanner exception");
            }
        } else {
            error("Match of " + expected + " found " + this.lookahead.getType()
                    + " instead.");
        }
    }

    /**
     * Errors out of the parser.
     * Prints an error message and then exits the program.
     *
     * @param message The error message to print.
     */
    public void error(String message) {
        System.out.println("Error " + message);
        System.exit(1);
    }
}
