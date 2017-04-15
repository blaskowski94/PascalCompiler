package syntaxtree;

import scanner.Type;

/**
 * Node to store a call to an Array variable. Allows an expression inside the brackets.
 *
 * Created by Bob on 3/14/2017.
 * @author Bob Laskowski
 */
public class ArrayNode extends VariableNode {

    private ExpressionNode expNode;

    /**
     * Creates an ArrayNode and the parent variableNode with the given attribute.
     *
     * @param attr The attribute for this value node.
     */
    public ArrayNode(String attr) {
        super(attr);
        expNode = null;
    }

    public ArrayNode(String attr, Type t) {
        super(attr);
        expNode = null;
        type = t;
    }

    /**
     * Returns the name of the variable of this node.
     *
     * @return The name of this VariableNode.
     */
    public String getName() {
        return (super.getName());
    }


    /**
     * Return the ExpressionNode associated with the array call
     *
     * @return An ExpressionNode
     */
    public ExpressionNode getExpNode() {
        return this.expNode;
    }


    /**
     * Set the ExpressionNode associated with the array call
     *
     * @param input ExpressionNode in brackets
     */
    public void setExpNode(ExpressionNode input) {
        this.expNode = input;
    }

    /**
     * Returns the name of the variable as the description of this node.
     *
     * @return The attribute String of this node.
     */
    @Override
    public String toString() {
        return ("VariableNode: " + super.getName() + " ExpressionNode: " + expNode);
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
        answer += "Array: " + super.getName() + ", Type: " + type + "\n";
        answer += this.expNode.indentedToString(level + 1);
        return answer;
    }

    /**
     * Determines if two ArrayNodes are equal. They are equal if they have the same name and same expression
     *
     * @param o Another ArrayNode
     * @return True if equal, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        boolean answer = false;
        if (o instanceof VariableNode) {
            VariableNode other = (VariableNode) o;
            if (this.getName().equals(other.getName())) answer = true;
        }
        return answer;
    }
}
