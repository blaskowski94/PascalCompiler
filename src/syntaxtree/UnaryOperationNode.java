package syntaxtree;

import scanner.Type;

/**
 * Created by Bob on 3/13/2017.
 */
public class UnaryOperationNode extends ExpressionNode {

    /**
     * The right operator of this operation.
     */
    private ExpressionNode expression;

    /**
     * The kind of operation.
     */
    private Type operation;

    /**
     * Creates an operation node given an operation token.
     *
     * @param op The token representing this node's math operation.
     */
    public UnaryOperationNode(Type op) {
        this.operation = op;
    }


    // Getters
    public ExpressionNode getExpression() {
        return (this.expression);
    }

    // Setters
    public void setExpression(ExpressionNode node) {
        // If we already have a left, remove it from our child list.
        this.expression = node;
    }

    public Type getOperation() {
        return (this.operation);
    }

    public void setOperation(Type op) {
        this.operation = op;
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

    @Override
    public String indentedToString(int level) {
        String answer = this.indentation(level);
        answer += "Unary Operation: " + this.operation + "\n";
        answer += expression.indentedToString(level + 1);
        return (answer);
    }

    @Override
    public boolean equals(Object o) {
        boolean answer = false;
        if (o instanceof syntaxtree.OperationNode) {
            syntaxtree.UnaryOperationNode other = (syntaxtree.UnaryOperationNode) o;
            if ((this.operation == other.operation) && (this.expression.equals(other.expression))) answer = true;
        }
        return answer;
    }
}