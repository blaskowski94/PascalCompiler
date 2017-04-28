package syntaxtree;

import scanner.Type;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents a variable in the syntax tree.
 *
 * @author Bob Laskowski
 */
public class VariableNode extends ExpressionNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private String name; // The name of the variable associated with this node.

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Creates a VariableNode with the given name
     *
     * @param attr The name for this variable
     */
    public VariableNode(String attr) {
        this.name = attr;
    }

    /**
     * Create a VariableNode with the given name and type
     *
     * @param attr The name of the variable
     * @param t    The type of this variable (INTEGER/REAL)
     */
    public VariableNode(String attr, Type t) {
        super(t);
        this.name = attr;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Returns the name of the variable of this node.
     *
     * @return The name of this VariableNode.
     */
    public String getName() {
        return (this.name);
    }

    /**
     * Returns the name of the variable as the description of this node.
     *
     * @return The attribute String of this node.
     */
    @Override
    public String toString() {
        return "Name: " + name + ", Type: " + type;
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
        answer += "Name: " + this.name;
        if (type != null) answer += ", Type: " + type;
        answer += "\n";
        return answer;
    }

    /**
     * Determines if two VariableNodes are equal. They are equal if they have the same name
     *
     * @param o Another ArrayNode
     * @return True if equal, False otherwise
     */
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
