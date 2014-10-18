package minijava.pgtree;

public class pgJumpStmt implements pgStmt {

	public pgLabel f0;
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		
		System.out.println(String.format("JUMP %s", this.f0));
	}

}
