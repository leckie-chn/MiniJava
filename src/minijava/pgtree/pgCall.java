package minijava.pgtree;

import java.util.Vector;

public class pgCall implements pgExp {

	public pgExp f0;
	
	public Vector<pgExp> f1 = new Vector<pgExp>();
	
	@Override
	public void PrintInstruction(int depth) {
		this.f0.PrintInstruction(depth);
		System.out.print('(');
		for (pgExp _para : this.f1){
			_para.PrintInstruction(depth);
		}
		System.out.print(')');
	}

}
