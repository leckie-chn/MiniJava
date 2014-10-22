package minijava.pgtree;

import minijava.symboltable.MType;

public class pgTemp implements pgExp {

	public int f0;
	
	public MType TempType = null;
	
	public pgTemp(){
		this.f0 = pgTemp.TempCnt++;
	}
	
	// just for special pgTemp setup
	public pgTemp(int flag){
		// do nothing
	}
	
	@Override
	public void PrintInstruction(int depth) {
		System.out.print("TEMP " + this.f0);
	}

	public String toString(){
		return "TEMP " + this.f0;
	}
	
	/*********************** static, for temporary variable allocation **************************/
	private static int TempCnt = 20;
	
	public static int TempAlloc(){
		return pgTemp.TempCnt++;
	}
}
