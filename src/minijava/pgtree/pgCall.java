package minijava.pgtree;

import java.util.Vector;

public class pgCall implements pgExp {

	public pgExp f0;
	
	public Vector<pgExp> f1 = new Vector<pgExp>();
	
	public pgCall(){
		
	}
	
	public pgCall(pgExp _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		this.f0.PrintInstruction(depth);
		
		if (this.f0 instanceof pgStmtExp)
			for (int i = 0; i < depth; i++)
				System.out.print('\t');
		else
			System.out.print(" ");
		
		System.out.print('(');
		for (pgExp _para : this.f1){
			_para.PrintInstruction(depth);
			if (_para instanceof pgStmtExp)
				for (int i = 0; i < depth; i++)
					System.out.print('\t');
			else
				System.out.print(" ");
		}
		
		System.out.print(')');
	}

}
