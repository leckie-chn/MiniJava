package spiglet.stmtnode;



public class spgBinOp extends spgStmtNode {

	public final OperatorEnum opcode;
	public spgBinOp(spgTempRef A, spgTempRef B, spgSimpleExp C, OperatorEnum opcode){
		super(A, B, C, null);
		this.opcode = opcode;
	}
}
