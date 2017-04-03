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
    private VariableNode variable = null;
    // An ArrayList of the Argument Expressions
    private ArrayList<ExpressionNode> expNode = new ArrayList();

    /**
     * Add an ArrayList of ExpressionNodes for the function arguments
     *
     * @param input ArrayList of ExpressionNodes
     */
    public void addAllExpNode(ArrayList<ExpressionNode> input) {
        expNode.addAll(input);
    }

    /**
     * Get the function VariableNode
     *
     * @return VariableNode of function
     */
    public VariableNode getVariable() {
        return this.variable;
    }

    /**
     * Set the function VariableNode
     *
     * @param input A function VariableNode
     */
    public void setVariable(VariableNode input) {
        this.variable = input;
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
        answer += this.variable + "\n";
        for (ExpressionNode exp : expNode) {
            answer += exp.indentedToString(level + 1);
        }
        return answer;
    }
}