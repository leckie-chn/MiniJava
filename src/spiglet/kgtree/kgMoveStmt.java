package spiglet.kgtree;

public class kgMoveStmt implements kgStmt {

	public kgReg f0;
	
	public kgExp f1;
	
	public kgMoveStmt(){
		
	}
	
	public kgMoveStmt(kgReg _f0, kgExp _f1){
		this.f0 = _f0;
		this.f1 = _f1;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("\t\tMOVE ");
		this.f0.PrintInstruction();
		System.out.print(' ');
		this.f1.PrintInstruction();
		System.out.println();
	}

}
