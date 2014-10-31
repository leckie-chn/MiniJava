package minijava.pgtree;

public class pgPrintStmt implements pgStmt {

	public pgSimpleExp f0;
	
	public pgPrintStmt(){
		
	}
	
	public pgPrintStmt(pgSimpleExp _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("PRINT ");
		this.f0.PrintInstruction(depth);
	}

}
