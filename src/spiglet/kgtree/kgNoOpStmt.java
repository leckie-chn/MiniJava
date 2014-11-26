package spiglet.kgtree;

public class kgNoOpStmt implements kgStmt {

	@Override
	public void PrintInstruction() {
		System.out.println("\t\tNOOP");
	}

}
