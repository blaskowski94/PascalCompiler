package syntaxtree;


/**
 * Created by Bob on 4/15/2017.
 */
public class ReadNode extends StatementNode {

    private VariableNode id;

    public ReadNode(VariableNode id) {
        this.id = id;
    }

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
