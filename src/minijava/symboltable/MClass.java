package minijava.symboltable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import minijava.typecheck.CompileError;

public class MClass extends MType {

	private final HashMap<String, MMethod> MethodTable = new HashMap<String, MMethod>();
	
	private final HashMap<String, MVar> VarTable = new HashMap<String, MVar>();
	
	private MIdentifier ParentClassID = null;
	
	private MClass ParentClassRef = null;
	
	private boolean MethodBinded = false;
	
	private boolean VarBinded = false;
	
	public MClass() {
		super(TypeEnum.M_CLASS);
		// TODO Auto-generated constructor stub
		
	}

	public MClass(MIdentifier _ID){
		super(TypeEnum.M_CLASS, _ID);
	}
	public MClass(MClass _object) {
		super(_object);
		// TODO Auto-generated constructor stub
	}
	
	public void InsertMethod(MMethod _method) {
		if (this.MethodTable.containsKey(_method.GetID().GetID())){
			try {
				CompileError.DupDefinitionError(_method, this.MethodTable.get(_method.GetID().GetID()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			return;
		}
		this.MethodTable.put(_method.GetID().GetID(), _method);
	}
	
	public void InsertVar(MVar _variable) {
		if (this.VarTable.containsKey(_variable.GetID().GetID())){
			try {
				CompileError.DupDefinitionError(_variable, this.MethodTable.get(_variable.GetID().GetID()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			return;
		}
		this.VarTable.put(_variable.GetID().GetID(), _variable);
	}
	
	// bind method of this and parent class methods
	// check for incorrect override
	private void BindMethod(){
		if (this.MethodBinded) return;
		if (this.ParentClassRef != null){
			this.ParentClassRef.BindMethod();
		}
		for (Map.Entry<String, MMethod> entry : this.ParentClassRef.MethodTable.entrySet()){
			if (this.MethodTable.containsKey(entry.getKey())){
				// override
				if (entry.getValue().equals(this.MethodTable.get(entry.getKey()))){
					try {
						CompileError.DupDefinitionError(this.MethodTable.get(entry.getKey()), entry.getValue());
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
			}
			else {
				// inheritance
				this.MethodTable.put(entry.getKey(), entry.getValue());
			}
		}
		this.MethodBinded = true;
	}
	
	// bind variable of this and parent variables
	
	private void BindVar(){
		if (this.VarBinded) return;
		if (this.ParentClassRef != null){
			this.ParentClassRef.BindVar();
		}
		for (Map.Entry<String, MVar> entry : this.ParentClassRef.VarTable.entrySet()){
			if (this.VarTable.containsKey(entry.getKey())){
				// override
				if (entry.getValue().GetVarType() != this.VarTable.get(entry.getKey()).GetVarType()){
					try {
						CompileError.DupDefinitionError(this.VarTable.get(entry.getKey()), entry.getValue());
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
			}
			else {
				this.VarTable.put(entry.getKey(), entry.getValue());
			}
		}
		this.VarBinded = true;
	}
	
	public MMethod GetMethod(MIdentifier _ID){
		if (!this.MethodTable.containsKey(_ID.GetID())){
			CompileError.UndefinedError(new MMethod(_ID, null));
			return null;
		}
		
		return this.MethodTable.get(_ID.GetID());
	}
	
	public MVar GetVar(MIdentifier _ID){
		if (!this.VarTable.containsKey(_ID.GetID())){
			CompileError.UndefinedError(new MVar(_ID));
			return null;
		}
		return this.VarTable.get(_ID.GetID());
	}
	
	public void SetParent(MIdentifier _ParentID){
		this.ParentClassID = _ParentID;
	}
	
	public MClass GetParent(){
		return this.ParentClassRef;
	}
	
	/**
	 * @round 1.5, bind stage
	 */
	private void BindParent(){
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
	
	// basic, parent class name, parent hashcode
	public String SymbolContent(){
		String ContentStr = String.format("%s\t%s@%d\n", super.SymbolContent(), this.ParentClassID.GetID(), this.ParentClassRef.hashCode());
		Collection<MMethod> Methods = this.MethodTable.values();
		for (MMethod method : Methods){
			ContentStr += method.SymbolContent();
		}
		Collection<MVar> Variables = this.VarTable.values();
		for (MVar variable : Variables){
			ContentStr += variable.SymbolContent();
		}
		return ContentStr;
	}
	
	public void Bind(){
		this.BindParent();
		for (Map.Entry<String, MMethod> entry : this.MethodTable.entrySet()){
			entry.getValue().Bind();
		}
		for (Map.Entry<String, MVar> entry : this.VarTable.entrySet()){
			entry.getValue().Bind();
		}
		this.BindMethod();
		this.BindVar();
	}
}
