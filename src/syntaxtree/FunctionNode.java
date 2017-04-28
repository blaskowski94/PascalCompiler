package syntaxtree;

import scanner.Type;

import java.util.ArrayList;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * April 27th, 2017
 * <p>
 * The node for a call to a function. Stores the name and an ArrayList of the arguments.
 *
 * @author Bob Laskowski
 */
public class FunctionNode extends VariableNode {

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    // The name of the arguments associated with this node
    private ArrayList<ExpressionNode> args;

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Creates a FunctionNode and the parent variableNode with the given name
     *
     * @param attr The attribute for this value node.
     */
    public FunctionNode(String attr) {
        super(attr);
        args = new ArrayList<>();
        this.type = null;
    }

    /**
     * Creates a FuncitonNode and the parent variableNode with the given name and Type
     *
     * @param attr Name of functionNode
     * @param t    Type of functionNode
     */
    public FunctionNode(String attr, Type t) {
        super(attr);
        args = new ArrayList<>();
        this.type = t;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Returns the name of the function
     *
     * @return The name of this FunctionNode
     */
    public String getName() {
        return (super.getName());
    }

    /**
     * Return the ArrayList of expression nodes associated with the function arguments
     *
     * @return ArrayList of ExpressionNodes
     */
    public ArrayList<ExpressionNode> getArgs() {
        return this.args;
    }

    /**
     * Sets the ArrayList of ExpressionNodes for the function arguments
     *
     * @param input an ArrayList of ExpressionNodes for function args
     */
    public void setArgs(ArrayList<ExpressionNode> input) {
        this.args = input;
    }

    /**
     * Add an argument to the FunctionNode
     *
     * @param ex An ExpressionNode to be an argument
     */
    public void addArg(ExpressionNode ex) {
        args.add(ex);
    }

    /**
     * Remove the ArrayList of arguments from the function and return that list
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
     * Returns the name of the variable as the description of this node.
     *
     * @return The attribute String of this node.
     */
    @Override
    public String toString() {
        return ("VariableNode: " + super.getName() + "ExpressionNode: " + args);
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
        answer.append("Name: ").append(super.getName()).append("\n");
        answer.append(this.indentation(level));
        answer.append("Arguments: \n");
        for (ExpressionNode expression : args) {
            answer.append(expression.indentedToString(level + 1));
        }
        return answer.toString();
    }

    /**
     * Determines if two FunctionNodes are equal. They are equal if they have the same name and same arguments
     *
     * @param o Another FunctionNode
     * @return True if equal, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        boolean answer = false;
        if (o instanceof FunctionNode) {
            FunctionNode other = (FunctionNode) o;
            if (super.getName().equals(other.getName()) && (this.args.equals(other.getArgs()))) answer = true;
        }
        return answer;
    }
}