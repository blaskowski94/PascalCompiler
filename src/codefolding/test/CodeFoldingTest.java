package codefolding.test;

import codefolding.CodeFolding;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scanner.Type;
import syntaxtree.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Bob on 4/11/2017.
 */
class CodeFoldingTest {
    ProgramNode program;
    CodeFolding cf;

    @BeforeEach
    void setUp() {
        program = new ProgramNode("test");
        cf = new CodeFolding();
    }

    @AfterEach
    void tearDown() {
        program = null;
    }

    @Test
    void foldStatement() {
        //ASSIGNMENTNODE
        System.out.println("\n------AssignmentStatementNode------");
        //expression node
        OperationNode op = new OperationNode(Type.PLUS);
        //nested statement
        OperationNode op2 = new OperationNode(Type.ASTERISK);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);
        //assignment statement node
        AssignmentStatementNode assign = new AssignmentStatementNode();
        assign.setLvalue(new VariableNode("Test"));
        assign.setExpression(op);

        int original = assign.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + assign.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldStatement(assign);
        int end = assign.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + assign.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));


        //Assignment2
        System.out.println("\n------AssignmentStatementNode------");
        //expression node
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);
        //assignment statement node
        assign = new AssignmentStatementNode();

        //Testing ArrayCall 5 + 8
        ArrayNode array = new ArrayNode("array", Type.REAL);
        //setting up the operation
        //adding it to the node
        array.setExpNode(op);
        assign.setLvalue(array);
        assign.setExpression(new ValueNode("10.5"));

        original = assign.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + assign.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldStatement(assign);
        end = assign.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + assign.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));

        //Assignment3
        System.out.println("\n------AssignmentStatementNode------");
        //expression node
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);
        //assignment statement node
        assign = new AssignmentStatementNode();

        //Testing ArrayCall 5 + 8
        array = new ArrayNode("array", Type.REAL);
        //setting up the operation
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);

        //adding it to the node
        array.setExpNode(op);
        assign.setLvalue(array);
        assign.setExpression(op);

        original = assign.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + assign.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldStatement(assign);
        end = assign.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + assign.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));


        //IfStatementNode
        System.out.println("\n------IfStatementNode------");
        //ifstatement
        OperationNode ifop = new OperationNode(Type.LTHAN);
        ifop.setLeft(new VariableNode("varNode", Type.INTEGER));
        ifop.setRight(new ValueNode("5"));

        //Then statement
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);
        AssignmentStatementNode thenState = new AssignmentStatementNode();
        thenState.setLvalue(new VariableNode("var2", Type.INTEGER));
        thenState.setExpression(op);

        //else statement
        OperationNode elseop = new OperationNode(Type.PLUS);
        elseop.setLeft(new ValueNode("5"));
        elseop.setRight(new ValueNode("5"));
        AssignmentStatementNode elseState = new AssignmentStatementNode();
        elseState.setLvalue(new VariableNode("array", Type.INTEGER));
        elseState.setExpression(elseop);

        //if statement node
        IfStatementNode ifstate = new IfStatementNode();
        ifstate.setTest(ifop);
        ifstate.setThenStatement(thenState);
        ifstate.setElseStatement(elseState);

        original = ifstate.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + ifstate.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldStatement(ifstate);
        end = ifstate.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + ifstate.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));

        //procedureStatementNode
        System.out.println("\n------ProcedureStatementNode------");
        //statement1
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);

        //statement2
        elseop = new OperationNode(Type.PLUS);
        elseop.setLeft(new ValueNode("5"));
        elseop.setRight(new ValueNode("5"));

        //ProcedureStatementNode
        ProcedureStatementNode psn = new ProcedureStatementNode("test");
        psn.addArg(op);
        psn.addArg(elseop);

        original = psn.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + psn.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldStatement(psn);
        end = psn.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + psn.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));

        //while statement node
        System.out.println("\n------IfStatementNode------");
        //ifstatement
        ifop = new OperationNode(Type.LTHAN);
        ifop.setLeft(new VariableNode("varNode", Type.INTEGER));
        ifop.setRight(new ValueNode("5"));

        //Do statement
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);
        AssignmentStatementNode doState = new AssignmentStatementNode();
        doState.setLvalue(new VariableNode("var2", Type.INTEGER));
        doState.setExpression(op);

        //if statement node
        WhileStatementNode whilestate = new WhileStatementNode();
        whilestate.setTest(ifop);
        whilestate.setDoStatement(doState);

        original = whilestate.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + whilestate.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldStatement(whilestate);
        end = whilestate.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + whilestate.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));

