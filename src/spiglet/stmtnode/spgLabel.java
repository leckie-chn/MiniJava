package spiglet.stmtnode;

import spiglet.kgtree.kgLabel;
import spiglet.kgtree.kgProcedure;

public class spgLabel extends spgStmtNode implements spgSimpleExp {
	public final String arg;
	
	public spgLabel(String _arg){
		super(null, null, null, null);
		this.arg = _arg;
	}
	
	@Override
	public void DoTranslation(kgProcedure context){ 
		context.f4.f0.add(new kgLabel(
				context.f0.f0 + "_" + this.arg
				));
	}
}
