package spiglet.kgtree;

public class kgProcedure implements kgNode {

	public kgLabel f0;
	
	public int f1;
	
	public int f2;
	
	public int f3;	// maximum sub routine parameter number , will be determined later
	
	public final kgStmtList f4 = new kgStmtList();
	
	public kgProcedure(){
		
	}
	
	public kgProcedure(kgLabel _f0, int _f1, int _f2){
		this.f0 = _f0;
		this.f1 = _f1;
		this.f2 = _f2;
	}
	
	@Override
	public void PrintInstruction() {
		// TODO Auto-generated method stub
		this.f0.PrintInstruction();
		System.out.printf(" [%d] [%d] [%d]\n", this.f1, this.f2, this.f3);
		
		this.f4.PrintInstruction();
		System.out.println("END");
	}

}
