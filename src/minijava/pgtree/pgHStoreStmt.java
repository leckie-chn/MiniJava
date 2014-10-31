package minijava.pgtree;

public class pgHStoreStmt implements pgStmt {

	public pgTemp f0;
	
	public pgIntegerLiteral f1;
	
	public pgTemp f2;
	
	public pgHStoreStmt(){
		
	}
	
	public pgHStoreStmt(pgTemp _f0, pgIntegerLiteral _f1, pgTemp _f2){
		this.f0 = _f0;
		this.f1 = _f1;
		this.f2 = _f2;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		System.out.print("HSTORE ");
		this.f0.PrintInstruction(depth);
		
		System.out.print(" ");
		
		System.out.print(this.f1.f0 + " ");
		this.f2.PrintInstruction(depth);
	}

}