package syntaxtree;

/**
 * Represents a single assignment statement.
 *
 * @author Bob Laskowski
 */
public class AssignmentStatementNode extends StatementNode {

    private VariableNode lvalue; // The variable the value is being assigned to
    private ExpressionNode expression; // The value being assigned

    /**
     * Set the variable the value is being assigned to
     *
     * @param lvalue A VariableNode to store the value in
     */
    public void setLvalue(VariableNode lvalue) {
        this.lvalue = lvalue;
    }

    /**
     * Get the expression on the right side of the assigment statement
     *
     * @return The ExpressionNode
     */
    public ExpressionNode getExpression() {
        return expression;
    }

    /**
     * Set the expression on the right hand side
     *
     * @param expression The ExpressionNode on the right hand side
     */
    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
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
        answer += "Assignment:\n";
        if (lvalue != null) answer += this.lvalue.indentedToString(level + 1);
        if (expression != null) answer += this.expression.indentedToString(level + 1);
        return answer;
    }
}
