package spiglet.stmtnode;

import java.util.Vector;

import spiglet.visitor.VisitorParameter;

public class Container implements VisitorParameter {

	public final Vector<spgTempRef> arg = new Vector<spgTempRef>();
}
