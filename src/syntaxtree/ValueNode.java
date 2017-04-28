package syntaxtree;

import scanner.Type;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents a value or number in an expression.
 *
 * @author Bob Laskowski
 */
public class ValueNode extends ExpressionNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private String attribute; // The attribute associated with this node.

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Creates a ValueNode with the given attribute.
     *
     * @param attr The attribute for this value node.
     */
    public ValueNode(String attr) {
        this.attribute = attr;
        if (attribute.contains(".")) type = Type.REAL;
        else type = Type.INTEGER;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Returns the attribute of this node.
     *
     * @return The attribute of this ValueNode.
     */
    public String getAttribute() {
        return (this.attribute);
    }

    /**
     * Returns the attribute as the description of this node.
     *
     * @return The attribute String of this node.
     */
    @Override
    public String toString() {
        return (attribute);
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
        answer += "Value: " + this.attribute + ", Type: " + type + "\n";
        return answer;
    }

    /**
     * Determines if two ValueNodes are equal. They are equal if they have the same attribute
     *
     * @param o Another ValueNode
     * @return True if equal, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        boolean answer = false;
        if (o instanceof ValueNode) {
            ValueNode other = (ValueNode) o;
            if (this.attribute.equals(other.attribute)) answer = true;
        }
        return answer;
    }
}
