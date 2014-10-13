package minijava.symboltable;

public class MVar extends MType {

	private MIdentifier VarTypeID;
	
	private MType VarTypeRef;
	
	public MVar() {
		super(TypeEnum.M_VARIABLE);
		// TODO Auto-generated constructor stub
	}

	public MVar(MType _object) {
		super(_object);
		// TODO Auto-generated constructor stub
	}
	
	public void SetVarTypeID(MIdentifier _ID){
		this.VarTypeID = _ID;
	}
	
	public void BindType(){
		VarTypeRef = MType.GetTypeByID(this.VarTypeID);
	}

}
