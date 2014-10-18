package minijava.pgtree;

public class pgCJumpStmt implements pgStmt {

	public pgExp f0;
	
	public pgLabel f1;
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("CJUMP ");
		this.f0.PrintInstruction(depth);
		System.out.println(this.f1);
	}

}
