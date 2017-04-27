package codegeneration;

import scanner.Type;
import symboltable.SymbolTable;
import syntaxtree.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class will create assembly code for a pascal program
 *
 * @author Bob Laskowski
 */
public class CodeGeneration {
    private SymbolTable stc;
    private ProgramNode program;
    private int currentReg;
    private int loopNum;
    private int ifNum;

    public CodeGeneration(ProgramNode pro, SymbolTable symb) {
        program = pro;
        stc = symb;
        currentReg = 0;
        loopNum = 0;
        ifNum = 0;
    }

    /**
     * Starts the code from the root node by writing the outline of the
     * assembly code, and telling the root node to write its answer into $s0.
     *
     * @return A String of the assembly code.
     */
    public String writeCodeForRoot() {
        StringBuilder code = new StringBuilder();
        code.append(".data\n");
        code.append("__newline__: .asciiz \"\\n\"\n");
        code.append("__input__: .asciiz \"input: \"\n");

        for (VariableNode var : program.getVariables().getVars()) {
            if (stc.isArrayName(var.getName())) {
                int aryLength = stc.get(var.getName()).getArrayLength();
                code.append(var.getName()).append(":\t.word\t");
                for (int i = 0; i < aryLength - 1; i++) {
                    code.append("0, ");
                }
                code.append("0\n");
            } else {
                code.append(var.getName()).append(":\t.word\t0\n");
                stc.get(var.getName()).setMemAddress(var.getName());
            }
        }

        code.append("\n.text\n");
        code.append("main:\n");

        // push all s registers, ra
        code.append(pushToStack("$s", 8));

        // write main code
        for (StatementNode state : program.getMain().getStatements()) {
            code.append(writeStatement(state, "$s" + currentReg));
        }

        // pop s registers, ra
        code.append(popFromStack("$s", 8));

        // write function code
        for (SubProgramNode subNode : program.getFunctions().getProcs()) {
            code.append(writeFunction(subNode));
        }


        return (code.toString());
    }

    public String writeFunction(SubProgramNode node) {
        StringBuilder code = new StringBuilder();
        code.append("\n# Function\n");
        code.append(node.getName() + ":\n");
        // get the local table for the function
        HashMap<String, SymbolTable.Symbol> localTable = stc.getLocalTable(node.getName());
        int offset = 0;
        if (localTable != null) offset = localTable.size();
        // add local table to current symbol table stack
        stc.pushLocalTable(localTable);

        code.append(pushToStack("$s", 8));
        // move stack pointer to fp
        code.append("move\t$fp,\t$sp\n");
        code.append("addi\t$sp,\t$sp,\t-" + offset * 4 + "\n");

        code.append(assignMemoryAddress(node));

        for (StatementNode state : node.getMain().getStatements()) {
            String reg = "$s" + currentReg;
            code.append(writeStatement(state, reg));
        }

        // add return type for function
        if (stc.isFunctionName(node.getName())) {
            code.append("lw\t$v0,\t" + stc.get(node.getName()).getMemAddress() + "\n");
        }

        code.append("addi\t$sp,\t$sp,\t" + offset * 4 + "\n");
        code.append("move\t$sp,\t$fp\n");
        code.append(popFromStack("$s", 8));

        stc.removeScope();
        return code.toString();
    }

    public String assignMemoryAddress(SubProgramNode node) {
        StringBuilder code = new StringBuilder();
        ArrayList<VariableNode> vars = node.getVariables().getVars();
        ArrayList<VariableNode> args = node.getArgs();
        int numArgs = args.size();
        int offset = 0;

        for (VariableNode var : vars) {
            String reg = offset + "($sp)";
            stc.get(var.getName()).setMemAddress(reg);
            offset += 4;
        }

        for (int i = 0; i < numArgs; i++) {
            String reg = offset + "($sp)";
            stc.get(args.get(i).getName()).setMemAddress(reg);
            code.append("sw\t$a" + i + "," + reg + "\n");
            offset += 4;
        }
        return code.toString();
    }


    public String writeStatement(StatementNode node, String reg) {
        StringBuilder code = new StringBuilder();
        if (node instanceof AssignmentStatementNode) {
            code.append(writeAssignment((AssignmentStatementNode) node, reg));
        } else if (node instanceof IfStatementNode) {
            code.append(writeIfStatement((IfStatementNode) node, reg));
        } else if (node instanceof ProcedureStatementNode) {

        } else if (node instanceof WhileStatementNode) {
            code.append(writeWhile((WhileStatementNode) node, reg));
        } else if (node instanceof CompoundStatementNode) {
            for (StatementNode state : ((CompoundStatementNode) node).getStatements()) {
                code.append(writeStatement(state, reg));
            }
        } else if (node instanceof WriteNode) {
            code.append(writeWrite((WriteNode) node, reg));
        } else if (node instanceof ReadNode) {
            code.append(read((ReadNode) node));
        } else code.append("ERROR!!!");
        return code.toString();
    }

