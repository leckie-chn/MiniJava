package spiglet.kgtree;

public class kgHAllocate implements kgExp {

	public kgSimpleExp f0;
	
	public kgHAllocate(){
		
	}
	
	public kgHAllocate(kgSimpleExp _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print("HALLOCATE ");
		this.f0.PrintInstruction();
	}

}
