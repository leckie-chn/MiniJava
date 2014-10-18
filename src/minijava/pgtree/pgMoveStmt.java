package minijava.pgtree;

public class pgMoveStmt implements pgStmt {

	public pgTemp f0;
	
	public pgExp f1;
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("MOVE " + this.f0);
		this.f1.PrintInstruction(depth);
	}

}
