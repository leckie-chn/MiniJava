package spiglet.kgtree;

import spiglet.stmtnode.OperatorEnum;

public class kgBinOp implements kgExp {

	public OperatorEnum f0;
	
	public kgReg f1;
	
	public kgSimpleExp f2;
	
	public kgBinOp(){
		
	}
	
	public kgBinOp(OperatorEnum _f0, kgReg _f1, kgSimpleExp _f2){
		this.f0 = _f0;
		this.f1 = _f1;
		this.f2 = _f2;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print(this.f0.toString() + " ");
		this.f1.PrintInstruction();
		System.out.print(' ');
		this.f2.PrintInstruction();
	}

}
