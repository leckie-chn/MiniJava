package kanga.expr;

public class kgInteger implements kgSimpleExp {
	private int a0;
	
	public kgInteger(int _a0){
		this.a0 = _a0;
	}
	
	public int GetValue(){
		return this.a0;
	}
	
}	