//compoundstatementNode
        //ASSIGNMENTNODE
        System.out.println("\n------CompoundStatementNode------");
        //expression node
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);
        //assignment statement node
        assign = new AssignmentStatementNode();
        assign.setLvalue(new VariableNode("Test"));
        assign.setExpression(op);

        //IfStatementNode
        //ifstatement
        ifop = new OperationNode(Type.LTHAN);
        ifop.setLeft(new VariableNode("varNode", Type.INTEGER));
        ifop.setRight(new VariableNode("5"));

        //Then statement
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);
        thenState = new AssignmentStatementNode();
        thenState.setLvalue(new VariableNode("var2", Type.INTEGER));
        thenState.setExpression(op);

        //else statement
        elseop = new OperationNode(Type.PLUS);
        elseop.setLeft(new ValueNode("5"));
        elseop.setRight(new VariableNode("5"));
        elseState = new AssignmentStatementNode();
        elseState.setLvalue(new VariableNode("array", Type.INTEGER));
        elseState.setExpression(elseop);

        //if statement node
        ifstate = new IfStatementNode();
        ifstate.setTest(ifop);
        ifstate.setThenStatement(thenState);
        ifstate.setElseStatement(elseState);

        //procedureStatementNode
        //statement1
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);

        //statement2
        elseop = new OperationNode(Type.PLUS);
        elseop.setLeft(new ValueNode("5"));
        elseop.setRight(new VariableNode("5"));

        //ProcedureStatementNode
        psn = new ProcedureStatementNode("var");
        psn.addArg(op);
        psn.addArg(elseop);

        //while statement node
        //ifstatement
        ifop = new OperationNode(Type.LTHAN);
        ifop.setLeft(new VariableNode("varNode", Type.INTEGER));
        ifop.setRight(new VariableNode("5"));

        //Do statement
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);
        doState = new AssignmentStatementNode();
        doState.setLvalue(new VariableNode("var2", Type.INTEGER));
        doState.setExpression(op);

        //if statement node
        whilestate = new WhileStatementNode();
        whilestate.setTest(ifop);
        whilestate.setDoStatement(doState);

        //ACTUAL TEST
        CompoundStatementNode csn = new CompoundStatementNode();
        csn.addStatement(assign);
        csn.addStatement(ifstate);
        csn.addStatement(psn);
        csn.addStatement(whilestate);

        original = csn.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + csn.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldStatement(csn);
        end = csn.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + csn.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));


    }

    @Test
    void foldVariable() throws Exception {
        //Testing standard variable
        VariableNode var = new VariableNode("foo", Type.INTEGER);
        VariableNode result = cf.foldVariable(var);
        String expected = "Name: foo, Type: INTEGER";
        assertEquals(result.toString(), expected);


        System.out.println("------ArrayNode[5+8]------");
        //Testing ArrayCall 5 + 8
        ArrayNode array = new ArrayNode("array", Type.REAL);
        //setting up the operation
        OperationNode op = new OperationNode(Type.PLUS);
        op.setLeft(new ValueNode("5"));
        op.setRight(new ValueNode("8"));
        //adding it to the node
        array.setExpNode(op);
        //saving the resulting number of lines
        int original = array.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + array.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldVariable(array);
        int end = array.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + array.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));


        System.out.println("\n------ArrayNode[8+8+8+8]------");

        //Testing ArrayCall 5 + 8
        array = new ArrayNode("array", Type.REAL);
        //setting up the operation
        op = new OperationNode(Type.PLUS);
        //nested statement
        OperationNode op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);

        //adding it to the node
        array.setExpNode(op);
        //saving the resulting number of lines
        original = array.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + array.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldVariable(array);
        end = array.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + array.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));


        System.out.println("\n------FunctionNode(5+8,8)------");
        //Testing ArrayCall 5 + 8
        FunctionNode fctn = new FunctionNode("fctn", Type.REAL);
        //setting up the operation
        op = new OperationNode(Type.PLUS);
        op.setLeft(new ValueNode("5"));
        op.setRight(new ValueNode("8"));
        //adding it to the node
        fctn.addArg(op);
        fctn.addArg(new ValueNode("8"));
        //saving the resulting number of lines
        original = fctn.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + fctn.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldVariable(fctn);
        end = fctn.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + fctn.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));


        System.out.println("\n------FunctionNode(8+8+8+8,8)------");
        //Testing ArrayCall 5 + 8
        fctn = new FunctionNode("fctn", Type.REAL);
        //setting up the operation
        op = new OperationNode(Type.PLUS);
        //nested statement
        op2 = new OperationNode(Type.PLUS);
        op2.setLeft(new ValueNode("8"));
        op2.setRight(new ValueNode("8"));
        op.setLeft(op2);
        op.setRight(op2);
        //adding it to the node
        fctn.addArg(op);
        fctn.addArg(new ValueNode("8"));
        //saving the resulting number of lines
        original = fctn.indentedToString(0).split("\n").length;
        System.out.println("Original:\n\t" + fctn.indentedToString(0).replaceAll("\n", "\n\t"));
        //calling the fold
        cf.foldVariable(fctn);
        end = fctn.indentedToString(0).split("\n").length;
        System.out.println("New:\n\t" + fctn.indentedToString(0).replaceAll("\n", "\n\t"));
        System.out.println("Difference: " + (original - end));

    }

    @Test
    void foldExpression() {
        System.out.println("----- Fold Expression -----");
        System.out.println("*PLUS*");
        OperationNode op = new OperationNode(Type.PLUS);
        op.setLeft(new ValueNode("5"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        ExpressionNode val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*MINUS*");
        op.setOperation(Type.MINUS);
        op.setLeft(new ValueNode("5"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*ASTERISK*");
        op.setOperation(Type.ASTERISK);
        op.setLeft(new ValueNode("5"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*FSLASH INT*");
        op.setOperation(Type.FSLASH);
        op.setLeft(new ValueNode("5"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*FSLASH REAL*");
        op.setOperation(Type.FSLASH);
        op.setLeft(new ValueNode("5.0"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*DIV*");
        op.setOperation(Type.DIV);
        op.setLeft(new ValueNode("5.0"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*MOD*");
        op.setOperation(Type.MOD);
        op.setLeft(new ValueNode("5"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*EQUAL FALSE*");
        op.setOperation(Type.EQUAL);
        op.setLeft(new ValueNode("5"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*EQUAL TRUE*");
        op.setOperation(Type.EQUAL);
        op.setLeft(new ValueNode("10"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*NOT EQUAL TRUE*");
        op.setOperation(Type.NOTEQ);
        op.setLeft(new ValueNode("5"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*NOT EQUAL FALSE*");
        op.setOperation(Type.NOTEQ);
        op.setLeft(new ValueNode("10"));
        op.setRight(new ValueNode("10"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*AND*");
        op.setOperation(Type.AND);
        op.setLeft(new ValueNode("0"));
        op.setRight(new ValueNode("1"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));

        System.out.println("*AND INVALID*");
        op.setOperation(Type.AND);
        op.setLeft(new ValueNode("10"));
        op.setRight(new ValueNode("1"));

        System.out.println(op.indentedToString(0));
        val = cf.foldExpression(op);
        System.out.println(val.indentedToString(0));
    }

    @Test
    void codeFolding() {
    }

}