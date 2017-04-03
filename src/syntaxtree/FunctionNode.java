package syntaxtree;

import java.util.ArrayList;

/**
 * The node for a call to a function. Stores the name and an ArrayList of the arguments.
 *
 * Created by Bob on 3/15/2017.
 * @author Bob Laskowski
 */
public class FunctionNode extends VariableNode {

    // The name of the arguments associated with this node
    private ArrayList<ExpressionNode> expNode;

    /**
     * Creates a FunctionNode and the parent variableNode with the given name
     *
     * @param attr The attribute for this value node.
     */
    public FunctionNode(String attr) {
        super(attr);
        expNode = null;
        this.type = null;
    }

    /**
     * Returns the name of the function
     *
     * @return The name of this FunctionNode
     */
    public String getName() {
        return (super.getName());
    }

    /**
     * Return the ArrayList of expression nodes associated with the function arguments
     *
     * @return ArrayList of ExpressionNodes
     */
    public ArrayList<ExpressionNode> getExpNode() {
        return this.expNode;
    }

    /**
     * Sets the ArrayList of ExpressionNodes for the function arguments
     *
     * @param input an ArrayList of ExpressionNodes for function args
     */
    public void setExpNode(ArrayList<ExpressionNode> input) {
        this.expNode = input;
    }

    /**
     * Returns the name of the variable as the description of this node.
     *
     * @return The attribute String of this node.
     */
    @Override
    public String toString() {
        return ("VariableNode: " + super.getName() + "ExpressionNode: " + expNode);
    }

    /**
     * Print out the node with proper indentation to build a visual syntax tree
     *
     * @param level The level of indentation, counting begins at zero
     * @return A String with the tree representation of the Node
     */
    @Override
    public String indentedToString(int level) {
        StringBuilder answer = new StringBuilder(this.indentation(level));
        answer.append("Name: ").append(super.getName()).append("\n");
        answer.append(this.indentation(level));
        answer.append("Arguments: \n");
        for (ExpressionNode expression : expNode) {
            answer.append(expression.indentedToString(level + 1));
        }
        return answer.toString();
    }

    /**
     * Determines if two FunctionNodes are equal. They are equal if they have the same name and same arguments
     *
     * @param o Another FunctionNode
     * @return True if equal, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        boolean answer = false;
        if (o instanceof FunctionNode) {
            FunctionNode other = (FunctionNode) o;
            if (super.getName().equals(other.getName()) && (this.expNode.equals(other.getExpNode()))) answer = true;
        }
        return answer;
    }
}