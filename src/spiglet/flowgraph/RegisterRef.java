package spiglet.flowgraph;

public class RegisterRef {
	private final String RegName;
	
	private int index;
	
	public RegisterRef(String _regname){
		this.RegName = _regname;
	}
	
	public String toString(){
		return this.RegName;
	}
	
	public int GetIndex(){
		return this.index;
	}
	
	public final static RegisterRef ARegs [] = new RegisterRef [4];
	public final static RegisterRef TRegs [] = new RegisterRef [10];
	public final static RegisterRef SRegs [] = new RegisterRef [8];
	public final static RegisterRef VRegs [] = new RegisterRef [2];
	
	// used for caller save check
	public final static boolean CallerSaveFlag [] = new boolean [10];
	
	public static void init(){
		for (int i = 0; i < 4; i++){
			RegisterRef.ARegs[i] = new RegisterRef("a" + i);
			RegisterRef.ARegs[i].index = i;
		}
		for (int i = 0; i < 10; i++){
			RegisterRef.TRegs[i] = new RegisterRef("t" + i);
			RegisterRef.TRegs[i].index = i;
		}
		for (int i = 0; i < 8; i++){
			RegisterRef.SRegs[i] = new RegisterRef("s" + i);
			RegisterRef.SRegs[i].index = i;
		}
		for (int i = 0; i < 2; i++){
			RegisterRef.VRegs[i] = new RegisterRef("v" + i);
			RegisterRef.VRegs[i].index = i;
		}
	}
	
	public static void ClearCallerSave(){
		for (boolean flag : RegisterRef.CallerSaveFlag)
			flag = false;
	}
}
