package minijava.pgtree;

import java.util.Vector;

public class pgStmtList implements pgNode {

	public Vector<pgStmt> f0 = new Vector<pgStmt>();
	@Override
	public void PrintInstruction(int depth) {
		if (f0.lastElement() instanceof pgLabel)
			this.f0.add(new pgNoOpStmt());
		for (int i = 0; i < this.f0.size(); i++){
			pgStmt _stmt = this.f0.elementAt(i);
			_stmt.PrintInstruction(depth);
			if (_stmt instanceof pgLabel && i + 1 < this.f0.size() && this.f0.elementAt(i + 1) instanceof pgLabel)
				new pgNoOpStmt().PrintInstruction(depth);
			System.out.println();
		}
		
			
	}
}
