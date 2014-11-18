package spiglet.flowgraph;

import java.util.HashSet;
import java.util.Set;

import spiglet.stmtnode.spgStmtNode;
import spiglet.stmtnode.spgTempRef;

public class ProgramStatus {
	
	public final spgStmtNode Statement;
	
	public final Set<spgTempRef> AliveSet = new HashSet<spgTempRef>();
	
	public ProgramStatus(spgStmtNode _stmt){
		this.Statement = _stmt;
	}
}
