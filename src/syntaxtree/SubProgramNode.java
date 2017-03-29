package syntaxtree;

/**
 * Created by Bob on 3/4/2017.
 */
public class SubProgramNode extends SubProgramDeclarationsNode {
    private String name;
    private DeclarationsNode variables;
    private SubProgramDeclarationsNode functions;
    private CompoundStatementNode main;

    public SubProgramNode(String aName) {
        this.name = aName;
    }

    public DeclarationsNode getVariables() {
        return variables;
    }

    public void setVariables(DeclarationsNode variables) {
        this.variables = variables;
    }

    public SubProgramDeclarationsNode getFunctions() {
        return functions;
    }

    public void setFunctions(SubProgramDeclarationsNode functions) {
        this.functions = functions;
    }

    public CompoundStatementNode getMain() {
        return main;
    }

    public void setMain(CompoundStatementNode main) {
        this.main = main;
    }

    @Override
    public String indentedToString(int level) {
        String answer = this.indentation(level);
        answer += "SubProgram: " + name + "\n";
        answer += variables.indentedToString(level + 1);
        answer += functions.indentedToString(level + 1);
        answer += main.indentedToString(level + 1);
        return answer;
    }
}
