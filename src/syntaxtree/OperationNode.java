package syntaxtree;

import scanner.Type;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents any operation in an expression.
 *
 * @author Bob Laskowski
 */
public class OperationNode extends ExpressionNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private ExpressionNode left; // The left operator of this operation.
    private ExpressionNode right; // The right operator of this operation.
    private Type operation; // Operation to be performed such as plus or minus

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Creates an operation node given an operation token.
     *
     * @param op The token representing this node's math operation.
     */
    public OperationNode(Type op) {
        this.operation = op;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Get the left side of the OperationNode
     *
     * @return An ExpressionNode on the left side of the operation
     */
    public ExpressionNode getLeft() {
        return this.left;
    }

    /**
     * Set the left hand side of the operation
     *
     * @param node The ExpressionNode on the left
     */
    public void setLeft(ExpressionNode node) {
        this.left = node;
        if (node.type != null) {
            if (type == null) type = node.type;
            else if (type.equals(Type.INTEGER) && node.getType().equals(Type.REAL)) type = Type.REAL;
        }
    }

    /**
     * Get the right side of the OperationNode
     *
     * @return An ExpressionNode on the right side of the OperationNode
     */
    public ExpressionNode getRight() {
        return this.right;
    }

    /**
     * Set the right hand side of the operation
     *
     * @param node The ExpressionNode on the right
     */
    public void setRight(ExpressionNode node) {
        this.right = node;
        if (node.type != null) {
            if (type == null) type = node.type;
            else if (type.equals(Type.INTEGER) && node.getType().equals(Type.REAL)) type = Type.REAL;
        }
    }

    /**
     * Get the type of the operation such as plus, minus, asterisk (multiplication), etc.
     *
     * @return The Type of operation in the OperationNode
     */
    public Type getOperation() {
        return this.operation;
    }

    /**
     * Set the type of operation to be perfromed such as plus or minus
     *
     * @param t The Type of operation in the OperationNode
     */
    public void setOperation(Type t) {
        operation = t;
    }

    /**
     * Returns the operation token as a String.
     *
     * @return The String version of the operation token.
     */
    @Override
    public String toString() {
        String retVal = "Operation: " + operation.toString() + " ";
        if (type != null) retVal += "Type: " + type.toString() + " ";
        if (right != null) retVal += "Right: " + right.toString() + " ";
        if (left != null) retVal += "Left: " + left.toString();
        return retVal;
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
        answer += "Operation: " + this.operation + ", Type: " + this.type + "\n";
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
            if ((this.type == other.type) && this.left.equals(other.left) && this.right.equals(other.right))
                answer = true;
        }
        return answer;
    }
}
