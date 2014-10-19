package minijava.pgtree;

public class pgTemp implements pgExp {

	public int f0;
	public pgTemp(){
		this.f0 = pgTemp.TempCnt++;
	}
	
	@Override
	public void PrintInstruction(int depth) {
		System.out.print("TEMP " + this.f0);
	}

	/*********************** static, for temporary variable allocation **************************/
	private static int TempCnt = 20;
	
	public static int TempAlloc(){
		return pgTemp.TempCnt++;
	}
}
