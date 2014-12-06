package spiglet.kgtree;

import spiglet.flowgraph.RegisterRef;

public class kgReg implements kgSimpleExp {

	public RegisterRef f0 = null;
	
	public kgReg(){
		
	}
	
	public kgReg(RegisterRef _f0){
		this.f0 = _f0;
		RegisterRef.CallerSaveFlag[_f0.GetIndex()] = true;
	}
	
	@Override
	public void PrintInstruction() {
		System.out.print(this.f0.toString());
	}

}
