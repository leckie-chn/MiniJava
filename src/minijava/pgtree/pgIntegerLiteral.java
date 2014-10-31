package minijava.pgtree;

public class pgIntegerLiteral implements pgSimpleExp {

	public int f0;
	
	public pgIntegerLiteral(int _f0){
		this.f0 = _f0;
	}
	@Override
	public void PrintInstruction(int depth) {
		System.out.print(f0);
	}

}
