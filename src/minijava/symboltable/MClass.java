package minijava.symboltable;

import java.util.HashMap;

import minijava.typecheck.CompileError;

public class MClass extends MType {

	private final HashMap<String, MMethod> MethodTable = new HashMap<String, MMethod>();
	
	private final HashMap<String, MVar> VarTable = new HashMap<String, MVar>();
	
	private MIdentifier ParentClassID = null;
	
	private MClass ParentClassRef = null;
	
	public MClass() {
		super(TypeEnum.M_CLASS);
		// TODO Auto-generated constructor stub
		
	}

	public MClass(MClass _object) {
		super(_object);
		// TODO Auto-generated constructor stub
	}
	
	public void InsertMethod(MMethod _method) throws Exception{
		if (this.MethodTable.containsKey(_method.GetID().GetID())){
			CompileError.DupDefinitionError(_method, this.MethodTable.get(_method.GetID().GetID()));
		}
		this.MethodTable.put(_method.GetID().GetID(), _method);
	}
	
	public void InsertVar(MVar _variable) throws Exception{
		if (this.VarTable.containsKey(_variable.GetID().GetID())){
			CompileError.DupDefinitionError(_variable, this.MethodTable.get(_variable.GetID().GetID()));
		}
		this.VarTable.put(_variable.GetID().GetID(), _variable);
	}

	
	public MMethod GetMethod(MIdentifier _ID){
		String MethodName = _ID.GetID();
		MClass MasterClass = this;
		MMethod RetMethod = null;
		while (MasterClass != null){
			RetMethod = MasterClass.MethodTable.get(MethodName);
			if (RetMethod != null)
				return RetMethod;
			MasterClass = MasterClass.ParentClassRef;
		}
		CompileError.UndefinedError(new MMethod(_ID));
		return null;
	}
	
	public MVar GetVar(MIdentifier _ID){
		String VarName = _ID.GetID();
		MClass MasterClass = this;
		MVar RetVar = null;
		while (MasterClass != null){
			RetVar = MasterClass.VarTable.get(VarName);
			if (RetVar != null)
				return RetVar;
			MasterClass = MasterClass.ParentClassRef;
		}
		CompileError.UndefinedError(new MMethod(_ID));
		return null;
	}
	
	public void SetParent(MIdentifier _ParentID){
		this.ParentClassID = _ParentID;
	}
	
	public MClass GetParent(){
		return this.ParentClassRef;
	}
	
	public void BindParent(){
		// check for inheritance loop
		MClass RootClass = (MClass) MType.GetTypeByID(this.ParentClassID);
		this.ParentClassRef = RootClass;
		while (RootClass != null){
			RootClass = RootClass.ParentClassRef;
			if (RootClass == this){
				CompileError.ExtendLoopError(this);
				this.ParentClassRef = null;
			}
		}
	}
	
	
}
