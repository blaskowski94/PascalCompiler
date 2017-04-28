package syntaxtree;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * The base class for all nodes in a syntax tree.
 *
 * @author Bob Laskowski
 */
public abstract class SyntaxTreeNode {

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Creates a String representation of this node and its children.
     *
     * @param level The tree level at which this node resides.
     * @return A String representing this node.
     */
    public abstract String indentedToString(int level);

    /**
     * Creates an indentation String for the indentedToString.
     *
     * @param level The amount of indentation.
     * @return A String displaying the given amount of indentation.
     */
    protected String indentation(int level) {
        StringBuilder answer = new StringBuilder();
        if (level > 0) {
            answer = new StringBuilder("|-- ");
        }
        for (int indent = 1; indent < level; indent++) answer.append("--- ");
        return (answer.toString());
    }

}
