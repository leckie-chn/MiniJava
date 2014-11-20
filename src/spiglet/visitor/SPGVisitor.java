package spiglet.visitor;


import spiglet.flowgraph.FlowGraph;
import spiglet.stmtnode.Container;
import spiglet.stmtnode.OperatorEnum;
import spiglet.stmtnode.spgAllocate;
import spiglet.stmtnode.spgBinOp;
import spiglet.stmtnode.spgCJump;
import spiglet.stmtnode.spgCall;
import spiglet.stmtnode.spgError;
import spiglet.stmtnode.spgHLoad;
import spiglet.stmtnode.spgHStore;
import spiglet.stmtnode.spgInteger;
import spiglet.stmtnode.spgJump;
import spiglet.stmtnode.spgLabel;
import spiglet.stmtnode.spgMove;
import spiglet.stmtnode.spgNoOp;
import spiglet.stmtnode.spgPrint;
import spiglet.stmtnode.spgRoot;
import spiglet.stmtnode.spgSimpleExp;
import spiglet.stmtnode.spgTempRef;
import spiglet.syntaxtree.BinOp;
import spiglet.syntaxtree.CJumpStmt;
import spiglet.syntaxtree.Call;
import spiglet.syntaxtree.ErrorStmt;
import spiglet.syntaxtree.Goal;
import spiglet.syntaxtree.HAllocate;
import spiglet.syntaxtree.HLoadStmt;
import spiglet.syntaxtree.HStoreStmt;
import spiglet.syntaxtree.IntegerLiteral;
import spiglet.syntaxtree.JumpStmt;
import spiglet.syntaxtree.Label;
import spiglet.syntaxtree.MoveStmt;
import spiglet.syntaxtree.NoOpStmt;
import spiglet.syntaxtree.Node;
import spiglet.syntaxtree.Operator;
import spiglet.syntaxtree.PrintStmt;
import spiglet.syntaxtree.Procedure;
import spiglet.syntaxtree.SimpleExp;
import spiglet.syntaxtree.Stmt;
import spiglet.syntaxtree.StmtExp;
import spiglet.syntaxtree.StmtList;
import spiglet.syntaxtree.Temp;

public class SPGVisitor extends GJDepthFirst<spgRoot, VisitorParameter> {
	/**
	    * f0 -> "MAIN"
	    * f1 -> StmtList()
	    * f2 -> "END"
	    * f3 -> ( Procedure() )*
	    * f4 -> <EOF>
	    */
	   public spgRoot visit(Goal n, VisitorParameter argu) {
		   FlowGraph graph = new FlowGraph(new spgLabel("main"), 0);
		   FlowGraph.GlobalFlowGraphVec.add(graph);
		   n.f1.accept(this, graph);	     
		   n.f3.accept(this, argu);
		   return null;
	   }

	   /**
	    * f0 -> ( ( Label() )? Stmt() )*
	    */
	   public spgRoot visit(StmtList n, VisitorParameter argu) {
		   ((FlowGraph)argu).InStmtList = true;
		   n.f0.accept(this, argu);
		   return null;
	   }

	   /**
	    * f0 -> Label()
	    * f1 -> "["
	    * f2 -> IntegerLiteral()
	    * f3 -> "]"
	    * f4 -> StmtExp()
	    */
	   public spgRoot visit(Procedure n, VisitorParameter argu) {
	      spgLabel _label = (spgLabel) n.f0.accept(this, argu);
	      spgInteger _numb = (spgInteger) n.f2.accept(this, argu);
	      FlowGraph graph = new FlowGraph(_label, _numb.arg);
	      FlowGraph.GlobalFlowGraphVec.add(graph);
	      n.f4.accept(this, graph);
	      return null;
	   }

