package minijava.symboltable;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import minijava.typecheck.CompileError;

public class MMethod extends MType {

	private MIdentifier RetTypeID = null;
	
	private MType RetTypeRef = null;
	
	private final MClass MasterClass;
	
	public Vector<MVar> ParaTypeList = new Vector<MVar>();
	
	private HashMap<String, MVar> VarTable = new HashMap<String, MVar>();
	
	public MMethod(MClass _master) {
		super(TypeEnum.M_METHOD);
		this.MasterClass = _master;
		// TODO Auto-generated constructor stub
	}

	public MMethod(MIdentifier _ID, MClass _master) {
		super(TypeEnum.M_METHOD, _ID);
		this.MasterClass = _master;
	}
	public MMethod(MMethod _object) {
		super(_object);
		this.MasterClass = _object.MasterClass;
		// TODO Auto-generated constructor stub
	}
	
	public void SetRetType(MIdentifier _ID){
		this.RetTypeID = _ID;
	}
	
	public MType GetRetType(){
		return this.RetTypeRef;
	}
	
	private void BindRetType(){
		if (this.RetTypeRef == null)
			this.RetTypeRef = MType.GetTypeByID(this.RetTypeID);
	}

	public void AddParaItem(MVar _parameter){
		this.ParaTypeList.add(_parameter);
		this.InsertVar(_parameter);
	}
	
	public void InsertVar(MVar _variable) {
		if (this.VarTable.containsKey(_variable.GetID().GetID())){
			try {
				CompileError.DupDefinitionError(_variable, this.VarTable.get(_variable.GetID().GetID()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		this.VarTable.put(_variable.GetID().GetID(), _variable);
	}
	
    public MVar GetVar(MIdentifier _ID){
    	if (this.VarTable.containsKey(_ID.GetID())){
    		return this.VarTable.get(_ID.GetID());
    	} 
    	else 
    		return this.MasterClass.GetVar(_ID);
    }
    
	// equals in return type and parameter list
	public boolean equals(MMethod target){
		if (this.RetTypeRef != target.RetTypeRef) return false;
		if (this.ParaTypeList.size() != target.ParaTypeList.size()) return false;
		for (int i = 0; i < this.ParaTypeList.size(); i++){
			if (this.ParaTypeList.get(i).GetVarType() != target.ParaTypeList.get(i).GetVarType()) return false;
		}
		return true;
	}
	
	public void Bind(){
		this.BindRetType();
		for (MVar param : this.ParaTypeList){
			param.Bind();
		}
		
		for (Map.Entry<String, MVar> entry : this.VarTable.entrySet()){
			entry.getValue().Bind();
		}
	}
	
	// basic, return type, \n paralist \n varlist
	public String SymbolContent(){
		String Content = String.format("\t%s\t%s@%d\n", super.SymbolContent(), this.RetTypeRef.GetID().GetID(), this.RetTypeRef.hashCode());
		Content += "\tParameter List:";
		for (MVar param : this.ParaTypeList){
			Content += String.format("\t\t%s", param.SymbolContent());
		}
		Content += "\tTemporary Variable List:";
		for (Map.Entry<String, MVar> varentry : this.VarTable.entrySet()){
			Content += String.format("\t\t%s", varentry.getValue().SymbolContent());
		}
		return Content;
	}
	
}
