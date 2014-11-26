package spiglet.stmtnode;

import spiglet.kgtree.kgInteger;
import spiglet.kgtree.kgPrintStmt;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgSimpleExp;

public class spgPrint extends spgStmtNode {

	public spgPrint(spgSimpleExp A){
		super(null, A, null, null);
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		kgSimpleExp src = null;
		if (this.SrcOperand1 instanceof spgInteger)
			src = new kgInteger(((spgInteger)this.SrcOperand1).arg);
		else {
			src = this.GetReg(context, (spgTempRef)this.SrcOperand1, 1);
		}
		
		context.f4.f0.add(new kgPrintStmt(
				src
				));
	}
}
