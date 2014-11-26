package spiglet.kgtree;

public class kgALoadStmt implements kgStmt {

	public kgReg f0;
	
	public kgSpilledArg f1;
	
	public kgALoadStmt(){
		
	}
	
	public kgALoadStmt(kgReg _f0, kgSpilledArg _f1){
		this.f0 = _f0;
		this.f1 = _f1;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("\t\tALOAD ");
		this.f0.PrintInstruction();
		System.out.print(' ');
		this.f1.PrintInstruction();
		System.out.println();
	}

}
