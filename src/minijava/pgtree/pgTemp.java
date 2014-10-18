package minijava.pgtree;

public class pgTemp implements pgExp {

	public int f0;
	@Override
	public void PrintInstruction(int depth) {
		System.out.print("TEMP " + this.f0);
	}

}
