package syntaxtree;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * A node for the built in read function of pascal. Takes in input from the console.
 *
 * @author Bob Laskowski
 */
public class ReadNode extends StatementNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private VariableNode id;

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Creates a ReadNode with a VariableNode id
     *
     * @param id The VariableNode to store the input in
     */
    public ReadNode(VariableNode id) {
        this.id = id;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Get the id of the ReadNode
     *
     * @return A VariableNode the input is stored in
     */
    public VariableNode getId() {
        return id;
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
        answer += "Read\n";
        answer += id.indentedToString(level + 1);
        return answer;
    }
}
