package kanga.expr;

public class kgReg implements kgSimpleExp {
	private String a0;
	
	public kgReg(String regname){
		this.a0 = regname;
	}
	
	public String GetRegName(){
		return "$" + this.a0;
	}

}
