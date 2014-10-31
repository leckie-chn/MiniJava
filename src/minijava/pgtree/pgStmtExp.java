package minijava.pgtree;

public class pgStmtExp implements pgExp {

	public pgStmtList f0;
	
	public pgSimpleExp f1;
	
	public pgStmtExp(){
		
	}
	
	public pgStmtExp(pgStmtList _f0, pgSimpleExp _f1){
		this.f0 = _f0;
		this.f1 = _f1;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.println("BEGIN");
		this.f0.PrintInstruction(depth + 1);
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("RETURN\t");
		this.f1.PrintInstruction(depth);
		System.out.println();
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("END");
	}

}
