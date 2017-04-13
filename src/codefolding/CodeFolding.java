package codefolding;

import scanner.Type;
import syntaxtree.*;

import java.util.ArrayList;

/**
 * Code folding module of the Mini-Pascal Compiler.
 * This class can perform the code folding analysis on a Program tree.
 *
 * @author Bob Laskowski
 */
public class CodeFolding {

    public ProgramNode foldProgram(ProgramNode program) {
        SubProgramDeclarationsNode spdn = program.getFunctions();
        CompoundStatementNode comp = program.getMain();

        SubProgramDeclarationsNode newSpdn = new SubProgramDeclarationsNode();
        for (SubProgramNode spn : spdn.getProcs()) {
            newSpdn.addSubProgramDeclaration((SubProgramNode) foldProgram(spn));
        }

        CompoundStatementNode newComp = new CompoundStatementNode();
        for (StatementNode state : comp.getStatements()) {
            newComp.addStatement(foldStatement(state));
        }

        program.setFunctions(newSpdn);
        program.setMain(newComp);
        return program;
    }

    public StatementNode foldStatement(StatementNode state) {
        switch (state.getClass().getSimpleName()) {
            case "AssignmentStatementNode":
                AssignmentStatementNode asn = (AssignmentStatementNode) state;
                ExpressionNode ex = asn.getExpression();
                VariableNode lvalue = asn.getLValue();
                asn.setExpression(foldExpression(ex));
                asn.setLvalue(foldVariable(lvalue));
                return asn;
            case "CompoundStatementNode":
                CompoundStatementNode csn = (CompoundStatementNode) state;
                CompoundStatementNode returnState = new CompoundStatementNode();
                for (StatementNode sn : csn.getStatements()) {
                    returnState.addStatement(foldStatement(sn));
                }
                return returnState;
            case "IfStatementNode":
                IfStatementNode ifState = (IfStatementNode) state;
                ExpressionNode test = ifState.getTest();
                ifState.setTest(foldExpression(test));
                StatementNode ifThen = ifState.getThen();
                ifState.setThenStatement(foldStatement(ifThen));
                StatementNode ifElse = ifState.getElse();
                ifState.setElseStatement(foldStatement(ifElse));
                return ifState;
            case "WhileStatementNode":
                WhileStatementNode whileState = (WhileStatementNode) state;
                test = whileState.getTest();
                whileState.setTest(foldExpression(test));
                StatementNode doState = whileState.getDo();
                whileState.setDoStatement(foldStatement(doState));
                return whileState;
            case "ProcedureStatementNode":
                ProcedureStatementNode psn = (ProcedureStatementNode) state;
                ArrayList<ExpressionNode> args = psn.removeArgs();
                for (ExpressionNode arg : args) {
                    psn.addArg(foldExpression(arg));
                }
                return psn;
            default:
                break;
        }
        return state;
    }

    public ExpressionNode foldExpression(ExpressionNode ex) {
        if (ex instanceof OperationNode) {
            OperationNode op = (OperationNode) ex;
            ex = codeFolding(op);
        } else if (ex instanceof UnaryOperationNode) {
            UnaryOperationNode uo = (UnaryOperationNode) ex;
            ex = foldUnary(uo);
        }
        return ex;
    }

    public ExpressionNode foldUnary(ExpressionNode uo) {
        ExpressionNode ex = foldExpression(((UnaryOperationNode) uo).getExpression());

        Type operation = ((UnaryOperationNode) uo).getOperation();

        if (ex instanceof ValueNode) {
            if (operation == Type.PLUS) {
                uo = new ValueNode(((ValueNode) ex).getAttribute());
                return uo;
            } else if (operation == Type.MINUS) {
                return new ValueNode("-" + ((ValueNode) ex).getAttribute());
            } else if (operation == Type.NOT) {
                Double val = Double.parseDouble(((ValueNode) ex).getAttribute());
                if (val == 0) {
                    return new ValueNode("1");
                } else {
                    return new ValueNode("0");
                }
            }
        }
        // If not a value
        else {
            if (operation == Type.PLUS) {
                return ex;
            } else if (operation == Type.MINUS) {
                OperationNode op = new OperationNode(operation);
                op.setLeft(new ValueNode("0"));
                op.setRight(ex);
                return op;
            }
        }
        return uo;
    }

