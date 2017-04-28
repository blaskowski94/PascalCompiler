package syntaxtree;

import java.util.ArrayList;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents a compound statement in Mini-Pascal. A compound statement is a block of zero or more statements to be run
 * sequentially.
 *
 * @author Bob Laskowski
 */
public class CompoundStatementNode extends StatementNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    // Stores all the statements in the Compound Statement
    private ArrayList<StatementNode> statements = new ArrayList<>();

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * When used, an ArrayList of StatementNodes is built. This adds all the of the statements at once
     *
     * @param nodes ArrayList of StatementNodes inside the CompoundStatement
     */
    public void addAll(ArrayList<StatementNode> nodes) {
        statements.addAll(nodes);
    }

    /**
     * Get the ArrayList of StatementNodes for the CompoundStatement
     *
     * @return ArrayList of StatementNodes
     */
    public ArrayList<StatementNode> getStatements() {
        return statements;
    }

    /**
     * Add a StatementNode to the arrayList of statements
     *
     * @param state The StatementNode to be added
     */
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
