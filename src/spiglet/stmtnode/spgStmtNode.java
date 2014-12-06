package spiglet.stmtnode;

import spiglet.flowgraph.RegisterRef;
import spiglet.kgtree.kgALoadStmt;
import spiglet.kgtree.kgInteger;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgReg;
import spiglet.kgtree.kgSpilledArg;

public class spgStmtNode implements spgRoot {
	public final spgTempRef TargetOperand;
	public final spgSimpleExp SrcOperand1;
	public final spgSimpleExp SrcOperand2;
	public final spgSimpleExp SrcOperand3;
	
	public spgStmtNode(spgTempRef A, spgSimpleExp B, spgSimpleExp C, spgSimpleExp D){
		this.TargetOperand = A;
		this.SrcOperand1 = B;
		this.SrcOperand2 = C;
		this.SrcOperand3 = D;
	}
	
	public void DoTranslation(kgProcedure context){ 
		
	}
	
	// warning: for src register only
	protected kgReg GetReg(kgProcedure context, spgTempRef temp, int vindex){
		if (temp.register != null)
			return new kgReg(temp.register);
		else {
			kgReg _ret = null;
			if (vindex == 2){
				_ret = new kgReg(RegisterRef.ARegs[0]);
			} else {
				_ret = new kgReg(RegisterRef.VRegs[vindex]);
			}
			context.f4.f0.add(new kgALoadStmt(
					_ret,
					new kgSpilledArg(temp.StackPos)
					));
			return _ret;
		}
	}
}
