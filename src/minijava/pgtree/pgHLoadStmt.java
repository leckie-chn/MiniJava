package minijava.pgtree;

public class pgHLoadStmt implements pgStmt {

	public pgTemp f0;
	
	public pgTemp f1;
	
	public pgIntegerLiteral f2;
	
	public pgHLoadStmt(){
		
	}
	
	public pgHLoadStmt(pgTemp _f0, pgTemp _f1, pgIntegerLiteral _f2){
		this.f0 = _f0;
		this.f1 = _f1;
		this.f2 = _f2;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("HLOAD " + this.f0 + " ");
		this.f1.PrintInstruction(depth);
		System.out.print(" ");
		
		System.out.println(this.f2.f0);
	}

}
