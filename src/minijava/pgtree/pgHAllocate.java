package minijava.pgtree;

public class pgHAllocate implements pgExp {

	public pgSimpleExp f0;
	
	public pgHAllocate(){
		
	}
	
	public pgHAllocate(pgSimpleExp _f0){
		this.f0 = _f0;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		System.out.print("HALLOCATE ");
		this.f0.PrintInstruction(depth);
	}

}
