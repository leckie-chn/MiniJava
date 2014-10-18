package minijava.pgtree;

public class pgBinOp implements pgExp {

	public OperatorEnum f0;
	
	public pgExp f1;
	
	public pgExp f2;
	
	@Override
	public void PrintInstruction(int depth) {
		System.out.print(this.f0 + " ");
		this.f1.PrintInstruction(depth);
		System.out.print(" ");
		this.f2.PrintInstruction(depth);
	}

}
