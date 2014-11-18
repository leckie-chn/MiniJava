package spiglet.flowgraph;

public class RegisterRef {
	private final String RegName;
	
	public RegisterRef(String _regname){
		this.RegName = _regname;
	}
	
	public String toString(){
		return this.RegName;
	}
	
	public final static RegisterRef ARegs [] = new RegisterRef [4];
	public final static RegisterRef TRegs [] = new RegisterRef [10];
	public final static RegisterRef SRegs [] = new RegisterRef [8];
	public final static RegisterRef VRegs [] = new RegisterRef [2];
	
	public static void init(){
		for (int i = 0; i < 4; i++)
			RegisterRef.ARegs[i] = new RegisterRef("a" + i);
		for (int i = 0; i < 10; i++)
			RegisterRef.TRegs[i] = new RegisterRef("t" + i);
		for (int i = 0; i < 8; i++)
			RegisterRef.SRegs[i] = new RegisterRef("s" + i);
		for (int i = 0; i < 2; i++)
			RegisterRef.VRegs[i] = new RegisterRef("v" + i);
	}
}
