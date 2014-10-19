package minijava.pgtree;

public class pgLabel implements pgExp, pgStmt {

	public static int StmtLabelCnt = 0;
	
	public String f0;
	
	public pgLabel(String _FuncName){
		f0 = _FuncName;
	}
	
	public pgLabel(){
		f0 = "L" + StmtLabelCnt++;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		System.out.print(this.f0);
	}
	
	@Override
	public String toString(){
		return this.f0;
	}

}
