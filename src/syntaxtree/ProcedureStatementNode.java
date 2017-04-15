package syntaxtree;

import java.util.ArrayList;

/**
 * The node for a call to a procedure. Stores the name and an ArrayList of the arguments.
 *
 * Created by Bob on 3/13/2017.
 * @author Bob Laskowski
 */
public class ProcedureStatementNode extends StatementNode {

    // The function variable
    private String name;
    // An ArrayList of the Argument Expressions
    private ArrayList<ExpressionNode> args = new ArrayList();

    public ProcedureStatementNode(String name) {
        this.name = name;
    }

    /**
     * Add an ArrayList of ExpressionNodes for the function arguments
     *
     * @param input ArrayList of ExpressionNodes
     */
    public void addAllExpNode(ArrayList<ExpressionNode> input) {
        args.addAll(input);
    }

    public void addArg(ExpressionNode exp) {
        args.add(exp);
    }

    /**
     * Get the function VariableNode
     *
     * @return VariableNode of function
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the function VariableNode
     *
     * @param input A function VariableNode
     */
    public void setName(String input) {
        this.name = input;
    }

    public ArrayList<ExpressionNode> removeArgs() {
        ArrayList<ExpressionNode> temp = new ArrayList<>();
        temp.addAll(args);
        args.clear();
        return temp;
    }

    public ArrayList<ExpressionNode> getArgs() {
        return args;
    }

    /**
     * Print out the node with proper indentation to build a visual syntax tree
     *
     * @param level The level of indentation, counting begins at zero
     * @return A String with the tree representation of the Node
     */
    @Override
    public String indentedToString(int level) {
        String answer = this.indentation(level);
        answer += "Procedure: ";
        answer += this.name + "\n";
        for (ExpressionNode exp : args) {
            answer += exp.indentedToString(level + 1);
        }
        return answer;
    }
}