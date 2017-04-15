package syntaxtree;

import java.util.ArrayList;

/**
 * Represents a compound statement in Mini-Pascal. A compound statement is a block of zero or more statements to be run
 * sequentially.
 *
 * @author Bob Laskowski
 */
public class CompoundStatementNode extends StatementNode {

    // Stores all the statements in the Compound Statement
    private ArrayList<StatementNode> statements = new ArrayList<>();

    /**
     * When used, an ArrayList of StatemetNodes is built. This adds all the of the statements at once
     *
     * @param nodes ArrayList of StatementNodes inside the CompoundStatement
     */
    public void addAll(ArrayList<StatementNode> nodes) {
        statements.addAll(nodes);
    }

    public ArrayList<StatementNode> getStatements() {
        return statements;
    }

    public void addStatement(StatementNode state) {
        statements.add(state);
    }

    /**
     * Print out the node with proper indentation to build a visual syntax tree
     *
     * @param level The level of indentation, counting begins at zero
     * @return A String with the tree representation of the Node
     */
    public String indentedToString(int level) {
        StringBuilder answer = new StringBuilder(this.indentation(level));
        answer.append("Compound Statement\n");
        for (StatementNode state : statements) {
            answer.append(state.indentedToString(level + 1));
        }
        return answer.toString();
    }
}
