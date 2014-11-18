package spiglet.stmtnode;

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
	
}
