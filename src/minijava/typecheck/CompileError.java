package minijava.typecheck;


import java.util.Vector;

import minijava.symboltable.*;


public class CompileError {
	private static Vector<String> Info = new Vector<String>();
	
	/**
	 * @param e1, redefined
	 * @param e2, already
	 * @throws Exception
	 */
	public static void DupDefinitionError (MType e1, MType e2) throws Exception {
		if (e1.GetType() != e2.GetType()){
			throw new Exception("the actual type of the duplicate error should be the same");
		}
		
		String errorstr = String.format("duplicate %s definition error: already defined in %d", e1.GetType().toString(), e2.GetID().GetLineNo());
		
		Info.add(String.format("At Line %d: %s", e1.GetID().GetLineNo(), errorstr));
	}
	
	public static void UndefinedError(MType e){
		String errorstr = String.format("undefined %s error", e.GetType().toString());
		Info.add(String.format("At Line %d: %s", e.GetID().GetLineNo(), errorstr));
	}
	
	public static void ExtendLoopError(MClass e){
		MClass TmpClass = e;
		String errorstr = "Class Inheritance loop error: ";
		while (true){
			errorstr = errorstr + TmpClass.GetID().GetID() + " => ";
			TmpClass = TmpClass.GetParent();
			if (TmpClass == e) break;
		}
		
		Info.add(String.format("At Line %d: %s", e.GetID().GetLineNo(), errorstr));
	}
	

}
