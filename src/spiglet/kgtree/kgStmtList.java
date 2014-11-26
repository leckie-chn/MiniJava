package spiglet.kgtree;

import java.util.Vector;

public class kgStmtList implements kgNode {

	public final Vector<kgStmt> f0 = new Vector<kgStmt>();
	
	@Override
	public void PrintInstruction() {
		for (int i = 0; i < this.f0.size(); i++){
			kgStmt stmt = this.f0.elementAt(i);
			stmt.PrintInstruction();
			if ((stmt instanceof kgLabel) && (i + 1 < this.f0.size()) && (this.f0.elementAt(i + 1) instanceof kgLabel))
				(new kgNoOpStmt()).PrintInstruction();
		}

	}

}
