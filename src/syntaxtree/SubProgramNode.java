package syntaxtree;

import scanner.Type;

import java.util.ArrayList;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * Represents a single subprogram declarations
 *
 * @author Bob Laskowski
 */
public class SubProgramNode extends ProgramNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    private Type returnType; // Return type of the subprogram (REAL/INTEGER for functions, NULL for procedures)
    private ArrayList<VariableNode> args; // Function arguments

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Create a new SubProgram with a name
     *
     * @param aName Name of the function or procedure
     */
    public SubProgramNode(String aName) {
        this.name = aName;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Get the name of the function/procedure
     *
     * @return the name of the function/procedure
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the main function of the subprogram
     *
     * @return A CompoundStatementNode representing the main function of the subprogram
     */
    public CompoundStatementNode getMain() {
        return main;
    }

    /**
     * Set the main body of the SubProgram
     *
     * @param main A CompoundStatementNode with the main body of the program
     */
    public void setMain(CompoundStatementNode main) {
        this.main = main;
    }

    /**
     * Get the variables declared in the SubProgram
     *
     * @return A DeclarationsNode with the variables declared
     */
    public DeclarationsNode getVariables() {
        return variables;
    }

    /**
     * Set the variables in the SubProgram
     *
     * @param variables A DeclarationsNode with the variables declared
     */
    public void setVariables(DeclarationsNode variables) {
        this.variables = variables;
    }

    /**
     * Get the functions/procedures declared in the SubProgram
     *
     * @return A SubProgramDeclarationsNode with the functions/procedures declared
     */
    public SubProgramDeclarationsNode getFunctions() {
        return functions;
    }

    /**
     * Set the functions/procedures declared in the SubProgram
     *
     * @param functions A SubProgramDeclarationsNode with the functions/procedures declared
     */
    public void setFunctions(SubProgramDeclarationsNode functions) {
        this.functions = functions;
    }

    /**
     * Set the return type of the function
     *
     * @param t INTEGER/REAL return type
     */
    public void setReturnType(Type t) {
        returnType = t;
    }

    /**
     * Get the arguments of the subprogram
     *
     * @return An ArrayList of VariableNodes for the arguments of the subprogram
     */
    public ArrayList<VariableNode> getArgs() {
        return args;
    }

    /**
     * Set the arguments of the subprogram
     *
     * @param args An ArrayList of VariableNodes for the arguments of the subprogram
     */
    public void setArgs(ArrayList<VariableNode> args) {
        this.args = args;
    }

    /**
     * Print out the node with proper indentation to build a visual syntax tree
     *
     * @param level The level of indentation, counting begins at zero
     * @return A String with the tree representation of the Node
     */
    @Override
    public String indentedToString(int level) {
        StringBuilder answer = new StringBuilder(this.indentation(level));
        answer.append("SubProgram: ").append(name).append(", Return: ").append(returnType).append("\n");
        answer.append(this.indentation(level)).append("Arguments: (");
        if (args != null) {
            for (int i = 0; i < args.size(); i++) {
                if (i != args.size() - 1)
                    answer.append(args.get(i).getName()).append(" [").append(args.get(i).getType()).append("], ");
                else answer.append(args.get(i).getName()).append(" [").append(args.get(i).getType()).append("]");
            }
        }
        answer.append(")\n");
        answer.append(variables.indentedToString(level + 1));
        answer.append(functions.indentedToString(level + 1));
        answer.append(main.indentedToString(level + 1));
        return answer.toString();
    }
}