	   /**
	    * f0 -> NoOpStmt()
	    *       | ErrorStmt()
	    *       | CJumpStmt()
	    *       | JumpStmt()
	    *       | HStoreStmt()
	    *       | HLoadStmt()
	    *       | MoveStmt()
	    *       | PrintStmt()
	    */
	   public spgRoot visit(Stmt n, VisitorParameter argu) {
		   ((FlowGraph)argu).InStmtList = false;
		   n.f0.accept(this, argu);
		   ((FlowGraph)argu).InStmtList = true;
		   return null;
	   }

	   /**
	    * f0 -> "NOOP"
	    */
	   public spgRoot visit(NoOpStmt n, VisitorParameter argu) {
		   FlowGraph graph = (FlowGraph) argu;
		   graph.AddStmtNode(new spgNoOp());
		   return null;
	   }

	   /**
	    * f0 -> "ERROR"
	    */
	   public spgRoot visit(ErrorStmt n, VisitorParameter argu) {
		   FlowGraph graph = (FlowGraph) argu;
		   graph.AddStmtNode(new spgError());
		   return null;
	   }

	   /**
	    * f0 -> "CJUMP"
	    * f1 -> Temp()
	    * f2 -> Label()
	    */
	   public spgRoot visit(CJumpStmt n, VisitorParameter argu) {
		   spgTempRef CondTemp = (spgTempRef) n.f1.accept(this, argu);
		   spgLabel Target = (spgLabel) n.f2.accept(this, argu);
		   FlowGraph graph = (FlowGraph) argu;
		   graph.AddStmtNode(new spgCJump(CondTemp, Target));
		   return null;
	   }

	   /**
	    * f0 -> "JUMP"
	    * f1 -> Label()
	    */
	   public spgRoot visit(JumpStmt n, VisitorParameter argu) {
		   FlowGraph graph = (FlowGraph) argu;
		   spgLabel Target = (spgLabel) n.f1.accept(this, argu);
		   graph.AddStmtNode(new spgJump(Target));
		   return null;
	   }

	   /**
	    * f0 -> "HSTORE"
	    * f1 -> Temp()
	    * f2 -> IntegerLiteral()
	    * f3 -> Temp()
	    */
	   public spgRoot visit(HStoreStmt n, VisitorParameter argu) {
		   spgTempRef A = (spgTempRef) n.f1.accept(this, argu);
		   spgInteger B = (spgInteger) n.f2.accept(this, argu);
		   spgTempRef C = (spgTempRef) n.f3.accept(this, argu);
		   FlowGraph graph = (FlowGraph) argu;
		   graph.AddStmtNode(new spgHStore(A, B, C));
		   return null;
	   }

	   /**
	    * f0 -> "HLOAD"
	    * f1 -> Temp()
	    * f2 -> Temp()
	    * f3 -> IntegerLiteral()
	    */
	   public spgRoot visit(HLoadStmt n, VisitorParameter argu) {
		   spgTempRef A = (spgTempRef) n.f1.accept(this, argu);
		   spgTempRef B = (spgTempRef) n.f2.accept(this, argu);
		   spgInteger C = (spgInteger) n.f3.accept(this, argu);
		   FlowGraph graph = (FlowGraph) argu;
		   graph.AddStmtNode(new spgHLoad(A, B, C));
		   return null;
	   }

