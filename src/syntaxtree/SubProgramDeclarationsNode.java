package syntaxtree;

import java.util.ArrayList;

/**
 * Represents a collection of subprogram declarations
 *
 * @author Erik Steinmetz
 */
public class SubProgramDeclarationsNode extends SyntaxTreeNode {

    private ArrayList<SubProgramNode> procs = new ArrayList<SubProgramNode>();

    public void addSubProgramDeclaration(SubProgramNode aSubProgram) {
        procs.add(aSubProgram);
    }

    public void addall(ArrayList<SubProgramNode> aSubProgram) {
        procs.addAll(aSubProgram);
    }

    public ArrayList<SubProgramNode> getProcs() {
        return procs;
    }

    public String indentedToString(int level) {
        String answer = this.indentation(level);
        answer += "SubProgramDeclarations\n";
        for (SubProgramNode subProg : procs) {
            answer += subProg.indentedToString(level + 1);
        }
        return answer;
    }

}