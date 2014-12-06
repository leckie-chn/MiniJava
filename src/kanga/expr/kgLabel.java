package kanga.expr;

public class kgLabel implements kgSimpleExp {
	
	private String a0;
	
	public kgLabel(String _a0){
		this.a0 = _a0;
	}
	
	public String toString(){
		return this.a0;
	}
	
}
