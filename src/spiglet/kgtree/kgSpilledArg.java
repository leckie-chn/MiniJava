package spiglet.kgtree;

public class kgSpilledArg implements kgNode {
	
	public int f0 = -1; // denotes the spilled arg position in the stack
	
	public kgSpilledArg(){
		
	}
	
	public kgSpilledArg(int _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.printf("SPILLEDARG %d", this.f0);
	}

}
