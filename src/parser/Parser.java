package parser;

import scanner.MyScanner;
import scanner.Token;
import scanner.Type;
import symboltable.SymbolTable;

import java.io.*;
import java.util.ArrayList;

import static scanner.Type.*;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * February 3rd, 2017
 * <p>
 * The parser recognizes whether an input string of tokens is a valid Mini-Pascal program as defined in the grammar.
 * <p>
 * To use a parser, create an instance pointing at a file or a String of code, and then call the top-level function,
 * <code>program()</code>. If the functions returns without an error, the file contains an acceptable expression.
 * <p>
 * The terminal symbols described in the grammar rule are denoted in <strong>bold</strong> and the non-terminal symbols are
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
    private SymbolTable symbolTable;

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
            assert fis != null;
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
        symbolTable = new SymbolTable();
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
     * <strong>program id ;</strong>
     * declarations
     * subprogram_declarations
     * compound_statement
     * <strong>.</strong>
     */
    public void program() {
        match(PROGRAM);
        String name = lookahead.getLexeme();
        match(ID);
        if (!symbolTable.addProgram(name)) error("Name already exists in symbol table");
        match(SEMI);
        declarations();
        subprogram_declarations();
        compound_statement();
        match(PERIOD);
    }

    /**
     * An identifier_list contains the following:
     * <p>
     * <strong>id</strong> | <strong>id , </strong> identifier_list
     * <p>
     * This creates an ArrayList of all the variable IDs found in the identifier list and returns it to the calling
     * function so the type can be determined and added to the symbol table.
     * </p>
     *
     * @return An ArrayList of names of ids declared
     */
    public ArrayList<String> identifier_list() {
        ArrayList<String> idList = new ArrayList<>();
        idList.add(lookahead.getLexeme());
        match(ID);
        if (lookahead.getType() == COMMA) {
            match(COMMA);
            idList.addAll(identifier_list());
        }
        // else lambda case
        return idList;
    }

    /**
     * Declarations contain the following:
     * <p>
     * <strong>var</strong> identifier_list <strong>:</strong> type <strong>;</strong> declarations | lambda
     */
    public void declarations() {
        if (lookahead.getType() == VAR) {
            match(VAR);
            ArrayList<String> idList = identifier_list();
            match(COLON);
            type(idList);
            match(SEMI);
            declarations();
        }
        // else lambda case
    }

    /**
     * A type contains the following:
     * <p>
     * standard_type | <strong>array[num:num] of</strong> standard_type
     *
     * @param idList An ArrayList of ID names to be added to symbol table
     */
    public void type(ArrayList<String> idList) {
        int beginidx, endidx;
        if (lookahead.getType() == ARRAY) {
            match(ARRAY);
            match(LBRACE);
            beginidx = Integer.parseInt(lookahead.getLexeme());
            match(NUMBER);
            match(COLON);
            endidx = Integer.parseInt(lookahead.getLexeme());
            match(NUMBER);
            match(RBRACE);
            match(OF);
            Type t = standard_type();
            for (String anIdList : idList) {
                if (!symbolTable.addArray(anIdList, t, beginidx, endidx)) error("Name already exists in symbol table");
            }
        } else if (lookahead.getType() == INTEGER || lookahead.getType() == REAL) {
            Type t = standard_type();
            for (String anIdList : idList) {
                if (!symbolTable.addVariable(anIdList, t)) error("Name already exists in symbol table");
            }
        } else error("type");
    }

    /**
     * A standard_type contains the following:
     * <p>
     * <strong>integer</strong> | <strong>real</strong>
     *
     * @return Returns the Type of the declared item, either Type.INTEGER or Type.REAL
     */
    public Type standard_type() {
        Type t = null;
        if (lookahead.getType() == INTEGER) {
            t = INTEGER;
            match(INTEGER);
        } else if (lookahead.getType() == REAL) {
            t = REAL;
            match(REAL);
        } else error("standard_type");
        return t;
    }

    /**
     * Subprogram_declarations contain the following:
     * <p>
     * subprogram_declaration <strong>;</strong> subprogram_declarations | lambda
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
     * <strong>function id</strong> arguments <strong>:</strong> standard_type <strong>;</strong> |
     * <strong>procedure id</strong> arguments <strong>;</strong>
     */
    public void subprogram_head() {
        if (lookahead.getType() == FUNCTION) {
            match(FUNCTION);
            String funcName = lookahead.getLexeme();
            match(ID);
            arguments();
            match(COLON);
            Type t = standard_type();
            symbolTable.addFunction(funcName, t);
            match(SEMI);
        } else if (lookahead.getType() == PROCEDURE) {
            match(PROCEDURE);
            String procName = lookahead.getLexeme();
            match(ID);
            arguments();
            symbolTable.addProcedure(procName);
            match(SEMI);
        } else error("subprogram_head");
    }

    /**
     * Arguments contain the following:
     * <p>
     * <strong>(</strong> parameter_list <strong>)</strong> | lambda
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
     * identifier_list <strong>:</strong> type |
     * identifier_list <strong>:</strong> type <strong>;</strong> parameter_list
     */
    public void parameter_list() {
        ArrayList<String> idList = identifier_list();
        match(COLON);
        type(idList);
        if (lookahead.getType() == SEMI) {
            match(SEMI);
            parameter_list();
        }
    }

    /**
     * A compound_statement contains the following:
     * <p>
     * <strong>begin</strong> optional_statements <strong>end</strong>
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
     * statement_list | statement <strong>;</strong> statement_list
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
     * variable <strong>assignop</strong> expression |
     * procedure_statement |
     * compound_statement |
     * <strong>if</strong> expression <strong>then</strong> statement <strong>else</strong> statement |
     * <strong>while</strong> expression <strong>do</strong> statement |
     * read(id) |
     * write(expression)
     * <p>
     * Note: for the time being we have not included procedure_statement, read(id) or write(id) in our parser. It will
     * not recognize these in Pascal code and will throw an error. This has been done until we have a way to eliminate
     * ambiguity and recognize built-in functions.
     */
    public void statement() {
        if (lookahead.getType() == ID) {
            if (symbolTable.isVariableName(lookahead.getLexeme())) {
                variable();
                match(ASSIGN);
                expression();
            } else if (symbolTable.isProcedureName(lookahead.getLexeme())) {
                procedure_statement();
            } else error("Name not found in symbol table.");
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
     * <strong>id</strong> |
     * <strong>id [</strong> expression <strong>]</strong>
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
     * <strong>id</strong> |
     * <strong>id (</strong> expression_list <strong>)</strong>
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
     * expression | expression <strong>,</strong> expression_list
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
     * simple_expression | simple_expression <strong>relop</strong> simple_expression
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
     * <strong>addop</strong> term simple_part | lambda
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
     * <strong>mulop</strong> factor term_part | lambda
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
     * <strong>id</strong> | <strong>id [</strong> expression <strong>]</strong> | <strong>id (</strong> expression_list <strong>)</strong> |
     * <strong>num</strong> | <strong>(</strong> expression <strong>)</strong> | <strong>not</strong> factor
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
     * <strong>+</strong> | <strong>-</strong>
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

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
