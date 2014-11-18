package spiglet.stmtnode;

public class spgJump extends spgStmtNode implements spgJumpAble{

	public spgJump(spgLabel A){
		super(null, A, null, null);
	}
	
	public spgLabel getTarget(){
		return (spgLabel)this.SrcOperand1;
	}
}
