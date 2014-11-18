package spiglet.flowgraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;




import spiglet.stmtnode.spgJump;
import spiglet.stmtnode.spgJumpAble;
import spiglet.stmtnode.spgLabel;
import spiglet.stmtnode.spgStmtNode;
import spiglet.stmtnode.spgTempRef;
import spiglet.visitor.VisitorParameter;

public class FlowGraph implements VisitorParameter{
	
	public FlowGraphNode EntryNode;
	
	public FlowGraphNode ExitNode;
	
	public final spgLabel ProcedureName;
	
	public final int ParaNum;
	
	public final Map<spgLabel, FlowGraphNode> BLMapping = new HashMap<spgLabel, FlowGraphNode>();
	
	private final Vector<spgStmtNode> RawStmtVec = new Vector<spgStmtNode>();
	
	private final Vector<FlowGraphNode> NodeVec = new Vector<FlowGraphNode>();
	
	public final InterfereGraph ITFGraph = new InterfereGraph();
	
	private spgTempRef RetTemp;
	
	public FlowGraph(spgLabel _label, int _num){
		this.ProcedureName = _label;
		this.ParaNum = _num;
	}
	
	public void AddStmtNode(spgStmtNode _stmt){
		this.RawStmtVec.add(_stmt);
	}
	
	public void SetRetTemp(spgTempRef _ret){
		this.RetTemp = _ret;
	}
	
	public void Init(){
		// flow graph & basic block construction
		FlowGraphNode CurrentNode = null;
		
		// block division
		for (spgStmtNode _stmt : this.RawStmtVec){
			if (_stmt instanceof spgLabel){
				CurrentNode = new FlowGraphNode((spgLabel)_stmt);
				this.NodeVec.add(CurrentNode);
				this.BLMapping.put((spgLabel)_stmt, CurrentNode);
			} else {
				if (CurrentNode == null)
					CurrentNode = new FlowGraphNode(null);
				CurrentNode.FlowVec.add(new ProgramStatus(_stmt));
				if (_stmt instanceof spgJumpAble)
					CurrentNode = null;
			}
		}
		
		// flow connection
		for (FlowGraphNode block : this.NodeVec){
			spgStmtNode laststmt = block.FlowVec.lastElement().Statement;
			if (laststmt instanceof spgJumpAble){
				FlowGraphNode targblock = this.BLMapping.get(((spgJumpAble) laststmt).getTarget());
				block.successor.add(targblock);
				targblock.predecessor.add(block);
			}
			
			if ( !(laststmt instanceof spgJump)){
				FlowGraphNode nextblock = this.NodeVec.elementAt(this.NodeVec.indexOf(block) + 1);
				if (nextblock != null){
					block.successor.add(nextblock);
					nextblock.predecessor.add(block);
				} else {
					this.ExitNode = new FlowGraphNode(null);
					this.ExitNode.isExitNode = true;
					block.successor.add(ExitNode);
					this.ExitNode.predecessor.add(block);
					if (this.RetTemp != null)
						this.ExitNode.GetInAlive().add(this.RetTemp);
				}
			}
		}
		
		for (FlowGraphNode block : this.NodeVec){
			block.FlowVec.add(new ProgramStatus(null));
		}
	}
	
	public void LiveAnalysis(){
		Queue<FlowGraphNode> LiveQueue = new LinkedList<FlowGraphNode>();
		LiveQueue.add(this.ExitNode);
		
		while (!LiveQueue.isEmpty()){
			FlowGraphNode n = LiveQueue.poll();
			if (n.LiveAnalysis() == false){
				for (FlowGraphNode preblock : n.predecessor)
					LiveQueue.add(preblock);
			}
		}
	}
	
	// not a part of flowgraph, strictly
	public boolean InStmtList = false;
	
	public static final Vector<FlowGraph> GlobalFlowGraphVec = new Vector<FlowGraph>();
}
