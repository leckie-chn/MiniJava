package minijava.symboltable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import minijava.typecheck.CompileError;

import minijava.pgtree.*;

public class MClass extends MType {

	private final HashMap<String, MMethod> MethodTable = new HashMap<String, MMethod>();
	
	private final HashMap<String, MVar> VarTable = new HashMap<String, MVar>();
	
	private MIdentifier ParentClassID = null;
	
	private MClass ParentClassRef = null;
	
	private boolean MethodBinded = false;
	
	private boolean VarBinded = false;
	
	public MClass() {
		super(TypeEnum.M_CLASS);
		
	}

	public MClass(MIdentifier _ID){
		super(TypeEnum.M_CLASS, _ID);
	}
	public MClass(MClass _object) {
		super(_object);
	}
	
	public String toString(){
		return this.GetID().GetID();
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
		this.MethodBinded = true;
		if (this.ParentClassRef == null) return;
		this.ParentClassRef.BindMethod();
		for (Map.Entry<String, MMethod> entry : this.ParentClassRef.MethodTable.entrySet()){
			if (this.MethodTable.containsKey(entry.getKey())){
				// override
				if (!entry.getValue().equals(this.MethodTable.get(entry.getKey()))){
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
		
	}
	
	// bind variable of this and parent variables
	
	private void BindVar(){
		if (this.VarBinded) return;
		this.VarBinded = true;
		if (this.ParentClassRef == null)	return;
		this.ParentClassRef.BindVar();
		
		// lab2 code : begin
		if (this.ParentClassRef != null)
			this.VarCnt = this.ParentClassRef.VarCnt;	
		for (Map.Entry<String, MVar> entry : this.VarTable.entrySet()){
			entry.getValue().VarSerialNo = this.VarCnt++;				// set var serial number here, because needs to tackle class inheritance
		}
		// lab2 code : end
		
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
		if (this.ParentClassID == null) return;
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
		String ContentStr;
		if (this.ParentClassRef != null)
			ContentStr = String.format("%s\t%s@%x\n", super.SymbolContent(), this.ParentClassID.GetID(), this.ParentClassRef.hashCode());
		else
			ContentStr = String.format("%s\tNo Parent Class\n", super.SymbolContent());
		
		if (this.MethodTable.isEmpty()){
			ContentStr += "No Member Method\n";
		}else{
			ContentStr += "Member Method List:\n";
			Collection<MMethod> Methods = this.MethodTable.values();
			for (MMethod method : Methods){
				ContentStr += method.SymbolContent();
			}
		}
		
		if (this.VarTable.isEmpty()){
			ContentStr += "No Member Variable\n";
		}else{
			ContentStr += "Member Variable List:\n";
			Collection<MVar> Variables = this.VarTable.values();
			for (MVar variable : Variables){
				ContentStr += variable.SymbolContent();
			}
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
	
	/********************************** code for lab2 **************************************************/
	
	// the serial number of the class, used in global class table
	public int ClassSerialNo;
	
	// number of Methods, class constructor included
	public int MethodCnt = 1;
	
	// number of member variables, parent class member included
	public int VarCnt = 0;
	
	// Dtable init code
	public pgStmtList GenGBLInitCode(){
		pgStmtList _ret = new pgStmtList();
		pgTemp DtableBase = new pgTemp();
		
		// allocate space for Dtable 
		_ret.f0.add(new pgMoveStmt(
				DtableBase,
				new pgHAllocate(new pgIntegerLiteral(this.MethodCnt * 4))
				));
		
		// Store the Address of Methods into Dtable
		// constructor method
		_ret.f0.add(new pgHStoreStmt(
				DtableBase,
				new pgIntegerLiteral(0),
				new pgLabel("_" + this.GetID().GetID())
				));
		
		// normal member methods
		for (Map.Entry<String, MMethod> entry : this.MethodTable.entrySet()){
			_ret.f0.add(new pgHStoreStmt(
					DtableBase,
					new pgIntegerLiteral(entry.getValue().MethodSerialNo),
					new pgLabel(entry.getValue().PgName)
					));
		}
		
		// then Store Dtable Base into global class table
		_ret.f0.add(new pgHStoreStmt(
				MType.GlobalTableTemp,
				new pgIntegerLiteral(this.ClassSerialNo),
				DtableBase
				));
		return _ret;
	}
	
}