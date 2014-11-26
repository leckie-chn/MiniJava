package spiglet.kgtree;

public class kgHLoadStmt implements kgStmt {
	
	public kgReg f0;
	
	public kgReg f1;
	
	public int f2;
	
	public kgHLoadStmt(){
		
	}
	
	public kgHLoadStmt(kgReg _f0, kgReg _f1, int _f2){
		this.f0 = _f0;
		this.f1 = _f1;
		this.f2 = _f2;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("\t\tHLOAD ");
		this.f0.PrintInstruction();
		System.out.print(' ');
		this.f1.PrintInstruction();
		System.out.printf(" %d\n", this.f2);
	}

}
