package minijava.symboltable;

public class MMethod extends MType {

	public MMethod() {
		super(TypeEnum.M_METHOD);
		// TODO Auto-generated constructor stub
	}

	public MMethod(MIdentifier _ID) {
		super(TypeEnum.M_METHOD, _ID);
	}
	public MMethod(MType _object) {
		super(_object);
		// TODO Auto-generated constructor stub
	}

}
