package minijava.symboltable;

import java.util.Vector;

import minijava.pgtree.pgCall;

public class MParameter extends MType {

	public MMethod _context;
	
	
	
	public Vector<MType> ParaList = new Vector<MType>();
	
	public MParameter() {
		super(TypeEnum.M_BASIC, null);
	}

	/********************************** lab2 code ************************************/
	
	// pass down
	public pgCall CallNode = null;
	
}
