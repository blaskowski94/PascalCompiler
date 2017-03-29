package syntaxtree;

import java.util.ArrayList;

/**
 * Created by Bob on 3/13/2017.
 */
public class ProcedureStatementNode extends StatementNode {

    private VariableNode variable = null;
    private ArrayList<ExpressionNode> expNode = new ArrayList();

    //add a new node
    public void addExpNode(ExpressionNode input) {
        expNode.add(input);
    }

    public void addAllExpNode(ArrayList<ExpressionNode> input) {
        expNode.addAll(input);
    }

    //getters
    public VariableNode getVariable() {
        return this.variable;
    }

    //setters
    public void setVariable(VariableNode input) {
        this.variable = input;
    }

    public ArrayList<ExpressionNode> getExpNode() {
        return expNode;
    }

    public void setExpNode(ArrayList<ExpressionNode> input) {
        this.expNode = input;
    }

    @Override
    public String indentedToString(int level) {
        String answer = this.indentation(level);
        answer += "Procedure: ";
        answer += this.variable + "\n";
        for (ExpressionNode exp : expNode) {
            answer += exp.indentedToString(level + 1);
        }
        return answer;
    }
}