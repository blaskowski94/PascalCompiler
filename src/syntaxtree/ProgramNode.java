package syntaxtree;

/**
 * Represents an entire Mini-Pascal Program
 *
 * @author Bob Laskowski
 */
public class ProgramNode extends SyntaxTreeNode {

    private String name; // Name of the program
    private DeclarationsNode variables; // Declarations made in the program
    private SubProgramDeclarationsNode functions; // Functions/procedures declared in the program
    private CompoundStatementNode main; // Main body of the program

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
     * Set the functions declared in the program
     *
     * @param functions A SubProgramDeclarationsNode containing all the functions/procedures declared
     */
    public void setFunctions(SubProgramDeclarationsNode functions) {
        this.functions = functions;
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
