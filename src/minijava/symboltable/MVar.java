package minijava.symboltable;

public class MVar extends MType {

	private MIdentifier VarTypeID;
	
	private MType VarTypeRef = null;
	

	
	public MVar() {
		super(TypeEnum.M_VARIABLE);
		// TODO Auto-generated constructor stub
	}

	public MVar(MIdentifier _ID){
		super(TypeEnum.M_VARIABLE, _ID);
	}
	
	public MVar(MType _object) {
		super(_object);
		// TODO Auto-generated constructor stub
	}
	
	public void SetVarType(MIdentifier _ID){
		this.VarTypeID = _ID;
	}
	
	private void BindType(){
		if (VarTypeRef != null) return; // binded
		VarTypeRef = MType.GetTypeByID(this.VarTypeID);
	}
	
	public MType GetVarType(){
		return this.VarTypeRef;
	}

	public void Bind(){
		this.BindType();
	}
	
	public String SymbolContent(){
		return String.format("\t\t%s\t%s@%x\n", super.SymbolContent(), this.VarTypeRef.GetID().GetID(), this.VarTypeRef.hashCode());
	}
}
