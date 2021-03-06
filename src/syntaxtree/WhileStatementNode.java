package syntaxtree;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents a while statement in Mini-Pascal. A while statement includes a boolean expression and a body
 *
 * @author Bob Laskowski
 */
public class WhileStatementNode extends StatementNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private ExpressionNode test; // The test to be checked on each iteration of the while loop
    private StatementNode doStatement; // The statement to be executed on each iteration

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Get the test to be checked on each iteration of the while loop
     *
     * @return An ExpressionNode which will evaluate to T/F
     */
    public ExpressionNode getTest() {
        return test;
    }

    /**
     * Set the test to be checked on each iteration of the while loop
     *
     * @param test An ExpressionNode which must evaluate to T/F
     */
    public void setTest(ExpressionNode test) {
        this.test = test;
    }

    /**
     * Set the statement to be executed on each iteration of the while loop
     *
     * @param doStatement A StatementNode to be executed
     */
    public void setDoStatement(StatementNode doStatement) {
        this.doStatement = doStatement;
    }

    /**
     * Get the do statement of the while loop
     *
     * @return A StatementNode to do as long as the while test is true
     */
    public StatementNode getDo() {
        return doStatement;
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
        answer += "While:\n";
        answer += this.test.indentedToString(level + 1);
        answer += this.indentation(level) + "Do:\n" + this.doStatement.indentedToString(level + 1);
        return answer;
    }
}
