package spiglet.kgtree;

public class kgInteger implements kgSimpleExp {

	public int f0;
	
	public kgInteger(){
		
	}
	
	public kgInteger(int _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print(this.f0);
	}

}
