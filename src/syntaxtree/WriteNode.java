package syntaxtree;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents a write node for built in write function. Displays output to console.
 *
 * @author Bob Laskowski
 */
public class WriteNode extends StatementNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private ExpressionNode data;

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Create a new write node with the expression to write
     *
     * @param data An ExpressionNode to be written to the console
     */
    public WriteNode(ExpressionNode data) {
        this.data = data;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Get the data stored in the write node to be written
     *
     * @return The ExpressionNode to be written
     */
    public ExpressionNode getData() {
        return data;
    }

    /**
     * Creates a String representation of this node and its children.
     *
     * @param level The tree level at which this node resides.
     * @return A String representing this node.
     */
    @Override
    public String indentedToString(int level) {
        String answer = this.indentation(level);
        answer += "Write\n";
        answer += this.data.indentedToString(level + 1);
        return answer;
    }
}
