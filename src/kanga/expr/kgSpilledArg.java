package kanga.expr;

public class kgSpilledArg implements kgExprBase {
	private int a0;
	
	public kgSpilledArg(int argpos){
		this.a0 = argpos;
	}
	
	public int GetArgPosition(){
		return this.a0;
	}
}
