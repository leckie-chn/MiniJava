package minijava.pgtree;

public class pgStmtExp implements pgExp {

	public pgStmtList f0;
	
	public pgExp f1;
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.println("BEGIN");
		this.f0.PrintInstruction(depth + 1);
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("RETURN");
		this.f1.PrintInstruction(depth);
		System.out.println();
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("END");
	}

}
