package minijava.pgtree;

public class pgProcedure implements pgNode {

	/**
	 * function name
	 */
	public pgLabel f0;
	
	/**
	 * function parameter number
	 */
	public pgIntegerLiteral f1;
	
	/**
	 * function implementation
	 */
	public pgStmtExp f2;
	
	public pgProcedure(){
		
	}
	
	public pgProcedure(pgLabel _f0, pgIntegerLiteral _f1, pgStmtExp _f2){
		this.f0 = _f0;
		this.f1 = _f1;
		this.f2 = _f2;
	}
	
	/**
	 * @param depth=0
	 */
	@Override
	public void PrintInstruction(int depth) {
		System.out.println(String.format("%s\t[%d]", this.f0, this.f1.f0));
		this.f2.PrintInstruction(0);
	}

}
