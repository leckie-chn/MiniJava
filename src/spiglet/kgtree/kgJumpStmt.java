package spiglet.kgtree;

public class kgJumpStmt implements kgStmt {

	public kgLabel f0;
	
	public kgJumpStmt(){
		
	}
	
	public kgJumpStmt(kgLabel _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("\t\tJUMP ");
		this.f0.PrintInstruction();
		System.out.println();
	}

}
