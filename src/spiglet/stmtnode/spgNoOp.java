package spiglet.stmtnode;

import spiglet.kgtree.kgNoOpStmt;
import spiglet.kgtree.kgProcedure;

public class spgNoOp extends spgStmtNode {

	public spgNoOp(){
		super(null, null, null, null);
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		context.f4.f0.add(new kgNoOpStmt());
	}
}
