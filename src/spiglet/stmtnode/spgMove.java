package spiglet.stmtnode;

import spiglet.flowgraph.RegisterRef;
import spiglet.kgtree.kgALoadStmt;
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
		kgSimpleExp src = null;
		if (this.SrcOperand1 instanceof spgTempRef){
			spgTempRef srctemp = (spgTempRef) this.SrcOperand1;
			RegisterRef target = this.TargetOperand.register;
			if (target == null){
				if (this.TargetOperand.StackPos < 0)
					return;
				target = RegisterRef.VRegs[1];
			}
			if (srctemp.register == null){
				context.f4.f0.add(new kgALoadStmt(
						new kgReg(target),
						new kgSpilledArg(srctemp.StackPos)
						));
			} else {
				if (srctemp.register == target)
					return;
				else
					context.f4.f0.add(new kgMoveStmt(
							new kgReg(target),
							new kgReg(srctemp.register)
							));
			}
			if (this.TargetOperand.register == null && this.TargetOperand.StackPos >= 0)
				context.f4.f0.add(new kgAStoreStmt(
						new kgSpilledArg(this.TargetOperand.StackPos),
						new kgReg(target)
						));
			return;
		} else {
			if (this.SrcOperand1 instanceof spgInteger)
				src = new kgInteger(((spgInteger)this.SrcOperand1).arg);
			else
				src = new kgLabel(((spgLabel)this.SrcOperand1).arg);
			
			RegisterRef target = this.TargetOperand.register;
			if (target == null){
				if (this.TargetOperand.StackPos < 0)
					return;
				target = RegisterRef.VRegs[1];
			}
			context.f4.f0.add(new kgMoveStmt(
					new kgReg(target),
					src
					));
			if (this.TargetOperand.register == null && this.TargetOperand.StackPos >= 0)
				context.f4.f0.add(new kgAStoreStmt(
						new kgSpilledArg(this.TargetOperand.StackPos),
						new kgReg(target)
						));
			return;
		}
	}
}
