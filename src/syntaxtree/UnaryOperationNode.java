package syntaxtree;

import scanner.Type;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * A special kind of ExpressionNode for the "not" operator, PLUS (+) and MINUS (-)
 *
 * @author Bob Laskowski
 */
public class UnaryOperationNode extends ExpressionNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private ExpressionNode expression; // The right operator of this operation.
    private Type operation; // The kind of operation.

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Creates an operation node given an operation token.
     *
     * @param op The token representing this node's math operation.
     */
    public UnaryOperationNode(Type op) {
        this.operation = op;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Get the Expression associated with the Unary Operation
     *
     * @return An ExpressionNode after the UnaryOperation
     */
    public ExpressionNode getExpression() {
        return (this.expression);
    }

    /**
     * Set the Expression associated with the Unary Operation
     *
     * @param node An ExpressionNode after the UnaryOperation
     */
    public void setExpression(ExpressionNode node) {
        this.expression = node;
    }

    /**
     * Get the unary operation for the node
     *
     * @return The Type of unary operation such as plus or minus
     */
    public Type getOperation() {
        return operation;
    }

    /**
     * Returns the operation token as a String.
     *
     * @return The String version of the operation token.
     */
    @Override
    public String toString() {
        return operation.toString();
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
        answer += "Unary Operation: " + this.operation + ", Type: " + type + "\n";
        answer += expression.indentedToString(level + 1);
        return (answer);
    }

    /**
     * Determines if two UnaryOperationNodes are equal. They are equal if they have the same unary operation and
     * expression
     *
     * @param o Another UnaryOperationNode
     * @return True if equal, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        boolean answer = false;
        if (o instanceof UnaryOperationNode) {
            UnaryOperationNode other = (UnaryOperationNode) o;
            if ((this.operation == other.operation) && (this.expression.equals(other.expression))) answer = true;
        }
        return answer;
    }
}