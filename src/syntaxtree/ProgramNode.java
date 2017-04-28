package syntaxtree;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents an entire Mini-Pascal Program.
 *
 * @author Bob Laskowski
 */
public class ProgramNode extends SyntaxTreeNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    String name; // Name of the program *package-private*
    DeclarationsNode variables; // Declarations made in the program *package-private*
    SubProgramDeclarationsNode functions; // Functions/procedures declared in the program *package-private*
    CompoundStatementNode main; // Main body of the program *package-private*

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Creates a new program node with all instance variables initialized to null
     */
    public ProgramNode() {
        name = null;
        variables = null;
        functions = null;
        main = null;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Create a new ProgramNode with a name
     *
     * @param aName Name of the program
     */
    public ProgramNode(String aName) {
        this.name = aName;
    }

    /**
     * Get the variables declared in the program
     *
     * @return A DeclarationsNode of variables declared
     */
    public DeclarationsNode getVariables() {
        return variables;
    }

    /**
     * Set the variables declared in the program
     *
     * @param variables A DeclarationsNode of the variables declared
     */
    public void setVariables(DeclarationsNode variables) {
        this.variables = variables;
    }

    /**
     * Get the functions of the program in the form of a SubProgramDeclarationsNode
     *
     * @return The SubProgramDeclarationsNode holding the functions inside the program
     */
    public SubProgramDeclarationsNode getFunctions() {
        return functions;
    }

    /**
     * Set the functions declared in the program
     *
     * @param functions A SubProgramDeclarationsNode containing all the functions/procedures declared
     */
    public void setFunctions(SubProgramDeclarationsNode functions) {
        this.functions = functions;
    }

    /**
     * Get the main function of the Program
     *
     * @return A CompoundStatementNode of the main function
     */
    public CompoundStatementNode getMain() {
        return main;
    }

    /**
     * Set the main body of the program
     *
     * @param main A CompoundStatementNode containing the main body of the program
     */
    public void setMain(CompoundStatementNode main) {
        this.main = main;
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
        answer += "Program: " + name + "\n";
        if (variables != null) answer += variables.indentedToString(level + 1);
        else {
            answer += "\n";
        }
        if (functions != null) answer += functions.indentedToString(level + 1);
        else {
            answer += "\n";
        }
        if (main != null) answer += main.indentedToString(level + 1);
        else {
            answer += "\n";
        }
        return answer;
    }
}
