package spiglet.stmtnode;

import spiglet.kgtree.kgHStoreStmt;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgReg;

public class spgHStore extends spgStmtNode {
	
	public spgHStore(spgTempRef A, spgInteger B, spgTempRef C){
		super(null, A, B, C);
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		
		kgReg src1 = this.GetReg(context, (spgTempRef)this.SrcOperand1, 0);
		kgReg src3 = this.GetReg(context, (spgTempRef)this.SrcOperand3, 1);
		context.f4.f0.add(new kgHStoreStmt(
				src1,
				((spgInteger)this.SrcOperand2).arg,
				src3
				));
	}
}
