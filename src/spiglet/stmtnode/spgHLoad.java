package spiglet.stmtnode;

import spiglet.flowgraph.RegisterRef;
import spiglet.kgtree.kgAStoreStmt;
import spiglet.kgtree.kgHLoadStmt;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgReg;
import spiglet.kgtree.kgSpilledArg;

public class spgHLoad extends spgStmtNode {

	public spgHLoad(spgTempRef A, spgTempRef B, spgInteger C){
		super(A, B, C, null);
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		RegisterRef target = this.TargetOperand.register;
		if (target == null){
			if (this.TargetOperand.StackPos < 0)
				return;
			target = RegisterRef.VRegs[1];
		}
		kgReg srcreg = this.GetReg(context, (spgTempRef)this.SrcOperand1, 1);
		context.f4.f0.add(new kgHLoadStmt(
				new kgReg(target),
				srcreg,
				((spgInteger)this.SrcOperand2).arg
				));
		
		if (this.TargetOperand.register == null && this.TargetOperand.StackPos >= 0)
			context.f4.f0.add(new kgAStoreStmt(
					new kgSpilledArg(this.TargetOperand.StackPos),
					new kgReg(target)
					));
	}
}
