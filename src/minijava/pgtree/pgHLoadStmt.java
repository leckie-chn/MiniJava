package minijava.pgtree;

public class pgHLoadStmt implements pgStmt {

	public pgTemp f0;
	
	public pgExp f1;
	
	public int f2;
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("HLOAD " + this.f0);
		this.f1.PrintInstruction(depth);
		System.out.println(this.f2);
	}

}
