package spiglet.flowgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;




















import spiglet.kgtree.kgALoadStmt;
import spiglet.kgtree.kgAStoreStmt;
import spiglet.kgtree.kgBinOp;
import spiglet.kgtree.kgCallStmt;
import spiglet.kgtree.kgInteger;
import spiglet.kgtree.kgLabel;
import spiglet.kgtree.kgMoveStmt;
import spiglet.kgtree.kgProcedure;
import spiglet.kgtree.kgReg;
import spiglet.kgtree.kgSpilledArg;
import spiglet.kgtree.kgStmt;
import spiglet.stmtnode.spgInteger;
import spiglet.stmtnode.spgJump;
import spiglet.stmtnode.spgJumpAble;
import spiglet.stmtnode.spgLabel;
import spiglet.stmtnode.spgSimpleExp;
import spiglet.stmtnode.spgStmtNode;
import spiglet.stmtnode.spgTempRef;
import spiglet.visitor.VisitorParameter;

public class FlowGraph implements VisitorParameter{
	
	public FlowGraphNode EntryNode;
	
	public FlowGraphNode ExitNode;
	
	public final spgLabel ProcedureName;
	
	public final int ParaNum;  // denotes the number of paranumber 
	
	public final Map<String, FlowGraphNode> BLMapping = new HashMap<String, FlowGraphNode>();
	
	private final Vector<spgStmtNode> RawStmtVec = new Vector<spgStmtNode>();
	
	public final Vector<FlowGraphNode> NodeVec = new Vector<FlowGraphNode>();
	
	private spgSimpleExp RetExpr;
	
	private InterfereGraph ITFGraph = null;
	
	private final Set<RegisterRef> CalleeSave = new HashSet<RegisterRef>();
	
	public FlowGraph(spgLabel _label, int _num){
		this.ProcedureName = _label;
		this.ParaNum = _num;
		this.StackPosCnt = (_num - 4 > 0 ) ? _num - 4 : 0;	// set the stack position count
	}
	
	public void AddStmtNode(spgStmtNode _stmt){
		this.RawStmtVec.add(_stmt);
	}
	
	public void SetRetTemp(spgSimpleExp _ret){
		this.RetExpr = _ret;
	}
	
	public void Init(){
		// flow graph & basic block construction
		FlowGraphNode CurrentNode = null;
		
		// block division
		for (spgStmtNode _stmt : this.RawStmtVec){
			if (_stmt instanceof spgLabel){
				CurrentNode = new FlowGraphNode((spgLabel)_stmt);
				this.NodeVec.add(CurrentNode);
				this.BLMapping.put(((spgLabel)_stmt).arg, CurrentNode);
			} else {
				if (CurrentNode == null){
					CurrentNode = new FlowGraphNode(null);
					this.NodeVec.add(CurrentNode);
				}
				CurrentNode.FlowVec.add(new ProgramStatus(_stmt));
				if (_stmt instanceof spgJumpAble)
					CurrentNode = null;
			}
		}
		
		// flow connection
		for (int i = 0; i < this.NodeVec.size(); i++){
			FlowGraphNode block = this.NodeVec.elementAt(i);
			spgStmtNode laststmt = block.FlowVec.lastElement().Statement;
			if (laststmt instanceof spgJumpAble){
				FlowGraphNode targblock = this.BLMapping.get(((spgJumpAble) laststmt).getTarget().arg);
				block.successor.add(targblock);
				targblock.predecessor.add(block);
			}
			
			if ( (laststmt instanceof spgJump) == false ){
				if (i + 1 < this.NodeVec.size()){
					FlowGraphNode nextblock = this.NodeVec.elementAt(i + 1);
					block.successor.add(nextblock);
					nextblock.predecessor.add(block);
				} else {
					this.ExitNode = new FlowGraphNode(null);
					this.ExitNode.isExitNode = true;
					block.successor.add(ExitNode);
					this.ExitNode.predecessor.add(block);
					if (this.RetExpr != null && this.RetExpr instanceof spgTempRef)
						this.ExitNode.SetRetTemp((spgTempRef) RetExpr);
				}
			}
		}
		
		for (FlowGraphNode block : this.NodeVec){
			block.FlowVec.add(new ProgramStatus(null));
		}
		
		this.EntryNode = this.NodeVec.elementAt(0);
	}
	
	@SuppressWarnings("unused")
	public void LiveAnalysis(){
		Queue<FlowGraphNode> LiveQueue = new LinkedList<FlowGraphNode>();
		LiveQueue.add(this.ExitNode);
		while (LiveQueue.isEmpty() == false){
			FlowGraphNode n = LiveQueue.poll();
			spgLabel label = n.LeadingLabel;
			if (n == null) break;
			if (n.LiveAnalysis() == false){
				for (FlowGraphNode preblock : n.predecessor)
					LiveQueue.add(preblock);
			}
		}
	}
	
	
	public void DoRegAllocation(){
		this.ITFGraph = new InterfereGraph(this);
		this.ITFGraph.DoColor();
		this.ITFGraph.BindReg(this.CalleeSave);
		this.StackPosCnt += this.CalleeSave.size();
		this.ITFGraph.SpillStack(this);
	}
	
	
	// procedure translation part
	
