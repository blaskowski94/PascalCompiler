package syntaxtree;

import java.util.ArrayList;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * The node for a call to a procedure. Stores the name and an ArrayList of the arguments.
 *
 * @author Bob Laskowski
 */
public class ProcedureStatementNode extends StatementNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    // The function variable
    private String name;
    // An ArrayList of the Argument Expressions
    private ArrayList<ExpressionNode> args = new ArrayList<>();

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Creates a ProcedureStatementNode with the given name
     *
     * @param name The name of the Procedure
     */
    public ProcedureStatementNode(String name) {
        this.name = name;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Add an ArrayList of ExpressionNodes for the function arguments
     *
     * @param input ArrayList of ExpressionNodes
     */
    public void addAllExpNode(ArrayList<ExpressionNode> input) {
        args.addAll(input);
    }

    /**
     * Add an argument to the procedure
     *
     * @param exp An ExpressionNode to be a procedure argument
     */
    public void addArg(ExpressionNode exp) {
        args.add(exp);
    }

    /**
     * Get the function VariableNode
     *
     * @return VariableNode of function
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the function VariableNode
     *
     * @param input A function VariableNode
     */
    public void setName(String input) {
        this.name = input;
    }

    /**
     * Remove the ArrayList of arguments from the procedure and return that list
     *
     * @return ArrayList of ExpressionNodes
     */
    public ArrayList<ExpressionNode> removeArgs() {
        ArrayList<ExpressionNode> temp = new ArrayList<>();
        temp.addAll(args);
        args.clear();
        return temp;
    }

    /**
     * Return the ArrayList of expression nodes associated with the procedure arguments
     *
     * @return ArrayList of ExpressionNodes
     */
    public ArrayList<ExpressionNode> getArgs() {
        return args;
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
        answer.append("Procedure: ");
        answer.append(this.name).append("\n");
        for (ExpressionNode exp : args) {
            answer.append(exp.indentedToString(level + 1));
        }
        return answer.toString();
    }
}