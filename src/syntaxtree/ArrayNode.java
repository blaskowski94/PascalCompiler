package syntaxtree;

/**
 * Created by Bob on 3/14/2017.
 */
public class ArrayNode extends VariableNode {

    /**
     * The name of the variable associated with this node.
     */
    private ExpressionNode expNode;

    /**
     * Creates an ArrayNode and the parent variableNode with the given attribute.
     *
     * @param attr The attribute for this value node.
     */
    public ArrayNode(String attr) {
        super(attr);
        expNode = null;
    }

    /**
     * Returns the name of the variable of this node.
     *
     * @return The name of this VariableNode.
     */
    public String getName() {
        return (super.name);
    }

    //TODO add javadoc
    public ExpressionNode getExpNode() {
        return this.expNode;
    }

    //TODO add javadoc
    public void setExpNode(ExpressionNode input) {
        this.expNode = input;
    }

    /**
     * Returns the name of the variable as the description of this node.
     *
     * @return The attribute String of this node.
     */
    @Override
    public String toString() {
        return ("VariableNode: " + super.name + " ExpressionNode: " + expNode);
    }

    @Override
    public String indentedToString(int level) {
        String answer = this.indentation(level);
        answer += "Array: " + super.name + "\n";
        answer += this.expNode.indentedToString(level + 1);
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        boolean answer = false;
        if (o instanceof VariableNode) {
            VariableNode other = (VariableNode) o;
            if (this.name.equals(other.name)) answer = true;
        }
        return answer;
    }
}
