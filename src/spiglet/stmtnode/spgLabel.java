package spiglet.stmtnode;

public class spgLabel extends spgStmtNode implements spgSimpleExp {
	public final String arg;
	
	public spgLabel(String _arg){
		super(null, null, null, null);
		this.arg = _arg;
	}
}
