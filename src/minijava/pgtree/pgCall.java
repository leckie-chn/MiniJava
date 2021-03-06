package minijava.pgtree;

import java.util.Vector;

import minijava.symboltable.MType;

public class pgCall implements pgExp {

	public pgSimpleExp f0;
	
	public Vector<pgTemp> f1 = new Vector<pgTemp>();
	
	public MType RetType;
	
	public pgCall(){
		
	}
	
	public pgCall(pgSimpleExp _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		System.out.print("CALL ");
		this.f0.PrintInstruction(depth);
		
		if (this.f0 instanceof pgStmtExp)
			for (int i = 0; i < depth; i++)
				System.out.print('\t');
		else
			System.out.print(" ");
		
		System.out.print("(");
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
