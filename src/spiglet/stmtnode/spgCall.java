package spiglet.stmtnode;

import java.util.Vector;

import spiglet.flowgraph.RegisterRef;
import spiglet.kgtree.kgALoadStmt;
import spiglet.kgtree.kgAStoreStmt;
import spiglet.kgtree.kgCallStmt;
import spiglet.kgtree.kgLabel;
import spiglet.kgtree.kgMoveStmt;
import spiglet.kgtree.kgPassArgStmt;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgReg;
import spiglet.kgtree.kgSimpleExp;
import spiglet.kgtree.kgSpilledArg;

public class spgCall extends spgStmtNode {

	public final Vector<spgTempRef> ParaTable;
	public spgCall(spgTempRef A, spgSimpleExp B, Vector<spgTempRef> _table){
		super(A, B, null, null);
		this.ParaTable = _table;
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		// caller save registers
		for (int i = 0; i < 10; i++)
			context.f4.f0.add(new kgAStoreStmt(
					new kgSpilledArg(i + context.f2 - 10),
					new kgReg(RegisterRef.TRegs[i])
					));
		
		// pass arguments
		if (context.f3 < this.ParaTable.size())
			context.f3 = this.ParaTable.size();
		for (int i = 0; i < this.ParaTable.size(); i++){
			if (i < 4){
				if (this.ParaTable.elementAt(i).register != null)
					context.f4.f0.add(new kgMoveStmt(
							new kgReg(RegisterRef.ARegs[i]),
							new kgReg(this.ParaTable.elementAt(i).register)
							));
				else {
					context.f4.f0.add(new kgALoadStmt(
							new kgReg(RegisterRef.ARegs[i]),
							new kgSpilledArg(this.ParaTable.elementAt(i).StackPos)
							));
				}
			} else {
				kgReg parareg = this.GetReg(context, this.ParaTable.elementAt(i), 1);
				context.f4.f0.add(new kgPassArgStmt(
						i - 3,
						parareg
						));
			}
		}
		
		// call instruction
		kgSimpleExp subroutine = null;
		if (this.SrcOperand1 instanceof spgLabel)
			subroutine = new kgLabel(((spgLabel)this.SrcOperand1).arg);
		else
			subroutine = this.GetReg(context, (spgTempRef)this.SrcOperand1, 1);
		context.f4.f0.add(new kgCallStmt(subroutine));
		
		
		
		// caller restore registers
		for (int i = 9; i >= 0; i--)
			context.f4.f0.add(new kgALoadStmt(
					new kgReg(RegisterRef.TRegs[i]),
					new kgSpilledArg(i + context.f2 - 10)
					));
		
		RegisterRef target = this.TargetOperand.register;
		if (target == null){
			context.f4.f0.add(new kgAStoreStmt(
					new kgSpilledArg(this.TargetOperand.StackPos),
					new kgReg(RegisterRef.VRegs[0])
					));
		} else 
			context.f4.f0.add(new kgMoveStmt(
					new kgReg(this.TargetOperand.register),
					new kgReg(RegisterRef.VRegs[0])
					));
		
	}
}
