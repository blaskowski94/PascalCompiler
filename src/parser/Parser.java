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
 * April 27th, 2017
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
     * Creates a new Parser object to parse a String input.
     *
     * @param input A String to parse
     */
    public Parser(String input) {
        scanny = new MyScanner(new StringReader(input));

        try {
            lookahead = scanny.nextToken();
        } catch (IOException ex) {
            error("Scan error");
        }
        symbolTable = new SymbolTable();
    }

    /**
     * Creates a new Parser object to parse a File input
     *
     * @param input A File to parse
     */
    public Parser(File input) {
        InputStreamReader isr;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(input);
        } catch (FileNotFoundException ex) {
            error("File \"" + input + "\" not found. Check file name and path to ensure it exists.");
        }
        assert fis != null;
        isr = new InputStreamReader(fis);
        scanny = new MyScanner(isr);

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
     * Get the symbol table from the parse
     *
     * @return The symbol table populated by the parser
     */
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    /**
     * A program contains the following:
     * <p>
     * <strong>program id ;</strong>
     * declarations
     * subprogram_declarations
     * compound_statement
     * <strong>.</strong>
     *
     * @return A ProgramNode of the whole program
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
    private ArrayList<String> identifier_list() {
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
     *
     * @return A DeclarationsNode containing all the variables declared
     */
    private DeclarationsNode declarations() {
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
     * @return A standard Type (INTEGER/REAL)
     */
    private Type type(ArrayList<String> idList) {
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
                if (!symbolTable.addArray(anIdList, t, beginidx, endidx))
                    error(anIdList + " already exists in symbol table");
            }
        } else if (lookahead.getType() == INTEGER || lookahead.getType() == REAL) {
            t = standard_type();
            for (String anIdList : idList) {
                if (!symbolTable.addVariable(anIdList, t)) error(anIdList + " already exists in symbol table");
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
    private Type standard_type() {
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
     *
     * @return A SubProgramDeclarationsNode containing all the functions/procedures declared
     */
    private SubProgramDeclarationsNode subprogram_declarations() {
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
     *
     * @return A SubProgramNode for a function/procedure declared
     */
    private SubProgramNode subprogram_declaration() {
        SubProgramNode spNode = subprogram_head();
        spNode.setReturnType(symbolTable.getType(spNode.getName()));
        spNode.setVariables(declarations());
        spNode.setFunctions(subprogram_declarations());
        spNode.setMain(compound_statement());
        symbolTable.setLocalTable(spNode.getName(), symbolTable.removeScope());
        return spNode;
    }

    /**
     * A subprogram_head contains the following:
     * <p>
     * <strong>function id</strong> arguments <strong>:</strong> standard_type <strong>;</strong> |
     * <strong>procedure id</strong> arguments <strong>;</strong>
     *
     * @return A SubProgramNode for a function/procedure declared
     */
    private SubProgramNode subprogram_head() {
        SubProgramNode spNode = null;
        if (lookahead.getType() == FUNCTION) {
            match(FUNCTION);
            String funcName = lookahead.getLexeme();
            if (!symbolTable.addFunction(funcName, null)) error(funcName + " already exists in symbol table");
            spNode = new SubProgramNode(funcName);
            match(ID);
            symbolTable.addNewScope();
            ArrayList<VariableNode> args = arguments();

            ArrayList<Type> argTypes = new ArrayList<>();
            for (VariableNode var : args) {
                Type t = var.getType();
                symbolTable.get(funcName).addArg(t);
                argTypes.add(t);
            }
            symbolTable.get(funcName).setArgs(argTypes);
            match(COLON);
            Type t = standard_type();
            args.add(new VariableNode(funcName, t));
            spNode.setArgs(args);
            symbolTable.setType(funcName, t);
            symbolTable.addVariable(funcName, t);
            match(SEMI);
        } else if (lookahead.getType() == PROCEDURE) {
            match(PROCEDURE);
            String procName = lookahead.getLexeme();
            spNode = new SubProgramNode(procName);
            if (!symbolTable.addProcedure(procName)) error(procName + " already exists in symbol table");
            match(ID);
            symbolTable.addNewScope();
            ArrayList<VariableNode> args = arguments();
            ArrayList<Type> argTypes = new ArrayList<>();
            for (VariableNode var : args) {
                Type t = var.getType();
                symbolTable.get(procName).addArg(t);
                argTypes.add(t);
            }
            symbolTable.get(procName).setArgs(argTypes);
            spNode.setArgs(args);
            match(SEMI);
        } else error("subprogram_head");
        return spNode;
    }

    /**
     * Arguments contain the following:
     * <p>
     * <strong>(</strong> parameter_list <strong>)</strong> | lambda
     */
    private ArrayList<VariableNode> arguments() {
        ArrayList<VariableNode> args = new ArrayList<>();
        if (lookahead.getType() == LPAREN) {
            match(LPAREN);
            args = parameter_list();
            match(RPAREN);
        }
        // else lambda case

        return args;
    }

    /**
     * A parameter_list contains the following:
     * <p>
     * identifier_list <strong>:</strong> type |
     * identifier_list <strong>:</strong> type <strong>;</strong> parameter_list
     */
    private ArrayList<VariableNode> parameter_list() {
        ArrayList<String> idList = identifier_list();
        ArrayList<VariableNode> args = new ArrayList<>();
        match(COLON);
        Type t = type(idList);
        for (String id : idList) {
            args.add(new VariableNode(id, t));
        }
        if (lookahead.getType() == SEMI) {
            match(SEMI);
            args.addAll(parameter_list());
        }
        return args;
    }

    /**
     * A compound_statement contains the following:
     * <p>
     * <strong>begin</strong> optional_statements <strong>end</strong>
     *
     * @return A CompoundStatementNode for the body of the function/procedure
     */
    private CompoundStatementNode compound_statement() {
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
     *
     * @return A CompoundStatementNode for the body of the function/procedure
     */
    private CompoundStatementNode optional_statements() {
        CompoundStatementNode comp = new CompoundStatementNode();
        if (lookahead.getType() == ID || lookahead.getType() == BEGIN || lookahead.getType() == IF || lookahead.getType() == WHILE || lookahead.getType() == READ || lookahead.getType() == WRITE)
            comp.addAll(statement_list());
        // else lambda case
        return comp;
    }

    /**
     * A statement_list contains the following:
     * <p>
     * statement_list | statement <strong>;</strong> statement_list
     *
     * @return An ArrayList of StatementNodes
     */
    private ArrayList<StatementNode> statement_list() {
        ArrayList<StatementNode> nodes = new ArrayList<>();
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
     * Note: for the time being we have not included read(id) or write(id) in our parser. It will not recognize these in
     * Pascal code and will throw an error. This has been done until we have a way to recognize built-in functions.
     *
     * @return A StatementNode for a single statement
     */
    private StatementNode statement() {
        StatementNode state = null;
        if (lookahead.getType() == ID) {
            if (!symbolTable.doesExist(lookahead.getLexeme())) {
                error(lookahead.getLexeme() + " has not been declared");
                System.exit(1);
            }
            if (symbolTable.isVariableName(lookahead.getLexeme()) || symbolTable.isArrayName((lookahead.getLexeme()))) {
                AssignmentStatementNode assign = new AssignmentStatementNode();
                VariableNode varNode = variable();
                assign.setLvalue(varNode);
                match(ASSIGN);
                ExpressionNode expNode = expression();
                assign.setExpression(expNode);
                if (varNode.getType() != expNode.getType()) error("type mismatch at " + varNode.getName());
                return assign;
            } else if (symbolTable.isProcedureName(lookahead.getLexeme())) {
                return procedure_statement();
            } else error(lookahead.getLexeme() + " not found in symbol table.");
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
        } else if (lookahead.getType() == READ) {
            match(Type.READ);
            match(Type.LPAREN);
            String varName = lookahead.getLexeme();
            if (!symbolTable.isVariableName(varName)) error(varName + " has not been declared");
            match(Type.ID);
            match(Type.RPAREN);
            return new ReadNode(new VariableNode(varName));
        } else if (lookahead.getType() == WRITE) {
            match(Type.WRITE);
            match(Type.LPAREN);
            WriteNode write = new WriteNode(expression());
            match(Type.RPAREN);
            return write;
        } else {
            error("statement" + lookahead.getLexeme());
        }
        return state;
    }

    /**
     * A variable includes the following:
     * <p>
     * <strong>id</strong> |
     * <strong>id [</strong> expression <strong>]</strong>
     *
     * @return A VariableNode holding a variable
     */
    private VariableNode variable() {
        String varName = lookahead.getLexeme();
        if (!symbolTable.doesExist(varName)) error(varName + " has not been declared");
        if (!symbolTable.isArrayName(varName)) {
            VariableNode var = new VariableNode(varName);
            var.setType(symbolTable.getType(varName));
            match(ID);
            return var;
        } else {
            ArrayNode var = new ArrayNode(varName);
            var.setType(symbolTable.getType(varName));
            match(ID);
            if (lookahead.getType() == LBRACE) {
                match(LBRACE);
                var.setExpNode(expression());
                match(RBRACE);
            }
            return var;
        }
    }

    /**
     * A procedure_statement contains the following:
     * <p>
     * <strong>id</strong> |
     * <strong>id (</strong> expression_list <strong>)</strong>
     * <p>
     * Note: this has not yet been implemented due to ambiguity in the grammar (no way to choose between variable and
     * procedure_statement at the moment).
     *
     * @return A ProcedureStatementNode for a procedure call
     */
    private ProcedureStatementNode procedure_statement() {
        ProcedureStatementNode psNode = new ProcedureStatementNode(lookahead.getLexeme());
        match(ID);
        if (lookahead.getType() == LPAREN) {
            ArrayList<Type> argTypes = symbolTable.get(psNode.getName()).getArgs();
            match(LPAREN);
            ArrayList<ExpressionNode> expList = expression_list();
            for (int i = 0; i < argTypes.size(); i++) {
                Type expectedArg = argTypes.get(i);
                Type actualArg = expList.get(i).getType();
                if (!expectedArg.equals(actualArg)) {
                    error("Type mismatch in arguments for " + psNode.getName());
                }
            }
            psNode.addAllExpNode(expList);
            match(RPAREN);
        }
        return psNode;
    }

    /**
     * An expression_list contains the following:
     * <p>
     * expression | expression <strong>,</strong> expression_list
     *
     * @return An ArrayList of Expression Nodes
     */
    private ArrayList<ExpressionNode> expression_list() {
        ArrayList<ExpressionNode> exNodeList = new ArrayList<>();
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
     *
     * @return A single ExpressionNode
     */
    private ExpressionNode expression() {
        ExpressionNode left = simple_expression();
        Type leftType = left.getType();
        if (isRelOp(lookahead.getType())) {
            OperationNode opNode = new OperationNode(lookahead.getType());
            if (leftType.equals(Type.REAL)) opNode.setType(Type.REAL);
            else opNode.setType(Type.INTEGER);
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
     *
     * @return A single ExpressionNode
     */
    private ExpressionNode simple_expression() {
        ExpressionNode expNode = null;
        if (lookahead.getType() == ID || lookahead.getType() == NUMBER || lookahead.getType() == LPAREN || lookahead.getType() == NOT) {
            expNode = term();
            expNode = simple_part(expNode);
        } else if (lookahead.getType() == PLUS || lookahead.getType() == MINUS) {
            UnaryOperationNode uoNode = sign();
            expNode = term();
            uoNode.setType(expNode.getType());
            uoNode.setExpression(simple_part(expNode));
            return uoNode;
        } else error("simple_expression");
        return expNode;
    }

    /**
     * A simple_part contains the following:
     * <p>
     * <strong>addop</strong> term simple_part | lambda
     *
     * @return A single ExpressionNode
     */
    private ExpressionNode simple_part(ExpressionNode posLeft) {

        if (isAddOp(lookahead.getType())) {
            OperationNode op = new OperationNode(lookahead.getType());
            match(lookahead.getType());
            ExpressionNode right = term();
            op.setLeft(posLeft);
            op.setRight(simple_part(right));
            return op;
        }
        // else lambda case
        return posLeft;
    }

    /**
     * A term contains the following:
     * <p>
     * factor term_part
     *
     * @return A single ExpressionNode
     */
    private ExpressionNode term() {
        ExpressionNode left = factor();
        return term_part(left);
    }

    /**
     * A term_part contains the following:
     * <p>
     * <strong>mulop</strong> factor term_part | lambda
     *
     * @return A single ExpressionNode
     */
    private ExpressionNode term_part(ExpressionNode posLeft) {
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
     *
     * @return A single ExpressionNode
     */
    private ExpressionNode factor() {
        ExpressionNode ex = null;
        if (lookahead.getType() == ID) {
            String name = lookahead.getLexeme();
            if (!symbolTable.doesExist(name)) {
                error(name + " has not been declared");
                System.exit(1);
            }
            match(ID);
            Type t = symbolTable.getType(name);
            if (lookahead.getType() == LBRACE) {
                ArrayNode aNode = new ArrayNode(name);
                aNode.setType(t);
                match(LBRACE);
                ExpressionNode temp = expression();
                aNode.setExpNode(temp);
                match(RBRACE);
                return aNode;
            } else if (lookahead.getType() == LPAREN) {
                // check arg types here
                FunctionNode fNode = new FunctionNode(name);
                ArrayList<Type> argTypes = symbolTable.get(name).getArgs();
                fNode.setType(t);
                match(LPAREN);
                ArrayList<ExpressionNode> actualArgs = expression_list();
                for (int i = 0; i < argTypes.size(); i++) {
                    Type expected = argTypes.get(i);
                    Type actual = actualArgs.get(i).getType();
                    if (!expected.equals(actual)) {
                        error("Type mismatch for arguments of " + name);
                    }
                }
                fNode.setArgs(actualArgs);
                match(RPAREN);
                return fNode;
            } else {
                VariableNode vNode = new VariableNode(name);
                vNode.setType(t);
                return vNode;
            }
        } else if (lookahead.getType() == NUMBER) {
            Type t;
            String num = lookahead.getLexeme();
            if (num.contains(".")) t = Type.REAL;
            else t = Type.INTEGER;
            ValueNode valNode = new ValueNode(num);
            valNode.setType(t);
            match(NUMBER);
            return valNode;
        } else if (lookahead.getType() == LPAREN) {
            match(LPAREN);
            ex = expression();
            match(RPAREN);
        } else if (lookahead.getType() == NOT) {
            UnaryOperationNode uoNode = new UnaryOperationNode(NOT);
            match(NOT);
            ex = factor();
            uoNode.setExpression(ex);
            uoNode.setType(ex.getType());
            return uoNode;
        } else error("factor");
        return ex;
    }

    /**
     * A sign contains the following:
     * <p>
     * <strong>+</strong> | <strong>-</strong>
     *
     * @return A UnaryOperationNode holding not, + or - and the expression
     */
    private UnaryOperationNode sign() {
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
        if (this.lookahead.getType() == expected) {
            try {
                this.lookahead = scanny.nextToken();
                if (this.lookahead == null) {
                    this.lookahead = new Token("End of File", null, 0);
                }
            } catch (IOException ex) {
                error("Scanner exception");
            }
        } else {
            error("Match of " + expected + " found " + this.lookahead.getType() + " instead.");
        }
    }

    /**
     * Errors out of the parser. Prints an error message to standard error.
     *
     * @param message The error message to print.
     */
    private void error(String message) {
        System.err.println("Error: " + message + " Line: " + lookahead.getLineNumber());
    }

}
