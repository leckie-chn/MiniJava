package spiglet.stmtnode;

import spiglet.kgtree.kgErrorStmt;
import spiglet.kgtree.kgProcedure;

public class spgError extends spgStmtNode {

	public spgError(){
		super(null, null, null, null);
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		context.f4.f0.add(new kgErrorStmt());
	}
}
