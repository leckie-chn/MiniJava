package minijava.pgtree;

public class pgHAllocate implements pgExp {

	public pgExp f0;
	@Override
	public void PrintInstruction(int depth) {
		System.out.print("HALLOCATE ");
		this.f0.PrintInstruction(depth);
	}

}
