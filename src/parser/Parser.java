package parser;

import scanner.MyScanner;
import scanner.Token;
import scanner.Type;
import symboltable.SymbolTable;
import syntaxtree.*;

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
                error("File \"" + input + "\" not found. Check file name and path to ensure it exists.");
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
    public ProgramNode program() {
        match(PROGRAM);
        String name = lookahead.getLexeme();
        match(ID);
        if (!symbolTable.addProgram(name)) error("Name already exists in symbol table");
        ProgramNode program = new ProgramNode(name);
        match(SEMI);
        program.setVariables(declarations());
        program.setFunctions(subprogram_declarations());
        program.setMain(compound_statement());
        match(PERIOD);


        // Write syntax tree and contents of symbol table to files
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/compiler/output/" + name + ".tree"), "utf-8"))) {
            writer.write(program.indentedToString(0));
        } catch (Exception ex) {
            error("Problem with output file.");
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/compiler/output/" + name + ".table"), "utf-8"))) {
            writer.write(symbolTable.toString());
        } catch (Exception ex) {
            error("Problem with output file.");
        }

        return program;
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
    public DeclarationsNode declarations() {
        DeclarationsNode dec = new DeclarationsNode();
        if (lookahead.getType() == VAR) {
            match(VAR);
            ArrayList<String> idList = identifier_list();
            match(COLON);
            Type t = type(idList);
            for (String id : idList) {
                dec.addVariable(new VariableNode(id, t));
            }
            match(SEMI);
            dec.addDeclarations(declarations());
        }
        // else lambda case
        return dec;
    }

    /**
     * A type contains the following:
     * <p>
     * standard_type | <strong>array[num:num] of</strong> standard_type
     *
     * @param idList An ArrayList of ID names to be added to symbol table
     */
    public Type type(ArrayList<String> idList) {
        int beginidx, endidx;
        Type t = null;
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
            t = standard_type();
            for (String anIdList : idList) {
                if (!symbolTable.addArray(anIdList, t, beginidx, endidx)) error("Name already exists in symbol table");
            }
        } else if (lookahead.getType() == INTEGER || lookahead.getType() == REAL) {
            t = standard_type();
            for (String anIdList : idList) {
                if (!symbolTable.addVariable(anIdList, t)) error("Name already exists in symbol table");
            }
        } else error("type");
        return t;
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
    public SubProgramDeclarationsNode subprogram_declarations() {
        SubProgramDeclarationsNode spdNode = new SubProgramDeclarationsNode();
        if (lookahead.getType() == FUNCTION || lookahead.getType() == PROCEDURE) {
            spdNode.addSubProgramDeclaration(subprogram_declaration());
            match(SEMI);
            spdNode.addall(subprogram_declarations().getProcs());
        }
        // else lambda case
        return spdNode;
    }

    /**
     * A subprogram_declaration contains the following:
     * <p>
     * subprogram_head
     * declarations
     * subprogram_declarations
     * compound_statement
     */
    public SubProgramNode subprogram_declaration() {
        SubProgramNode spNode = subprogram_head();
        spNode.setVariables(declarations());
        spNode.setFunctions(subprogram_declarations());
        spNode.setMain(compound_statement());
        return spNode;
    }

    /**
     * A subprogram_head contains the following:
     * <p>
     * <strong>function id</strong> arguments <strong>:</strong> standard_type <strong>;</strong> |
     * <strong>procedure id</strong> arguments <strong>;</strong>
     */
    public SubProgramNode subprogram_head() {
        SubProgramNode spNode = null;
        if (lookahead.getType() == FUNCTION) {
            match(FUNCTION);
            String funcName = lookahead.getLexeme();
            spNode = new SubProgramNode(funcName);
            match(ID);
            arguments();
            match(COLON);
            Type t = standard_type();
            symbolTable.addFunction(funcName, t);
            match(SEMI);
        } else if (lookahead.getType() == PROCEDURE) {
            match(PROCEDURE);
            String procName = lookahead.getLexeme();
            spNode = new SubProgramNode(procName);
            match(ID);
            arguments();
            symbolTable.addProcedure(procName);
            match(SEMI);
        } else error("subprogram_head");
        return spNode;
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
    public CompoundStatementNode compound_statement() {
        CompoundStatementNode comp;
        match(BEGIN);
        comp = optional_statements();
        match(END);
        return comp;
    }

    /**
     * Optional_statements contain the following:
     * <p>
     * statement_list | lambda
     */
    public CompoundStatementNode optional_statements() {
        CompoundStatementNode comp = new CompoundStatementNode();
        if (lookahead.getType() == ID || lookahead.getType() == BEGIN || lookahead.getType() == IF || lookahead.getType() == WHILE)
            comp.addAll(statement_list());
        // else lambda case
        return comp;
    }

    /**
     * A statement_list contains the following:
     * <p>
     * statement_list | statement <strong>;</strong> statement_list
     */
    public ArrayList<StatementNode> statement_list() {
        ArrayList<StatementNode> nodes = new ArrayList();
        nodes.add(statement());
        if (lookahead.getType() == SEMI) {
            match(SEMI);
            nodes.addAll(statement_list());
        }
        // else lambda case
        return nodes;
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
    public StatementNode statement() {
        StatementNode state = null;
        if (lookahead.getType() == ID) {
            if (symbolTable.isVariableName(lookahead.getLexeme()) || symbolTable.isArrayName((lookahead.getLexeme()))) {
                AssignmentStatementNode assign = new AssignmentStatementNode();
                assign.setLvalue(variable());
                match(ASSIGN);
                assign.setExpression(expression());
                return assign;
            }
            else if (symbolTable.isProcedureName(lookahead.getLexeme())) {
                return procedure_statement();
            } else error("Name not found in symbol table.");
        } else if (lookahead.getType() == BEGIN) state = compound_statement();
        else if (lookahead.getType() == IF) {
            IfStatementNode ifState = new IfStatementNode();
            match(IF);
            ifState.setTest(expression());
            match(THEN);
            ifState.setThenStatement(statement());
            match(ELSE);
            ifState.setElseStatement(statement());
            return ifState;
        } else if (lookahead.getType() == WHILE) {
            WhileStatementNode whileState = new WhileStatementNode();
            match(WHILE);
            whileState.setTest(expression());
            match(DO);
            whileState.setDoStatement(statement());
            return whileState;
        } else {
            error("statement");
        }
        return state;
    }

    /**
     * A variable includes the following:
     * <p>
     * <strong>id</strong> |
     * <strong>id [</strong> expression <strong>]</strong>
     */
    public VariableNode variable() {
        VariableNode var = new VariableNode(lookahead.getLexeme());
        match(ID);
        if (lookahead.getType() == LBRACE) {
            match(LBRACE);
            expression();
            match(RBRACE);
        }
        // else lambda case
        return var;
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
    public ProcedureStatementNode procedure_statement() {
        ProcedureStatementNode psNode = new ProcedureStatementNode();
        String procName = lookahead.getLexeme();
        psNode.setVariable(new VariableNode(procName));
        match(ID);
        if (lookahead.getType() == LPAREN) {
            match(LPAREN);
            psNode.addAllExpNode(expression_list());
            match(RPAREN);
        }
        symbolTable.addProcedure(procName);
        return psNode;
    }

    /**
     * An expression_list contains the following:
     * <p>
     * expression | expression <strong>,</strong> expression_list
     */
    public ArrayList<ExpressionNode> expression_list() {
        ArrayList<ExpressionNode> exNodeList = new ArrayList();
        exNodeList.add(expression());
        if (lookahead.getType() == COMMA) {
            match(COMMA);
            exNodeList.addAll(expression_list());
        }
        return exNodeList;
    }

    /**
     * An expression contains the following:
     * <p>
     * simple_expression | simple_expression <strong>relop</strong> simple_expression
     */
    public ExpressionNode expression() {
        ExpressionNode left = simple_expression();
        if (isRelOp(lookahead.getType())) {
            OperationNode opNode = new OperationNode(lookahead.getType());
            opNode.setLeft(left);
            match(lookahead.getType());
            opNode.setRight(simple_expression());
            return opNode;
        }
        return left;
    }

    /**
     * A simple_expression contains the following:
     * <p>
     * term simple_part | sign term simple_part
     */
    public ExpressionNode simple_expression() {
        ExpressionNode expNode = null;
        if (lookahead.getType() == ID || lookahead.getType() == NUMBER || lookahead.getType() == LPAREN || lookahead.getType() == NOT) {
            expNode = term();
            expNode = simple_part(expNode);
        } else if (lookahead.getType() == PLUS || lookahead.getType() == MINUS) {
            UnaryOperationNode uoNode = sign();
            expNode = term();
            uoNode.setExpression(simple_part(expNode));
            return uoNode;
        } else error("simple_expression");
        return expNode;
    }

    /**
     * A simple_part contains the following:
     * <p>
     * <strong>addop</strong> term simple_part | lambda
     */
    public ExpressionNode simple_part(ExpressionNode posLeft) {

        if (isAddOp(lookahead.getType())) {
            OperationNode op = new OperationNode(lookahead.getType());
            match(lookahead.getType());
            ExpressionNode right = term();
            op.setLeft(posLeft);
            op.setRight(right);
            return simple_part(op);
        }
        // else lambda case
        return posLeft;
    }

    /**
     * A term contains the following:
     * <p>
     * factor term_part
     */
    public ExpressionNode term() {
        ExpressionNode left = factor();
        return term_part(left);
    }

    /**
     * A term_part contains the following:
     * <p>
     * <strong>mulop</strong> factor term_part | lambda
     */
    public ExpressionNode term_part(ExpressionNode posLeft) {
        if (isMulOp(lookahead.getType())) {
            OperationNode op = new OperationNode(lookahead.getType());
            match(lookahead.getType());
            ExpressionNode right = factor();
            op.setLeft(posLeft);
            op.setRight(term_part(right));
            return op;
        }
        // else lambda case
        return posLeft;
    }

    /**
     * A factor contains the following:
     * <p>
     * <strong>id</strong> | <strong>id [</strong> expression <strong>]</strong> | <strong>id (</strong> expression_list <strong>)</strong> |
     * <strong>num</strong> | <strong>(</strong> expression <strong>)</strong> | <strong>not</strong> factor
     */
    public ExpressionNode factor() {
        ExpressionNode ex = null;
        if (lookahead.getType() == ID) {
            String name = lookahead.getLexeme();
            match(ID);
            if (lookahead.getType() == LBRACE) {
                ArrayNode aNode = new ArrayNode(name);
                match(LBRACE);
                ExpressionNode temp = expression();
                aNode.setExpNode(temp);
                match(RBRACE);
                return aNode;
            }
            else if (lookahead.getType() == LPAREN) {
                FunctionNode fNode = new FunctionNode(name);
                match(LPAREN);
                fNode.setExpNode(expression_list());
                match(RPAREN);
                return fNode;
            } else {
                return new VariableNode(name);
            }
        } else if (lookahead.getType() == NUMBER) {
            ex = new ValueNode(lookahead.getLexeme());
            match(NUMBER);
        }
        else if (lookahead.getType() == LPAREN) {
            match(LPAREN);
            ex = expression();
            match(RPAREN);
        } else if (lookahead.getType() == NOT) {
            UnaryOperationNode uoNode = new UnaryOperationNode(NOT);
            match(NOT);
            uoNode.setExpression(factor());
            return uoNode;
        } else error("factor");
        return ex;
    }

    /**
     * A sign contains the following:
     * <p>
     * <strong>+</strong> | <strong>-</strong>
     */
    public UnaryOperationNode sign() {
        UnaryOperationNode uoNode = null;
        if (lookahead.getType() == PLUS) {
            uoNode = new UnaryOperationNode(PLUS);
            match(PLUS);
        } else if (lookahead.getType() == MINUS) {
            uoNode = new UnaryOperationNode(MINUS);
            match(MINUS);
        } else error("sign");
        return uoNode;
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
        //System.out.println("Match " + expected + " " + lookahead.getLexeme());
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
        System.out.println("Error: " + message);
        System.exit(1);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
