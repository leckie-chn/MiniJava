package minijava.pgtree;

public class pgCJumpStmt implements pgStmt {

	public pgTemp f0;
	
	public pgLabel f1;
	
	public pgCJumpStmt(){
		
	}
	
	public pgCJumpStmt(pgTemp _f0, pgLabel _f1){
		this.f0 = _f0;
		this.f1 = _f1;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("CJUMP ");
		this.f0.PrintInstruction(depth);
		
		System.out.print(" ");
		
		System.out.println(this.f1);
	}

}