    public String read(ReadNode node) {
        StringBuilder code = new StringBuilder();
        code.append("\n# Read\n");

        code.append("li $v0, 4");
        code.append("\nla $a0, __input__\n");
        code.append("syscall\n");

        Type retType = stc.get(node.getId().getName()).getType();

        if (retType == Type.INTEGER) {
            code.append("li\t$v0,\t5\n");
            code.append("syscall\n");
            code.append("sw\t$v0,\t").append(node.getId().getName()).append("\n");
        } else if (retType == Type.REAL) {
            // do floating point stuff
        }

        return code.toString();
    }

    public String writeWhile(WhileStatementNode node, String reg) {
        StringBuilder code = new StringBuilder();
        code.append("\n# while loop\n");

        code.append("while").append(loopNum).append(":\n");
        code.append(writeExpression(node.getTest(), reg));
        code.append("endWhile").append(loopNum).append("\n");

        reg = "$s" + ++currentReg;
        code.append(writeStatement(node.getDo(), reg));

        code.append("j while").append(loopNum).append("\n");
        code.append("endWhile").append(loopNum).append(":\n");

        currentReg--;
        return code.toString();
    }

    public String writeIfStatement(IfStatementNode node, String reg) {
        StringBuilder code = new StringBuilder();
        code.append("\n#If statement\n");

        // If code folding has turned the expression to evaluate into a 0 or 1
        if (node.getTest() instanceof ValueNode) {
            code.append(writeExpression(node.getTest(), reg));
            String otherReg = "$s" + ++currentReg;
            code.append("li\t").append(otherReg).append(",\t").append("1\n");
            code.append("bne\t").append(reg).append(",\t").append(otherReg).append(",\t");
            code.append("else").append(ifNum).append("\n");
        } else
            code.append(writeOperation((OperationNode) node.getTest(), reg)).append("else").append(ifNum).append("\n");

        code.append("\n# then\n");
        reg = "$s" + currentReg++;
        code.append(writeStatement(node.getThen(), reg));
        code.append("j endIf").append(ifNum).append("\n");

        code.append("\n# else\n");
        reg = "$s" + currentReg++;
        code.append("else").append(ifNum).append(":\n");
        code.append(writeStatement(node.getElse(), reg));
        code.append("endIf").append(ifNum).append(":\n");

        currentReg -= 2;
        return code.toString();
    }

    public String writeExpression(ExpressionNode node, String reg) {
        StringBuilder code = new StringBuilder();
        code.append("\n#Expression\n");
        if (node instanceof ValueNode) {
            code.append(writeValue((ValueNode) node, reg));
        } else if (node instanceof OperationNode) {
            code.append(writeOperation((OperationNode) node, reg));
        } else if (node instanceof ArrayNode) {

        } else if (node instanceof FunctionNode) {
            code.append("\n# Function call\n");
            int numArgs = ((FunctionNode) node).getArgs().size();
            for (int i = 0; i < numArgs; i++) {
                reg = "$a" + i;
                code.append(writeExpression(((FunctionNode) node).getArgs().get(i), reg));
            }

            code.append("jal\t" + ((FunctionNode) node).getName() + "\n");

            code.append("move\t" + reg + ", $v0\n");

        } else if (node instanceof VariableNode) {
            String var = stc.get(((VariableNode) node).getName()).getMemAddress();
            code.append("lw\t").append(reg).append(",\t").append(var).append("\n");
        } else code.append("ERROR!!!");
        return code.toString();
    }

    public String writeAssignment(AssignmentStatementNode node, String reg) {
        StringBuilder code = new StringBuilder();
        code.append("\n#Assignment\n");
        code.append(writeExpression(node.getExpression(), reg));
        code.append("sw\t").append(reg).append(",\t").append(node.getLValue().getName()).append("\n\n");
        return code.toString();
    }

    public String pushToStack(String reg, int num) {
        StringBuilder code = new StringBuilder();
        code.append("\n#Push to stack\n");
        code.append("addi\t$sp,\t$sp,\t-");
        code.append(num * 4 + 4);
        code.append('\n');

        for (int i = num - 1; i >= 0; i--) {
            code.append("sw\t").append(reg).append(i).append(",\t").append(4 * (i + 1)).append("($sp)\n");
        }

        code.append("sw\t$ra,\t0($sp)\n\n");

        return code.toString();
    }

    public String popFromStack(String reg, int num) {
        StringBuilder code = new StringBuilder();
        code.append("\n#Restore from stack\n");

        for (int i = num - 1; i >= 0; i--) {
            code.append("lw\t").append(reg).append(i).append(",\t").append(4 * (i + 1)).append("($sp)\n");
        }
        code.append("lw\t$ra,\t0($sp)\n\n");
        code.append("addi\t$sp,\t$sp,\t");
        code.append((num * 4) + 4).append("\n");
        code.append("jr\t$ra\n");
        return code.toString();
    }

