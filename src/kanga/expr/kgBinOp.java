package kanga.expr;

import minijava.pgtree.OperatorEnum;

public class kgBinOp implements kgExp {
	private OperatorEnum a0;
	
	private kgReg a1;
	
	private kgSimpleExp a2;
	
	public kgBinOp(OperatorEnum _a0, kgReg _a1, kgSimpleExp _a2){
		this.a0 = _a0;
		this.a1 = _a1;
		this.a2 = _a2;
	}
	
	public OperatorEnum GetOp(){
		return this.a0;
	}
	
	public kgReg GetSrc1(){
		return this.a1;
	}
	
	public kgSimpleExp GetSrc2(){
		return this.a2;
	}
	
	
}
