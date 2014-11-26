package spiglet.kgtree;

public class kgLabel implements kgSimpleExp, kgStmt {

	public String f0;
	
	public kgLabel(){
		
	}
	
	public kgLabel(String _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print(this.f0);
	}

}
