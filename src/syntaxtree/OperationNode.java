package syntaxtree;

import scanner.Type;

/**
 * Represents any operation in an expression.
 *
 * @author Bob Laskowski
 */
public class OperationNode extends ExpressionNode {

    private ExpressionNode left; // The left operator of this operation.
    private ExpressionNode right; // The right operator of this operation.

    /**
     * Creates an operation node given an operation token.
     *
     * @param op The token representing this node's math operation.
     */
    public OperationNode(Type op) {
        this.type = op;
    }

    /**
     * Set the left hand side of the operation
     *
     * @param node The ExpressionNode on the left
     */
    public void setLeft(ExpressionNode node) {
        // If we already have a left, remove it from our child list.
        this.left = node;
    }

    /**
     * Set the right hand side of the operation
     *
     * @param node The ExpressionNode on the right
     */
    public void setRight(ExpressionNode node) {
        // If we already have a right, remove it from our child list.
        this.right = node;
    }

    /**
     * Returns the operation token as a String.
     *
     * @return The String version of the operation token.
     */
    @Override
    public String toString() {
        return type.toString();
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
        answer += "Operation: " + this.type + "\n";
        answer += left.indentedToString(level + 1);
        answer += right.indentedToString(level + 1);
        return (answer);
    }

    /**
     * Determines if two OperationNodes are equal. They are equal if they have the same operation type and the same
     * expressions on the left and right
     *
     * @param o Another OperationNode
     * @return True if equal, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        boolean answer = false;
        if (o instanceof OperationNode) {
            OperationNode other = (OperationNode) o;
            if ((this.type == other.type) && this.left.equals(other.left) && this.right.equals(other.right)) answer = true;
        }
        return answer;
    }
}
