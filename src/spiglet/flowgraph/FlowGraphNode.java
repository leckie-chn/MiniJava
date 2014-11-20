package spiglet.flowgraph;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import spiglet.stmtnode.spgCall;
import spiglet.stmtnode.spgLabel;
import spiglet.stmtnode.spgTempRef;

public class FlowGraphNode {

	public final Vector<ProgramStatus> FlowVec = new Vector<ProgramStatus>();
	
	public final spgLabel LeadingLabel;
	
	public boolean isExitNode = false;
	
	public final Vector<FlowGraphNode> successor = new Vector<FlowGraphNode>();
	
	public final Vector<FlowGraphNode> predecessor = new Vector<FlowGraphNode>();
	
	private Set<spgTempRef> InAliveSet;
	
	private Set<spgTempRef> OutALiveSet;
	
	private spgTempRef RetTemp = null;
	
	public FlowGraphNode(spgLabel _label){
		this.LeadingLabel = _label;
	}
	
	public void SetRetTemp(spgTempRef _ret){
		if (this.isExitNode)
			this.RetTemp = _ret;
	}
	
	public Set<spgTempRef> GetInAlive(){
		return this.InAliveSet;
	}
	
	public boolean LiveAnalysis(){
		if (this.isExitNode == true) {
			this.InAliveSet = new HashSet<spgTempRef>();
			this.InAliveSet.add(this.RetTemp);
			return true;
		}
		
		int NodeSize = this.FlowVec.size() - 1;
		this.OutALiveSet = this.FlowVec.lastElement().AliveSet;
		
		for (FlowGraphNode next : this.successor){
			this.OutALiveSet.addAll(next.GetInAlive());
		}
		
		for (int i = NodeSize - 1; i >= 0; i--){
			ProgramStatus current = this.FlowVec.elementAt(i);
			ProgramStatus next = this.FlowVec.elementAt(i + 1);
			current.AliveSet.addAll(next.AliveSet);
			if (current.Statement.TargetOperand != null){
				current.AliveSet.remove(current.Statement.TargetOperand);
			}
			if (current.Statement.SrcOperand1 != null && current.Statement.SrcOperand1 instanceof spgTempRef){
				current.AliveSet.add((spgTempRef)current.Statement.SrcOperand1);
			}
			if (current.Statement.SrcOperand2 != null && current.Statement.SrcOperand2 instanceof spgTempRef){
				current.AliveSet.add((spgTempRef)current.Statement.SrcOperand2);
			}
			if (current.Statement.SrcOperand3 != null && current.Statement.SrcOperand3 instanceof spgTempRef){
				current.AliveSet.add((spgTempRef)current.Statement.SrcOperand3);
			}
			if (current.Statement instanceof spgCall){
				for (spgTempRef _para : ((spgCall)current.Statement).ParaTable ){
					current.AliveSet.add(_para);
				}
			}
		}
		
		boolean isStable = this.InAliveSet != null && this.InAliveSet.equals(this.FlowVec.elementAt(0).AliveSet);
		this.InAliveSet = this.FlowVec.elementAt(0).AliveSet;
		
		return isStable;
	}
}
