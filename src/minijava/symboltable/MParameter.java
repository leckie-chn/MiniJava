package minijava.symboltable;

import java.util.Vector;

public class MParameter extends MType {

	public MMethod _context;
	
	public Vector<MType> ParaList = new Vector<MType>();
	
	public MParameter() {
		super(TypeEnum.M_BASIC, null);
	}

}
