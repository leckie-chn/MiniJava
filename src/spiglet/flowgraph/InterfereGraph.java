package spiglet.flowgraph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import spiglet.stmtnode.spgMove;
import spiglet.stmtnode.spgTempRef;

public class InterfereGraph {
	
	public static final int MAX_COLOR = 18;
	
	private final Vector<InterfereGraphNode> NodeVec = new Vector<InterfereGraphNode>();
	
	
	public InterfereGraph(FlowGraph graph){

		for (FlowGraphNode block : graph.NodeVec){
			for (ProgramStatus point : block.FlowVec){
				if (point.AliveSet != null){
					spgTempRef [] TempArray = new spgTempRef [point.AliveSet.size()];
					int cnt = 0;
					for (spgTempRef temp : point.AliveSet){
						TempArray[cnt++] = temp;
					}
					for (int i = 0; i < TempArray.length; i++){

						if (TempArray[i].attachedNode == null)
							this.NodeVec.add(new InterfereGraphNode(TempArray[i]));
						for (int j = 0; j < i; j++){
							TempArray[i].attachedNode.Neighbors.add(TempArray[j].attachedNode);
							TempArray[j].attachedNode.Neighbors.add(TempArray[i].attachedNode);
						}
					}
					
					if (point.Statement != null && point.Statement.TargetOperand != null){
						if (point.Statement.TargetOperand.attachedNode == null)
							this.NodeVec.add(new InterfereGraphNode(point.Statement.TargetOperand));
						for (int i = 0; i < TempArray.length; i++)
							if (point.Statement.TargetOperand != TempArray[i]){
								TempArray[i].attachedNode.Neighbors.add(point.Statement.TargetOperand.attachedNode);
								point.Statement.TargetOperand.attachedNode.Neighbors.add(TempArray[i].attachedNode);
							}
					}
				}
			}
		}
		
		// adding obselete node
		for (Map.Entry<Integer, spgTempRef>  entry : spgTempRef.TempPool.entrySet()){
			spgTempRef temp = entry.getValue();
			if (temp.GetUseCount() > 0 && temp.attachedNode == null)
				this.NodeVec.add(new InterfereGraphNode(temp));
		}
		// coalescing
		/*
		for (FlowGraphNode block : graph.NodeVec){
			for (ProgramStatus point : block.FlowVec){
				if (point.Statement != null && point.Statement instanceof spgMove && ((spgMove)point.Statement).SrcOperand1 instanceof spgTempRef){
					spgMove movestmt = (spgMove) point.Statement;
					InterfereGraphNode newnode = InterfereGraphNode.Coalescing(movestmt.TargetOperand.attachedNode, ((spgTempRef)movestmt.SrcOperand1).attachedNode);
					if (newnode != null){
						this.NodeVec.removeElement(movestmt.TargetOperand.attachedNode);
						this.NodeVec.removeElement(((spgTempRef)movestmt.SrcOperand1).attachedNode);
						this.NodeVec.add(newnode);
					}
				}
			}
		}
		*/
	}
	
	public void DoColor(){
		// TODO: graph coloring algorithm
		Stack<InterfereGraphNode> VertexStack = new Stack<InterfereGraphNode>();
		
		for (int i = 0; i < this.NodeVec.size(); i++){
			// find a 'colorable' vertex
			boolean flag = false;
			for (InterfereGraphNode vertex : this.NodeVec){
				if (vertex.Deleted == false){
					int neighborcnt = 0;
					for (InterfereGraphNode adjcent : vertex.Neighbors)
						if (adjcent.Deleted == false)
							neighborcnt++;
					if (neighborcnt < InterfereGraph.MAX_COLOR){
						VertexStack.push(vertex);
						vertex.Deleted = true;
						flag = true;
					}
				}
				if (flag) break;
			}
			
			// if not find
			if (flag == false){
				// select potential spill nodes
				int MinCnt = Integer.MAX_VALUE;
				InterfereGraphNode LeastUsedNode = null;
				for (InterfereGraphNode vertex : this.NodeVec)
					if (vertex.Deleted == false){
						int UseCnt = 0;
						for (spgTempRef temp : vertex.TempNodeVec)
							UseCnt += temp.GetUseCount();
						if (UseCnt < MinCnt){
							LeastUsedNode = vertex;
							MinCnt = UseCnt;
						}
					}
				
				VertexStack.push(LeastUsedNode);
				LeastUsedNode.Deleted = true;
			}
			
		}
		
		boolean [] ColorFlag = new boolean [InterfereGraph.MAX_COLOR];
		while (!VertexStack.empty()){
			for (int i = 0; i < InterfereGraph.MAX_COLOR; i++)
				ColorFlag[i] = false;
			
			InterfereGraphNode vertex = VertexStack.pop();
			
			for (InterfereGraphNode adjcent : vertex.Neighbors)
				if (adjcent.Deleted == false && adjcent.color >= 0)
					ColorFlag[adjcent.color] = true;
			
			boolean colored = false;
			
			for (int i = 0; i < InterfereGraph.MAX_COLOR; i++){
				if (ColorFlag[i] == false){
					colored = true;
					vertex.color = i;
					break;
				}
			}
			
			if (colored == false)
				vertex.color = -1;
			
			vertex.Deleted = false;
		}
	}
	
	public void BindReg(Set<RegisterRef> calleesave){
		for (InterfereGraphNode node : this.NodeVec)
			node.BindReg(calleesave);
	}
	
	public void SpillStack(FlowGraph graph){
		for (InterfereGraphNode node : this.NodeVec)
			node.SpillStack(graph);
	}
}
