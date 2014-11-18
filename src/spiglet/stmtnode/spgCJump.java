package spiglet.stmtnode;

public class spgCJump extends spgStmtNode  implements spgJumpAble {

	public spgCJump(spgTempRef A, spgLabel B){
		super(null, A, B, null);
	}
	
	public spgLabel getTarget(){
		return (spgLabel)this.SrcOperand2;
	}
}
