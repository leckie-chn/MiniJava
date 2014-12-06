package spiglet.stmtnode;

import spiglet.flowgraph.RegisterRef;
import spiglet.kgtree.kgAStoreStmt;
import spiglet.kgtree.kgHAllocate;
import spiglet.kgtree.kgInteger;
import spiglet.kgtree.kgMoveStmt;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgReg;
import spiglet.kgtree.kgSimpleExp;
import spiglet.kgtree.kgSpilledArg;

public class spgAllocate extends spgStmtNode {

	public spgAllocate(spgTempRef A, spgSimpleExp B){
		super(A, B, null, null);
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		RegisterRef target = this.TargetOperand.register;
		if (target == null){
			// spilled 
			target = RegisterRef.VRegs[1];
		}
		
		kgSimpleExp src = null;
		if (this.SrcOperand1 instanceof spgInteger)
			src = new kgInteger(((spgInteger)this.SrcOperand1).arg);
		else {
			spgTempRef temp = (spgTempRef) this.SrcOperand1;
			src = this.GetReg(context, temp, 0);
		}
		
		// move stmt itself
		context.f4.f0.add(new kgMoveStmt(
				new kgReg(target),
				new kgHAllocate(src)
				));
		
		if (this.TargetOperand.register == null){
			context.f4.f0.add(new kgAStoreStmt(
					new kgSpilledArg(this.TargetOperand.StackPos),
					new kgReg(target)
					));
		}

	}
}
