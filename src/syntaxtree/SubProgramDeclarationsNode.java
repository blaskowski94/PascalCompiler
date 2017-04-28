package syntaxtree;

import java.util.ArrayList;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents a collection of subprogram declarations
 *
 * @author Bob Laskowski
 */
public class SubProgramDeclarationsNode extends SyntaxTreeNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    // ArrayList containing all the functions/procedures declared
    private ArrayList<SubProgramNode> procs = new ArrayList<>();

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Add a function/procedure to the SubProgramDeclarationsNode
     *
     * @param aSubProgram A SubProgramNode with a function/procedure declaration
     */
    public void addSubProgramDeclaration(SubProgramNode aSubProgram) {
        procs.add(aSubProgram);
    }

    /**
     * Add an ArrayList of functions/procedures to the SubProgramDeclarationsNode
     *
     * @param aSubProgram An ArrayList of SubProgramNodes with functions/procedures declarations
     */
    public void addall(ArrayList<SubProgramNode> aSubProgram) {
        procs.addAll(aSubProgram);
    }

    /**
     * Get all of the functions/procedures declared in this SubProgramDeclaration
     *
     * @return An ArrayList of SubProgramNodes
     */
    public ArrayList<SubProgramNode> getProcs() {
        return procs;
    }

    /**
     * Print out the node with proper indentation to build a visual syntax tree
     *
     * @param level The level of indentation, counting begins at zero
     * @return A String with the tree representation of the Node
     */
    public String indentedToString(int level) {
        StringBuilder answer = new StringBuilder(this.indentation(level));
        answer.append("SubProgramDeclarations\n");
        for (SubProgramNode subProg : procs) {
            answer.append(subProg.indentedToString(level + 1));
        }
        return answer.toString();
    }

}
