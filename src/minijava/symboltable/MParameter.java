package minijava.symboltable;

import java.util.Vector;

import minijava.pgtree.pgCall;
import minijava.pgtree.pgTemp;

public class MParameter extends MType {

	public MMethod _context;
	
	
	
	public Vector<MType> ParaList = new Vector<MType>();
	
	public MParameter() {
		super(TypeEnum.M_BASIC, null);
	}

	/********************************** lab2 code ************************************/
	
	// pass down
	public pgCall CallNode = null;
	
	public pgTemp ParaArray = null;
	
	public int ParaOverFlowCnt = 0;
}