    public VariableNode foldVariable(VariableNode var) {
        switch (var.getClass().getSimpleName()) {
            case "ArrayNode":
                ArrayNode ary = (ArrayNode) var;
                ExpressionNode exp = ary.getExpNode();
                if (exp instanceof OperationNode) {
                    OperationNode op = (OperationNode) exp;
                    ary.setExpNode(codeFolding(op));
                }
                break;
            case "FunctionNode":
                ArrayList<ExpressionNode> args = ((FunctionNode) var).removeArgs();
                for (ExpressionNode express : args) {
                    if (express instanceof OperationNode) {
                        OperationNode op = (OperationNode) express;
                        // test this
                        if (codeFolding(op) instanceof OperationNode) {
                            ((FunctionNode) var).addArg(codeFolding(op));
                        }
                    } else ((FunctionNode) var).addArg(express);
                }
                return var;
            default:
                break;
        }
        return var;
    }

    /**
     * Folds code for the given node.
     * We only fold if both children are value nodes, and the node itself
     * is a PLUS node.
     *
     * @param node The node to check for possible efficiency improvements.
     * @return The folded node or the original node if nothing
     */
    public ExpressionNode codeFolding(OperationNode node) {
        if (node.getLeft() instanceof OperationNode) {
            node.setLeft(codeFolding((OperationNode) node.getLeft()));
        }
        if (node.getRight() instanceof OperationNode) {
            node.setRight(codeFolding((OperationNode) node.getRight()));
        }
        if (node.getLeft() instanceof ValueNode && node.getRight() instanceof ValueNode) {
            boolean intOp = false;
            ExpressionNode left = node.getLeft();
            ExpressionNode right = node.getRight();
            if ((left.getType() == Type.INTEGER && right.getType() == Type.INTEGER)) {
                intOp = true;
            }

            double leftVal = Double.parseDouble(((ValueNode) left).getAttribute());
            double rightVal = Double.parseDouble(((ValueNode) right).getAttribute());

            ValueNode value = null;
            switch (node.getOperation()) {
                case PLUS:
                    value = new ValueNode("" + (leftVal + rightVal));
                    break;
                case MINUS:
                    value = new ValueNode("" + (leftVal - rightVal));
                    break;
                case ASTERISK:
                    value = new ValueNode("" + (leftVal * rightVal));
                    break;
                case FSLASH:
                    value = new ValueNode("" + (leftVal / rightVal));
                    break;
                case DIV:
                    value = new ValueNode("" + ((int) leftVal / (int) rightVal));
                    break;
                case MOD:
                    value = new ValueNode("" + (leftVal % rightVal));
                    break;
                case EQUAL:
                    if (leftVal == rightVal) value = new ValueNode("1");
                    else value = new ValueNode("0");
                    break;
                case NOTEQ:
                    if (leftVal != rightVal) value = new ValueNode("1");
                    else value = new ValueNode("0");
                    break;
                case AND:
                    if (leftVal == 0 || rightVal == 0) value = new ValueNode("0");
                    else value = new ValueNode("1");
                    break;
                case OR:
                    if (leftVal == 0 && rightVal == 0) value = new ValueNode("0");
                    else value = new ValueNode("1");
                    break;
                case LTHAN:
                    if (leftVal < rightVal) value = new ValueNode("1");
                    else value = new ValueNode("0");
                    break;
                case GTHAN:
                    if (leftVal > rightVal) value = new ValueNode("1");
                    else value = new ValueNode("0");
                    break;
                case LTHANEQ:
                    if (leftVal <= rightVal) value = new ValueNode("1");
                    else value = new ValueNode("0");
                    break;
                case GTHANEQ:
                    if (leftVal >= rightVal) value = new ValueNode("1");
                    else value = new ValueNode("0");
                    break;
                default:
                    break;
            }

            if (intOp && value != null) value = new ValueNode("" + (int) Double.parseDouble(value.getAttribute()));
            return value;


        } else {
            return node;
        }
    }

}