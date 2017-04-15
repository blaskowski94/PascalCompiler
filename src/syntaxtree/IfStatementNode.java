package syntaxtree;

/**
 * Represents an if statement in Mini-Pascal. An if statement includes a boolean expression and two statements.
 *
 * @author Bob Laskowski
 */
public class IfStatementNode extends StatementNode {
    // The test of the if statement
    private ExpressionNode test;
    // What to do if the test is true
    private StatementNode thenStatement;
    // What to do if the test is false
    private StatementNode elseStatement;

    /**
     * Get the if statement test
     *
     * @return An ExpressionNode which will evaluate to T/F
     */
    public ExpressionNode getTest() {
        return test;
    }

    /**
     * Set the test for the if statement
     *
     * @param test An ExpressionNode which will evaluate to T/F
     */
    public void setTest(ExpressionNode test) {
        this.test = test;
    }

    public StatementNode getThen() {
        return thenStatement;
    }

    public StatementNode getElse() {
        return elseStatement;
    }

    /**
     * Set the statement to be executed if the test is true
     *
     * @param thenStatement A StatementNode to be executed
     */
    public void setThenStatement(StatementNode thenStatement) {
        this.thenStatement = thenStatement;
    }

    /**
     * Set the statement to be executed if the test is false
     *
     * @param elseStatement A StatementNode to be executed
     */
    public void setElseStatement(StatementNode elseStatement) {
        this.elseStatement = elseStatement;
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
        answer += "If:\n";
        answer += this.test.indentedToString(level + 1);
        answer += this.indentation(level) + "Then:\n" + this.thenStatement.indentedToString(level + 1);
        answer += this.indentation(level) + "Else:\n" + this.elseStatement.indentedToString(level + 1);
        return answer;
    }

}