	   /**
	    * f0 -> "MOVE"
	    * f1 -> Temp()
	    * f2 -> Exp()
	    */
	   public spgRoot visit(MoveStmt n, VisitorParameter argu) {
		   FlowGraph graph = (FlowGraph) argu;
		   spgTempRef A = (spgTempRef) n.f1.accept(this, argu);
		   Node ExpNode = n.f2.f0.choice;
		   if (ExpNode instanceof SimpleExp){
			   spgSimpleExp B = (spgSimpleExp) ExpNode.accept(this, argu);
			   graph.AddStmtNode(new spgMove(A, B));
			   return null;
		   }
		   if (ExpNode instanceof HAllocate){
			   spgSimpleExp B = (spgSimpleExp) ((HAllocate)ExpNode).f1.accept(this, argu);
			   graph.AddStmtNode(new spgAllocate(A, B));
			   return null;
		   }
		   if (ExpNode instanceof BinOp){
			   OperatorEnum opcode = (OperatorEnum) ((BinOp)ExpNode).f0.accept(this, argu);
			   spgTempRef B = (spgTempRef) ((BinOp)ExpNode).f1.accept(this, argu);
			   spgSimpleExp C = (spgSimpleExp) ((BinOp)ExpNode).f2.accept(this, argu);
			   graph.AddStmtNode(new spgBinOp(A, B, C, opcode));
			   return null;
		   }
		   if (ExpNode instanceof Call){
			   spgSimpleExp B = (spgSimpleExp) ((Call)ExpNode).f1.accept(this, argu);
			   Container container = new Container();
			   ((Call)ExpNode).f3.accept(this, container);
			   graph.AddStmtNode(new spgCall(A, B, container.arg));
			   return null;
		   }
		   return null;
	   }

	   /**
	    * f0 -> "PRINT"
	    * f1 -> SimpleExp()
	    */
	   public spgRoot visit(PrintStmt n, VisitorParameter argu) {
		   spgSimpleExp A = (spgSimpleExp) n.f1.accept(this, argu);
		   FlowGraph graph = (FlowGraph) argu;
		   graph.AddStmtNode(new spgPrint(A));
		   return null;
	   }

	   /**
	    * f0 -> "BEGIN"
	    * f1 -> StmtList()
	    * f2 -> "RETURN"
	    * f3 -> SimpleExp()
	    * f4 -> "END"
	    */
	   public spgRoot visit(StmtExp n, VisitorParameter argu) {
		   n.f1.accept(this, argu);
		   spgSimpleExp _ret = (spgSimpleExp) n.f3.accept(this, argu);
		   FlowGraph graph = (FlowGraph) argu;
		   if (_ret instanceof spgTempRef)
			   graph.SetRetTemp((spgTempRef) _ret);
		   return null;
	   }

	   /**
	    * f0 -> "LT"
	    *       | "PLUS"
	    *       | "MINUS"
	    *       | "TIMES"
	    */
	   public spgRoot visit(Operator n, VisitorParameter argu) {
		   String OpLiteral = n.f0.choice.toString();
		   if (OpLiteral.equals("LT"))
			   return OperatorEnum.OP_LT;
		   if (OpLiteral.equals("MINUS"))
			   return OperatorEnum.OP_MINUS;
		   if (OpLiteral.equals("PLUS"))
			   return OperatorEnum.OP_PLUS;
		   if (OpLiteral.equals("TIMES"))
			   return OperatorEnum.OP_TIMES;
		   return null;
	   }

	   /**
	    * f0 -> Temp()
	    *       | IntegerLiteral()
	    *       | Label()
	    */
	   public spgRoot visit(SimpleExp n, VisitorParameter argu) {
	      
		   return n.f0.accept(this, argu);
	   }

	   /**
	    * f0 -> "TEMP"
	    * f1 -> IntegerLiteral()
	    */
	   public spgRoot visit(Temp n, VisitorParameter argu) {
		   spgInteger TempNumb = (spgInteger) n.f1.accept(this, argu);
		   if (argu instanceof Container){
			   Container _container = (Container) argu;
			   _container.arg.add(spgTempRef.GetTempByNum(TempNumb.arg));
			   return null;
		   }
		   return spgTempRef.GetTempByNum(TempNumb.arg);
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public spgRoot visit(IntegerLiteral n, VisitorParameter argu) {
		   return new spgInteger(new Integer(n.f0.tokenImage));
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public spgRoot visit(Label n, VisitorParameter argu) {
		   FlowGraph graph = (FlowGraph) argu;
		   if (graph != null && graph.InStmtList == true){
			   graph.AddStmtNode(new spgLabel(n.f0.tokenImage));
			   return null;
		   }
		   return new spgLabel(n.f0.tokenImage);
	   }
}
