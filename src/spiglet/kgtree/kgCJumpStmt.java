package spiglet.kgtree;

public class kgCJumpStmt implements kgStmt {

	public kgReg f0;
	
	public kgLabel f1;
	
	public kgCJumpStmt(){
		
	}
	
	public kgCJumpStmt(kgReg _f0, kgLabel _f1){
		this.f0 = _f0;
		this.f1 = _f1;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("\t\tCJUMP ");
		this.f0.PrintInstruction();
		System.out.print(" ");
		this.f1.PrintInstruction();
		System.out.println();
	}

}
