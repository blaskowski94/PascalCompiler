package syntaxtree;

import java.util.ArrayList;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents a set of declarations in a Mini-Pascal program.
 *
 * @author Bob Laskowski
 */
public class DeclarationsNode extends SyntaxTreeNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    // The variables declared in this declaration
    private ArrayList<VariableNode> vars = new ArrayList<>();

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Add one variable declared in this declarations statement
     *
     * @param aVariable A VariableNode being declared
     */
    public void addVariable(VariableNode aVariable) {
        vars.add(aVariable);
    }

    /**
     * Add all the variables declared in a declaration to this declaration
     *
     * @param dec A DeclarationNode
     */
    public void addDeclarations(DeclarationsNode dec) {
        vars.addAll(dec.vars);
    }

    /**
     * Get the ArrayList of VariableNodes declared
     *
     * @return An ArrayList of VariableNodes
     */
    public ArrayList<VariableNode> getVars() {
        return vars;
    }

    /**
     * Print out the node with proper indentation to build a visual syntax tree
     *
     * @param level The level of indentation, counting begins at zero
     * @return A String with the tree representation of the Node
     */
    public String indentedToString(int level) {
        StringBuilder answer = new StringBuilder(this.indentation(level));
        answer.append("Declarations\n");
        for (VariableNode variable : vars) {
            answer.append(variable.indentedToString(level + 1));
        }
        return answer.toString();
    }
}
