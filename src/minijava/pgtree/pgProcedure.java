package minijava.pgtree;

public class pgProcedure implements pgNode {

	/**
	 * function name
	 */
	public pgLabel f0;
	
	/**
	 * function parameter number
	 */
	public int f1;
	
	/**
	 * function implementation
	 */
	public pgStmtExp f2;
	
	/**
	 * @param depth=0
	 */
	@Override
	public void PrintInstruction(int depth) {
		System.out.println(String.format("%s\t[%d]", this.f0, this.f1));
		this.f2.PrintInstruction(0);
	}

}
