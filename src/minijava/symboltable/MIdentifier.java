package minijava.symboltable;

public class MIdentifier {
	
	private final String IDString;
	
	private final int LineNo;
	
	public MIdentifier(String _IDString, int _LineNo) {
		this.IDString = _IDString;
		this.LineNo = _LineNo;
	}
	
	public String GetID(){
		return this.IDString;
	}
	
	public int GetLineNo(){
		return this.LineNo;
	}

}
