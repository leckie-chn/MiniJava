package minijava.symboltable;

import java.util.Collection;
import java.util.HashMap;

import minijava.typecheck.CompileError;


public class MType {

	private MIdentifier Identifier;
	
	private final TypeEnum Type;
	
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
	
	public MIdentifier GetID(){
		return this.Identifier;
	}
	
	private static HashMap<String, MType> RootSymbolTable = new HashMap<String, MType>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		put("int", new MType(TypeEnum.M_INT));
		put("boolean", new MType(TypeEnum.M_BOOLEAN));
		put("int[]", new MType(TypeEnum.M_ARRAY));
	}};

	public static MType GetTypeByID(MIdentifier _ID){
		if (!MType.RootSymbolTable.containsKey(_ID.GetID())){
			CompileError.UndefinedError(new MType(TypeEnum.M_CLASS, _ID));			
		}
		return MType.RootSymbolTable.get(_ID.GetID());
	}
	
	public static void InsertClass(MClass _NewClass) throws Exception{
		if (MType.RootSymbolTable.containsKey(_NewClass.GetID().GetID())){
			minijava.typecheck.CompileError.DupDefinitionError(_NewClass, MType.RootSymbolTable.get(_NewClass.GetID().GetID()));
		}
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
		return String.format("%d\t%s\t%d\t%s", this.hashCode(), this.Identifier.GetID(), this.Identifier.GetLineNo(), this.Type);
	}
}
