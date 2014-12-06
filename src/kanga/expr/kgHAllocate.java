package kanga.expr;

public class kgHAllocate implements kgExp {
	private kgSimpleExp a0;
	
	public kgHAllocate(kgSimpleExp _a0){
		this.a0 = _a0;
	}
	
	public kgSimpleExp GetAllocateExp(){
		return this.a0;
	}
	
}
