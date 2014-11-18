package spiglet.stmtnode;

import java.util.Vector;

public class spgCall extends spgStmtNode {

	public final Vector<spgTempRef> ParaTable;
	public spgCall(spgTempRef A, spgSimpleExp B, Vector<spgTempRef> _table){
		super(A, B, null, null);
		this.ParaTable = _table;
	}
	
}
