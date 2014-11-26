package spiglet.stmtnode;

import spiglet.kgtree.kgJumpStmt;
import spiglet.kgtree.kgLabel;
import spiglet.kgtree.kgProcedure;

public class spgJump extends spgStmtNode implements spgJumpAble{

	public spgJump(spgLabel A){
		super(null, A, null, null);
	}
	
	public spgLabel getTarget(){
		return (spgLabel)this.SrcOperand1;
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		context.f4.f0.add(new kgJumpStmt(
				new kgLabel(context.f0.f0 + "_" + ((spgLabel)this.SrcOperand1).arg)
				));
	}
}
