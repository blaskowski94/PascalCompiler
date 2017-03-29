package syntaxtree;

/**
 * Represents a single assignment statement.
 *
 * @author Erik Steinmetz
 */
public class AssignmentStatementNode extends StatementNode {

    private VariableNode lvalue;
    private ExpressionNode expression;

    public VariableNode getLvalue() {
        return lvalue;
    }

    public void setLvalue(VariableNode lvalue) {
        this.lvalue = lvalue;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }


    @Override
    public String indentedToString(int level) {
        String answer = this.indentation(level);
        answer += "Assignment:\n";
        if (lvalue != null) answer += this.lvalue.indentedToString(level + 1);
        if (expression != null) answer += this.expression.indentedToString(level + 1);
        return answer;
    }
}