	public int StackPosCnt;
	
	public kgProcedure DoTranslation(){
		kgProcedure _ret = new kgProcedure(new kgLabel(this.ProcedureName.arg), this.ParaNum, this.StackPosCnt + 10);
		_ret.f3 = 0; 
		
		// arg load
		for (int i = 0; i < this.ParaNum; i++){
			spgTempRef paratemp = spgTempRef.GetTempByNum(i);
			RegisterRef target = paratemp.register;
			if (target == null){
				if (paratemp.StackPos < 0) continue;
				target = RegisterRef.VRegs[1];
			}
			if (i < 4)
				_ret.f4.f0.add(new kgMoveStmt(
						new kgReg(target),
						new kgReg(RegisterRef.ARegs[i])
						));
			else 
				_ret.f4.f0.add(new kgALoadStmt(
						new kgReg(target),
						new kgSpilledArg(i - 4)
						));
			if (paratemp.register == null)
				_ret.f4.f0.add(new kgAStoreStmt(
						new kgSpilledArg(paratemp.StackPos),
						new kgReg(target)
						));
		}
		
		// callee save 
		kgReg [] RegArray = new kgReg [this.CalleeSave.size()];
		
		int ArrCnt = 0;
		
		for (RegisterRef _reg : this.CalleeSave)
			RegArray[ArrCnt++] = new kgReg(_reg);
		
		for (int i = 0; i < RegArray.length; i++)
			_ret.f4.f0.add(new kgAStoreStmt(
					new kgSpilledArg(i + (this.ParaNum > 4 ? this.ParaNum : 4) - 4),
					RegArray[i]
					));
		
		
		RegisterRef.ClearCallerSave();
		// do translation
		for (spgStmtNode stmt : this.RawStmtVec)
			stmt.DoTranslation(_ret);
		
		// do caller save
		// first. scan the entire whole statement list, automated by kgReg Constructor function
		// second, scan the entire whole statement list, find the call statement, and insert the caller save & restore statement
		
		Vector<kgCallStmt> StmtList = new Vector<kgCallStmt>();
		
		for (kgStmt stmt : _ret.f4.f0)
			if (stmt instanceof kgCallStmt)
				StmtList.add((kgCallStmt)stmt);
		for (kgCallStmt stmt : StmtList){
			int callindex = _ret.f4.f0.indexOf(stmt);
			// caller restore first
			for (int i = 0 ; i < 10; i++)
				if (RegisterRef.CallerSaveFlag[i] == true){
					_ret.f4.f0.insertElementAt(new kgALoadStmt(
							new kgReg(RegisterRef.TRegs[i]),
							new kgSpilledArg(i + _ret.f2 - 10)
							),
							callindex + 1); 
				}
			
			// then caller save
			for (int i = 9; i >= 0; i--)
				if (RegisterRef.CallerSaveFlag[i] == true){
					_ret.f4.f0.insertElementAt(new kgAStoreStmt(
							new kgSpilledArg(i + _ret.f2 - 10),
							new kgReg(RegisterRef.TRegs[i])
							),
							callindex);
				}
			
		}
		
		
		// callee restore
		for (int i = RegArray.length - 1; i >= 0; i--)
			_ret.f4.f0.add(new kgALoadStmt(
					RegArray[i],
					new kgSpilledArg(i + (this.ParaNum > 4 ? this.ParaNum : 4) - 4)
					));
		 
		if (this.RetExpr != null){
			if (this.RetExpr instanceof spgTempRef){
				spgTempRef RetTemp = (spgTempRef) this.RetExpr;
				if (RetTemp.register == null)
					_ret.f4.f0.add(new kgALoadStmt(
							new kgReg(RegisterRef.VRegs[0]),
							new kgSpilledArg(RetTemp.StackPos)
							));
				else
					_ret.f4.f0.add(new kgMoveStmt(
							new kgReg(RegisterRef.VRegs[0]),
							new kgReg(RetTemp.register)
							));
			} else if (this.RetExpr instanceof spgInteger){
				_ret.f4.f0.add(new kgMoveStmt(
						new kgReg(RegisterRef.VRegs[0]),
						new kgInteger(((spgInteger)this.RetExpr).arg)
						));
			}
		}
		return _ret;
	}
	
	// not a part of flowgraph, strictly
	public boolean InStmtList = false;
	
	public static final Vector<FlowGraph> GlobalFlowGraphVec = new Vector<FlowGraph>();
}
