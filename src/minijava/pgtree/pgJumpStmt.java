package minijava.pgtree;

public class pgJumpStmt implements pgStmt {

	public pgLabel f0;
	
	public pgJumpStmt(){
		
	}
	
	public pgJumpStmt(pgLabel _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print('\t');
		
		System.out.println(String.format("JUMP %s", this.f0));
	}

}
