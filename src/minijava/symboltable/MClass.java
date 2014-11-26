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
		if (this.ParentClassRef != null) 
			this.ParentClassRef.BindMethod();
		
		// lab2 code : begin
		if (this.ParentClassRef != null)
			this.MethodCnt = this.ParentClassRef.MethodCnt;
		// not set for else, because it has already been set to 1
		
		for (Map.Entry<String, MMethod> entry : this.MethodTable.entrySet()){
			entry.getValue().MethodSerialNo = this.MethodCnt++;
		}
		// lab2 code : end
		
		if (this.ParentClassRef == null) 	return;
		// resolve for parent class methods
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
		if (this.ParentClassRef != null)	
			this.ParentClassRef.BindVar();
		
		// lab2 code : begin
		if (this.ParentClassRef != null)
			this.VarCnt = this.ParentClassRef.VarCnt;
		else
			this.VarCnt = 1; 		// set 1 because no. 0 is for dtable link
		for (Map.Entry<String, MVar> entry : this.VarTable.entrySet()){
			entry.getValue().VarSerialNo = this.VarCnt++;				// set var serial number here, because needs to tackle class inheritance
			entry.getValue().isClassMember = true;
		}
		// lab2 code : end
		
		// resolve for parent class variable
		if (this.ParentClassRef == null)	return;
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
		
		// code for lab2 : set constructor label
		this.ConstructorLabel = new pgLabel("_" + this.GetID().GetID());
	}
	
	/********************************** code for lab2 **************************************************/
	
	// the serial number of the class, used in global class table
	public int ClassSerialNo;
	
	// number of Methods, class constructor included
	public int MethodCnt = 1;
	
	// number of member variables, parent class member included, set for 1 because 0 is for dtable link
	public int VarCnt = 1;
	
	// name of the constuctor method, will be set in name bind 
	public pgLabel ConstructorLabel;
	
	// Dtable init code
	public pgTemp GenMethodTable(pgStmtList ConstructorList){
		pgStmtList _ret = new pgStmtList();
		pgTemp DtableBase = new pgTemp();
		boolean DtableFlag [] = new boolean [this.MethodCnt];
		
		// allocate space for Dtable 
		_ret.f0.add(new pgMoveStmt(
				DtableBase,
				new pgHAllocate(new pgIntegerLiteral(this.MethodCnt * 4))
				));
		
		// Store the Address of Methods into Dtable
		// constructor method
		pgTemp LabelTmp = new pgTemp();
		
		_ret.f0.add(new pgMoveStmt(
				LabelTmp,
				this.ConstructorLabel
				));
		
		_ret.f0.add(new pgHStoreStmt(
				DtableBase,
				new pgIntegerLiteral(0),
				LabelTmp
				));
		
		// normal member methods
		for (Map.Entry<String, MMethod> entry : this.MethodTable.entrySet()){
			
			_ret.f0.add(new pgMoveStmt(
					LabelTmp,
					new pgLabel(entry.getValue().PgName)
					));
			
			_ret.f0.add(new pgHStoreStmt(
					DtableBase,
					new pgIntegerLiteral(entry.getValue().MethodSerialNo * 4),
					LabelTmp
					));
			
			DtableFlag[entry.getValue().MethodSerialNo] = true;
		}
		
		// fill in the null hole
		for (int i = 0; i < this.MethodCnt; i++){
			if (DtableFlag[i] == false){
				MClass AncestorRef = this.ParentClassRef;
				while (AncestorRef != null){
					boolean isfound = false;
					for (Map.Entry<String, MMethod> entry : AncestorRef.MethodTable.entrySet()){
						if (entry.getValue().MethodSerialNo == i){
							isfound = true;
							_ret.f0.add(new pgMoveStmt(
									LabelTmp,
									new pgLabel(this.GetMethod(entry.getValue().GetID()).PgName)
									));
							_ret.f0.add(new pgHStoreStmt(
									DtableBase,
									new pgIntegerLiteral(i * 4),
									LabelTmp
									));
							break;
						}
					}
					if (isfound == true) 	break;
					AncestorRef = AncestorRef.ParentClassRef;
				}
			}
		}
		/**
		// then Store Dtable Base into global class table
		_ret.f0.add(new pgHStoreStmt(
				MType.GlobalTableTemp,
				new pgIntegerLiteral(this.ClassSerialNo * 4),
				DtableBase
				));
				*/
		ConstructorList.f0.addAll(_ret.f0);
		return DtableBase;
	}
	
	/**
	 * @return pgProcedure code for Vtable Allocation
	 * 
	 */
	public pgProcedure GenConstructorCode(){
		pgTemp VtableTemp = new pgTemp();
		pgTemp DtableTemp = null;
		pgStmtList _list = new pgStmtList();
		// allocate space for Vtable
		_list.f0.add(new pgMoveStmt(
				VtableTemp,
				new pgHAllocate(new pgIntegerLiteral(this.VarCnt * 4))
				));
		
		/**
		// get link to Dtable
		_list.f0.add(new pgHLoadStmt(
				DtableTemp,
				MType.GlobalTableTemp,
				new pgIntegerLiteral(this.ClassSerialNo * 4)
				));
		*/
		DtableTemp = this.GenMethodTable(_list);
		// store link to Dtable
		_list.f0.add(new pgHStoreStmt(
				VtableTemp,
				new pgIntegerLiteral(0),
				DtableTemp
				));
		
		// member variable init
		pgTemp ZeroTmp = new pgTemp();
		_list.f0.add(new pgMoveStmt(
				ZeroTmp,
				new pgIntegerLiteral(0)
				));
		for (Map.Entry<String, MVar> entry : this.VarTable.entrySet()){
			_list.f0.add(new pgHStoreStmt(
					VtableTemp,
					new pgIntegerLiteral(entry.getValue().VarSerialNo * 4),
					ZeroTmp
					));
		}
		
		return new pgProcedure(
				this.ConstructorLabel,
				new pgIntegerLiteral(0),
				new pgStmtExp(
						_list,
						VtableTemp
						)
				);
	}
}