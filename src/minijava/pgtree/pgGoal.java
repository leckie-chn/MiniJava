package minijava.pgtree;

import java.util.Vector;

public class pgGoal implements pgNode {

	public final pgStmtList f0;
	
	private final Vector<pgProcedure> f1 = new Vector<pgProcedure>();
	
	public pgTemp [] SpecialTemp = new pgTemp[20]; 
	
	public pgGoal(){
		this.f0 = new pgStmtList();
		
		// special temp init
		for (int i = 0; i < 20; i++)
			this.SpecialTemp[i].f0 = i;
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
