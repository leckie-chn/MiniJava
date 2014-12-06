package spiglet.flowgraph;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import spiglet.stmtnode.spgTempRef;

public class InterfereGraphNode {
	
	public final Vector<spgTempRef> TempNodeVec = new Vector<spgTempRef>();
	
	public final Set<InterfereGraphNode> Neighbors = new HashSet<InterfereGraphNode>();
	
	public InterfereGraphNode(){
		
	}
	
	public InterfereGraphNode(spgTempRef _temp){
		this.TempNodeVec.add(_temp);
		_temp.attachedNode = this;
	}
	
	public static InterfereGraphNode Coalescing(InterfereGraphNode n1, InterfereGraphNode n2){
		return null;
		/*
		if (n1 == n2) return null;
		InterfereGraphNode _ret = new InterfereGraphNode();
		_ret.TempNodeVec.addAll(n1.TempNodeVec);
		_ret.TempNodeVec.addAll(n2.TempNodeVec);
		_ret.Neighbors.addAll(n1.Neighbors);
		_ret.Neighbors.addAll(n2.Neighbors);
		if (_ret.Neighbors.size() < InterfereGraph.MAX_COLOR){
			for (spgTempRef temp : _ret.TempNodeVec)
				temp.attachedNode = _ret;
			return _ret;
		}
		return null; // return null if not coalescable
		*/
	}
	
	public boolean Deleted = false;
	
	public int color = -1; // -1 means spilled
	
	public void BindReg(Set<RegisterRef> calleesave){
		for (spgTempRef temp : this.TempNodeVec){
			if (color < 0){
				temp.register = null;
			}
			else{ 
				if (color < 10){
					temp.register = RegisterRef.TRegs[color];
				}
				else {
					temp.register = RegisterRef.SRegs[color - 10];
					calleesave.add(temp.register);
				}
			}
		}
	}
	
	public void SpillStack(FlowGraph graph){
		for (spgTempRef temp : this.TempNodeVec)
			if (temp.register == null){
				temp.StackPos = graph.StackPosCnt++;
			} else 
				temp.StackPos = -1;
	}
}
