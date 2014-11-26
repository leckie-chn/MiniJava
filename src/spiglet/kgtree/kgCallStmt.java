package spiglet.kgtree;

public class kgCallStmt implements kgStmt {

	public kgSimpleExp f0;
	
	public kgCallStmt(){
		
	}
	
	public kgCallStmt(kgSimpleExp _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("\t\tCALL ");
		this.f0.PrintInstruction();
		System.out.println();
	}

}
