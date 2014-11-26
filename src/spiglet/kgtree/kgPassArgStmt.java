package spiglet.kgtree;

public class kgPassArgStmt implements kgStmt {

	public int f0;
	
	public kgReg f1;
	
	public kgPassArgStmt(){
		
	}
	
	public kgPassArgStmt(int _f0, kgReg _f1){
		this.f0 = _f0;
		this.f1 = _f1;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.printf("\t\tPASSARG %d ", this.f0);
		this.f1.PrintInstruction();
		System.out.println();
	}

}
