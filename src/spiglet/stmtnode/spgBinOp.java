package spiglet.stmtnode;

import spiglet.flowgraph.RegisterRef;
import spiglet.kgtree.kgAStoreStmt;
import spiglet.kgtree.kgBinOp;
import spiglet.kgtree.kgInteger;
import spiglet.kgtree.kgMoveStmt;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgReg;
import spiglet.kgtree.kgSimpleExp;
import spiglet.kgtree.kgSpilledArg;



public class spgBinOp extends spgStmtNode {

	public final OperatorEnum opcode;
	public spgBinOp(spgTempRef A, spgTempRef B, spgSimpleExp C, OperatorEnum opcode){
		super(A, B, C, null);
		this.opcode = opcode;
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		RegisterRef target = this.TargetOperand.register;
		if (target == null){
			target = RegisterRef.VRegs[1];
		}
		
		kgReg src1 = this.GetReg(context, (spgTempRef)this.SrcOperand1,	0);
		kgSimpleExp src2 = null;
		if (this.SrcOperand2 instanceof spgInteger)
			src2 = new kgInteger(((spgInteger)this.SrcOperand2).arg);
		else
			src2 = this.GetReg(context, (spgTempRef)this.SrcOperand2, 1);
		
		context.f4.f0.add(new kgMoveStmt(
				new kgReg(target),
				new kgBinOp(this.opcode, src1, src2)
				));
		
		if (this.TargetOperand.register == null){
			context.f4.f0.add(new kgAStoreStmt(
					new kgSpilledArg(this.TargetOperand.StackPos),
					new kgReg(target)
					));
		}
		
	}
}
