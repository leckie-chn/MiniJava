package minijava.pgtree;

public class pgHStoreStmt implements pgStmt {

	public pgExp f0;
	
	public int f1;
	
	public pgExp f2;
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("HSTORE ");
		this.f0.PrintInstruction(depth);
		System.out.print(this.f1);
		this.f2.PrintInstruction(depth);
		System.out.println();
	}

}