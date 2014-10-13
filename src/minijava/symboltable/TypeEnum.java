package minijava.symboltable;

public enum TypeEnum {
	M_INT, M_BOOLEAN, M_ARRAY, M_CLASS, M_METHOD, M_VARIABLE;
	public String toString(){
		switch(this){
		case M_INT: return "int";
		case M_BOOLEAN: return "boolean";
		case M_ARRAY: return "int[]";
		case M_CLASS: return "class";
		case M_METHOD: return "method";
		case M_VARIABLE: return "variable";
		}
		return null;
	}
}
