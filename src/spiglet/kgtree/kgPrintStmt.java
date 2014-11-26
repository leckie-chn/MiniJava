package spiglet.kgtree;

public class kgPrintStmt implements kgStmt {

	public kgSimpleExp f0;
	
	public kgPrintStmt(){
		
	}
	
	public kgPrintStmt(kgSimpleExp _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("\t\tPRINT ");
		this.f0.PrintInstruction();
		System.out.println();
	}

}
