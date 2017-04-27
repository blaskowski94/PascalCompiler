package syntaxtree;

/**
 * Created by Bob on 4/15/2017.
 */
public class WriteNode extends StatementNode {

    ExpressionNode data;


    public WriteNode(ExpressionNode data) {
        this.data = data;
    }

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
