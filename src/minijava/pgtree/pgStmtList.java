package minijava.pgtree;

import java.util.Vector;

public class pgStmtList implements pgNode {

	public Vector<pgStmt> f0 = new Vector<pgStmt>();
	@Override
	public void PrintInstruction(int depth) {
		for (pgStmt _stmt : this.f0){
			_stmt.PrintInstruction(depth);
			System.out.println();
		}
	}
}
