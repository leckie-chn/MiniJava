package spiglet.stmtnode;

import spiglet.flowgraph.RegisterRef;
import spiglet.kgtree.kgAStoreStmt;
import spiglet.kgtree.kgInteger;
import spiglet.kgtree.kgLabel;
import spiglet.kgtree.kgMoveStmt;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgReg;
import spiglet.kgtree.kgSimpleExp;
import spiglet.kgtree.kgSpilledArg;

public class spgMove extends spgStmtNode {

	public spgMove(spgTempRef A, spgSimpleExp B){
		super(A, B, null, null);
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		if (this.TargetOperand == this.SrcOperand1)
			return;
		RegisterRef target = this.TargetOperand.register;
		if (target == null){
			target = RegisterRef.VRegs[1];
		}
		kgSimpleExp src = null;
		if (this.SrcOperand1 instanceof spgTempRef){
			src = this.GetReg(context, (spgTempRef)this.SrcOperand1, 0);
		} else if (this.SrcOperand1 instanceof spgInteger){
			src = new kgInteger(((spgInteger)this.SrcOperand1).arg);
		} else {
			src = new kgLabel(((spgLabel)this.SrcOperand1).arg);
		}
		context.f4.f0.add(new kgMoveStmt(
				new kgReg(target),
				src
				));
		
		if (this.TargetOperand.register == null){
			context.f4.f0.add(new kgAStoreStmt(
					new kgSpilledArg(this.TargetOperand.StackPos),
					new kgReg(target)
					));
		}
		return;
	}
}
