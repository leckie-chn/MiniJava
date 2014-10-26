package minijava.symboltable;

public class MIdentifier {
	
	private final String IDString;
	
	private final int LineNo;
	
	private final int ColumnNo;
	
	public MIdentifier(String _IDString, int _LineNo, int _ColumnNo) {
		this.IDString = _IDString;
		this.LineNo = _LineNo;
		this.ColumnNo = _ColumnNo;
	}
	
	public String GetID(){
		return this.IDString;
	}
	
	public int GetLineNo(){
		return this.LineNo;
	}
	
	public int GetColumnNo(){
		return this.ColumnNo;
	}

}