    public String writeWrite(WriteNode write, String reg) {
        StringBuilder code = new StringBuilder();
        code.append("\n#Syscall\n");
        code.append("addi\t$v0,\t$zero,\t1\n");
        code.append(writeExpression(write.getData(), reg));
        code.append("add\t$a0,\t").append(reg).append(",\t$zero\n");
        code.append("syscall\n");

        // new line
        code.append("li $v0, 4");
        code.append("\nla $a0, __newline__\n");
        code.append("syscall\n");
        return code.toString();
    }

    /**
     * Writes code for an operations node.
     * The code is written by gathering the child nodes' answers into
     * a pair of registers, and then executing the op on those registers,
     * placing the result in the given result register.
     *
     * @param opNode         The operation node to perform.
     * @param resultRegister The register in which to put the result.
     * @return The code which executes this operation.
     */
    public String writeOperation(OperationNode opNode, String resultRegister) {
        StringBuilder code = new StringBuilder();
        ExpressionNode left = opNode.getLeft();
        String leftRegister = "$s" + currentReg++;
        code.append(writeExpression(left, leftRegister));
        ExpressionNode right = opNode.getRight();
        String rightRegister = "$s" + currentReg++;
        code.append(writeExpression(right, rightRegister));
        Type kindOfOp = opNode.getOperation();
        if (kindOfOp == Type.PLUS) {
            // add resultregister, left, right
            code.append("add\t").append(resultRegister).append(",\t").append(leftRegister).append(",\t").append(rightRegister).append("\n");
        } else if (kindOfOp == Type.MINUS) {
            // add resultregister, left, right
            code.append("sub\t").append(resultRegister).append(",\t").append(leftRegister).append(",\t").append(rightRegister).append("\n");
        } else if (kindOfOp == Type.ASTERISK) {
            code.append("mult\t").append(leftRegister).append(",\t").append(rightRegister).append("\n");
            code.append("mflo\t").append(resultRegister).append("\n");
        } else if (kindOfOp == Type.FSLASH) {
            // implement floating point division?
            code.append("div\t").append(leftRegister).append(",\t").append(rightRegister).append("\n");
            code.append("mflo\t").append(resultRegister).append("\n");
        } else if (kindOfOp == Type.DIV) {
            code.append("div\t").append(leftRegister).append(",\t").append(rightRegister).append("\n");
            code.append("mflo\t").append(resultRegister).append("\n");
        } else if (kindOfOp == Type.MOD) {
            code.append("div\t").append(leftRegister).append(",\t").append(rightRegister).append("\n");
            code.append("mfhi\t").append(resultRegister).append("\n");
        } else if (kindOfOp == Type.AND) {
            // Note that this is a bitwise and
            code.append("and\t").append(resultRegister).append(",\t").append(leftRegister).append(",\t").append(rightRegister).append("\n");
        } else if (kindOfOp == Type.OR) {
            // Note that this is bitwise or
            code.append("or\t").append(resultRegister).append(",\t").append(leftRegister).append(",\t").append(rightRegister).append("\n");
        } else if (kindOfOp == Type.LTHAN) {
            // branch >=
            code.append("bge\t").append(leftRegister).append(",\t").append(rightRegister).append(",\t");
        } else if (kindOfOp == Type.GTHAN) {
            // branch <=
            code.append("ble\t").append(leftRegister).append(",\t").append(rightRegister).append(",\t");
        } else if (kindOfOp == Type.LTHANEQ) {
            // branch >
            code.append("bgt\t").append(leftRegister).append(",\t").append(rightRegister).append(",\t");
        } else if (kindOfOp == Type.GTHANEQ) {
            // branch <
            code.append("blt\t").append(leftRegister).append(",\t").append(rightRegister).append(",\t");
        } else if (kindOfOp == Type.EQUAL) {
            // branch !=
            code.append("bne\t").append(leftRegister).append(",\t").append(rightRegister).append(",\t");
        } else if (kindOfOp == Type.NOTEQ) {
            // branch ==
            code.append("beq\t").append(leftRegister).append(",\t").append(rightRegister).append(",\t");
        }
        this.currentReg -= 2;
        return code.toString();
    }

    /**
     * Writes code for a value node.
     * The code is written by executing an add immediate with the value
     * into the destination register.
     * Writes code that looks like  addi $reg, $zero, value
     *
     * @param valNode        The node containing the value.
     * @param resultRegister The register in which to put the value.
     * @return The code which executes this value node.
     */
    public String writeValue(ValueNode valNode, String resultRegister) {
        String value = valNode.getAttribute();
        return "li\t" + resultRegister + ",\t" + value + "\n";
    }
}
