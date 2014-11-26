package spiglet.kgtree;

public class kgHStoreStmt implements kgStmt {

	public kgReg f0;
	
	public int f1;
	
	public kgReg f2;
	
	public kgHStoreStmt(){
		
	}
	
	public kgHStoreStmt(kgReg _f0, int _f1, kgReg _f2){
		this.f0 = _f0;
		this.f1 = _f1;
		this.f2 = _f2;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("\t\tHSTORE ");
		this.f0.PrintInstruction();
		System.out.printf(" %d ", this.f1);
		this.f2.PrintInstruction();
		System.out.println();
	}

}
