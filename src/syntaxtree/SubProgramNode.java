package syntaxtree;

import scanner.Type;

import java.util.ArrayList;

/**
 * Represents a single subprogram declarations
 *
 * Created by Bob on 3/4/2017.
 * @author Bob Laskowski
 */
public class SubProgramNode extends ProgramNode {

    private Type returnType; // Return type of the subprogram (REAL/INTEGER for functions, NULL for procedures)
    private ArrayList<VariableNode> args; // Function arguments
    private ExpressionNode returnVal;

    /**
     * Create a new SubProgram with a name
     *
     * @param aName Name of the function or procedure
     */
    public SubProgramNode(String aName) {
        this.name = aName;
    }

    /**
     * Get the name of the function/procedure
     *
     * @return the name of the function/procedure
     */
    public String getName() {
        return name;
    }

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

    public ArrayList<VariableNode> getArgs() {
        return args;
    }

    public void setArgs(ArrayList<VariableNode> args) {
        this.args = args;
    }

    public void setReturnVal(ExpressionNode ex) {
        returnVal = ex;
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
        answer += "SubProgram: " + name + ", Return: " + returnType + "\n";
        answer += this.indentation(level) + "Arguments: (";
        if (args != null) {
            for (int i = 0; i < args.size(); i++) {
                if (i != args.size() - 1) answer += args.get(i).getName() + " [" + args.get(i).getType() + "], ";
                else answer += args.get(i).getName() + " [" + args.get(i).getType() + "]";
            }
        }
        answer += ")\n";
        answer += variables.indentedToString(level + 1);
        answer += functions.indentedToString(level + 1);
        answer += main.indentedToString(level + 1);
        return answer;
    }
}
