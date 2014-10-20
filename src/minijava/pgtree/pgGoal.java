package minijava.pgtree;

import java.util.Vector;

public class pgGoal implements pgNode {

	private final pgStmtList f0;
	
	private final Vector<pgProcedure> f1 = new Vector<pgProcedure>();
	
	public pgGoal(pgStmtList _f0){
		this.f0 = _f0;
	}
	
	public void AddProcedure(pgProcedure _procedure){
		this.f1.add(_procedure);
	}
	
	@Override
	public void PrintInstruction(int depth) {
		System.out.println("MAIN");
		this.f0.PrintInstruction(1);
		System.out.println("END");
		for (pgProcedure _procedure : this.f1){
			_procedure.PrintInstruction(0);
			System.out.println('\n');
		}
	}

}
