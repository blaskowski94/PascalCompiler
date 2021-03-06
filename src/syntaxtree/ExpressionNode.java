package syntaxtree;

import scanner.Type;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * General representation of any expression.
 *
 * @author Bob Laskowski
 */
public abstract class ExpressionNode extends SyntaxTreeNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    // The data type of this expression, either REAL or INTEGER *package-private*
    Type type;

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Create a default ExpressionNode with a null type
     */
    public ExpressionNode() {
        type = null;
    }

    /**
     * Create an ExpressionNode with a Type
     *
     * @param t Type INTEGER or REAL
     */
    public ExpressionNode(Type t) {
        this.type = t;
    }

    /**
     * Get the Type of this expression
     *
     * @return Type INTEGER or REAL
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the Type of this expression
     *
     * @param t Type INTEGER or REAL
     */
    public void setType(Type t) {
        this.type = t;
    }

}
