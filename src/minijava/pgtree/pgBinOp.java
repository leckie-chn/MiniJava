package minijava.pgtree;

public class pgBinOp implements pgExp {

	public OperatorEnum f0;
	
	public pgTemp f1;
	
	public pgSimpleExp f2;
	
	public pgBinOp(){
		
	}
	
	public pgBinOp(OperatorEnum _f0, pgTemp _f1, pgSimpleExp _f2){
		this.f0 = _f0;
		this.f1 = _f1;
		this.f2 = _f2;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		System.out.print(this.f0 + " ");
		this.f1.PrintInstruction(depth);
		System.out.print(" ");
		this.f2.PrintInstruction(depth);
	}

}
