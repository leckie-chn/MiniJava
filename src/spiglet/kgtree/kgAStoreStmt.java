package spiglet.kgtree;

public class kgAStoreStmt implements kgStmt {

	public kgSpilledArg f0;
	
	public kgReg f1;
	
	public kgAStoreStmt(){
		
	}
	
	public kgAStoreStmt(kgSpilledArg _f0, kgReg _f1){
		this.f0 = _f0;
		this.f1 = _f1;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("\t\tASTORE ");
		this.f0.PrintInstruction();
		System.out.print(' ');
		this.f1.PrintInstruction();
		System.out.println();
	}

}
