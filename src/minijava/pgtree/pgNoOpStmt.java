package minijava.pgtree;

public class pgNoOpStmt implements pgStmt {

	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.println("NOOP");
	}

}
