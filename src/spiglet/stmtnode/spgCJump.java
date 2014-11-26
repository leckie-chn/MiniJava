package spiglet.stmtnode;

import spiglet.kgtree.kgCJumpStmt;
import spiglet.kgtree.kgLabel;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgReg;

public class spgCJump extends spgStmtNode  implements spgJumpAble {

	public spgCJump(spgTempRef A, spgLabel B){
		super(null, A, B, null);
	}
	
	public spgLabel getTarget(){
		return (spgLabel)this.SrcOperand2;
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		kgReg CondReg = this.GetReg(context, (spgTempRef)this.SrcOperand1, 1);
		context.f4.f0.add(new kgCJumpStmt(
				CondReg,
				new kgLabel(context.f0.f0 + "_" + ((spgLabel)this.SrcOperand2).arg)
				));
	}
}
