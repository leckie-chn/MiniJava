package spiglet.kgtree;

import java.util.Vector;

public class kgGoal implements kgNode {

	public kgProcedure f0;
	
	public Vector<kgProcedure> f1 = new Vector<kgProcedure>();
	
	@Override
	public void PrintInstruction() {
		// TODO Auto-generated method stub
		// this.f0.PrintInstruction();
		for (kgProcedure _function : this.f1){
			System.out.println();
			_function.PrintInstruction();
		}
	}

}
