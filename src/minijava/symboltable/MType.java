package minijava.symboltable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import minijava.typecheck.CompileError;

import minijava.pgtree.*;

public class MType {

	private MIdentifier Identifier;
	
	private final TypeEnum Type;
	
	public static MMethod MainMethod = null;
	
	public static MClass MainClass = null;
	
	public MType(TypeEnum _type){
		this.Type = _type;
	}
	
	public MType(TypeEnum _type, MIdentifier _ID){
		this.Identifier = _ID;
		this.Type = _type;
	}
	
	public MType(MType _object){
		this.Identifier = _object.Identifier;
		this.Type = _object.Type;
	}
	
	
	public TypeEnum GetType(){
		return this.Type;
	}
	
	public void SetID(MIdentifier _ID){
		this.Identifier = _ID;
	}
	
	public String toString(){
		return this.Type.toString();
	}
	
	public MIdentifier GetID(){
		return this.Identifier;
	}
	
	public static HashMap<String, MType> RootSymbolTable = new HashMap<String, MType>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		put("int", new MType(TypeEnum.M_INT, new MIdentifier("int", -1, -1)));
		put("boolean", new MType(TypeEnum.M_BOOLEAN, new MIdentifier("boolean", -1, -1)));
		put("int[]", new MType(TypeEnum.M_ARRAY, new MIdentifier("int[]", -1, -1)));
	}};

	public static MType GetTypeByID(MIdentifier _ID){
		if (!MType.RootSymbolTable.containsKey(_ID.GetID())){
			CompileError.UndefinedError(new MType(TypeEnum.M_CLASS, _ID));
			return null;
		}
		return MType.RootSymbolTable.get(_ID.GetID());
	}
	
	public static void InsertClass(MClass _NewClass) {
		if (MType.RootSymbolTable.containsKey(_NewClass.GetID().GetID())){
			try {
				CompileError.DupDefinitionError(_NewClass, MType.RootSymbolTable.get(_NewClass.GetID().GetID()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		// code for lab2: begin
		if (_NewClass != MType.MainClass)
			_NewClass.ClassSerialNo = MType.GlobalClassCnt++;
		// code for lab2: end
		MType.RootSymbolTable.put(_NewClass.GetID().GetID(), _NewClass);
	}
	
	public static void RootSymbolTableDump(){
		Collection<MType> Symbols = MType.RootSymbolTable.values();
		for (MType SymbolItem : Symbols){
			System.out.println( SymbolItem.SymbolContent());
		}
	}
	
	// format: address, id string, id lineno, actual type 
	public String SymbolContent(){
		return String.format("%x\t%s\t%d\t%s", this.hashCode(), this.Identifier.GetID(), this.Identifier.GetLineNo(), this.Type);
	}
	
	public void Bind(){
		// do nothing
	}
	
	public static void RootBind(){
		for (Map.Entry<String, MType> entry : MType.RootSymbolTable.entrySet()){
			entry.getValue().Bind();
		}
	}
	
	// used in parameter match check
	public boolean equals(MType _target){
		if (this.Type != _target.Type) 	return false;
		if (this.Type == TypeEnum.M_CLASS && !this.Identifier.GetID().equals(_target.Identifier.GetID())) return false;
		return true;
	}
	
	// if this can be assigned to _target
	public boolean isInstanceOf(MType _target){
		if (this.Type != _target.Type)	return false;
		if (this.Type == TypeEnum.M_CLASS){
			MClass SonClass = (MClass) MType.GetTypeByID(this.Identifier);
			MClass ParentClass = (MClass) MType.GetTypeByID(_target.Identifier);
			while (SonClass != null){
				if (SonClass == ParentClass) return true;
				SonClass = SonClass.GetParent();
			}
			return false;
		}
		return true;
	}
	
	/**************************************** code for lab2 ************************************************/
	private static int GlobalClassCnt = 0;
	
	public static pgTemp GlobalTableTemp;
	
	public static pgProcedure Get_Global_Init(){
		pgStmtList StmtList = new pgStmtList();
		// allocate for global class table
		MType.GlobalTableTemp = new pgTemp(); 	// the temp var that stores the address of the global class table
		
		StmtList.f0.add(new pgMoveStmt(
				MType.GlobalTableTemp,
				new pgHAllocate(new pgIntegerLiteral(MType.GlobalClassCnt * 4))
				));
		
		for (Map.Entry<String, MType> entry : MType.RootSymbolTable.entrySet()){
			if (!(entry.getValue() instanceof MClass)) 	continue;
			if (entry.getValue() == MType.MainClass) continue;
			pgStmtList _stmtlist = ((MClass) entry.getValue()).GenGBLInitCode();
			StmtList.f0.addAll(_stmtlist.f0);
		}
		
		return new pgProcedure(
				new pgLabel("_Global_Init"), 
				new pgIntegerLiteral(0),
				new pgStmtExp(
						StmtList,
						MType.GlobalTableTemp
						)
				);
	}
	
}
