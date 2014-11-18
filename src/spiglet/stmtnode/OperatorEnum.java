package spiglet.stmtnode;

public enum OperatorEnum implements spgRoot {
	OP_LT, OP_PLUS, OP_MINUS, OP_TIMES;
	public String toString(){
		switch (this){
			case OP_LT: return "LT";
			case OP_PLUS: return "PLUS";
			case OP_MINUS: return "MINUS";
			case OP_TIMES: return "TIMES";
		}
		return null;
	}
}
