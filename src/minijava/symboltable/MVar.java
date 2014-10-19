package minijava.symboltable;

public class MVar extends MType {

	private MIdentifier VarTypeID;
	
	private MType VarTypeRef = null;
	

	// expression constructor
	public MVar() {
		super(TypeEnum.M_VARIABLE);
	}

	// variable constructor 
	public MVar(MIdentifier _ID){
		super(TypeEnum.M_VARIABLE, _ID);
	}
	
	public MVar(MType _object) {
		super(_object);
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
	
	/***************************************** code for lab2 ****************************************************/
	
	// serial number for class member variable, set in MClass.BindVar()
	public int VarSerialNo = -1;
}
