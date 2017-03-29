package syntaxtree;

import scanner.Type;

/**
 * General representation of any expression.
 *
 * @author erik
 */
public abstract class ExpressionNode extends SyntaxTreeNode {

    protected Type type;

    public ExpressionNode() {
        type = null;
    }

    public ExpressionNode(Type t) {
        this.type = t;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type t) {
        this.type = t;
    }

}
