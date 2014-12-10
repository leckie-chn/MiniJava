package kanga.visitor;

import minijava.pgtree.OperatorEnum;
import kanga.expr.kgBinOp;
import kanga.expr.kgExp;
import kanga.expr.kgExprBase;
import kanga.expr.kgHAllocate;
import kanga.expr.kgInteger;
import kanga.expr.kgLabel;
import kanga.expr.kgReg;
import kanga.expr.kgSimpleExp;
import kanga.expr.kgSpilledArg;
import kanga.syntaxtree.ALoadStmt;
import kanga.syntaxtree.AStoreStmt;
import kanga.syntaxtree.BinOp;
import kanga.syntaxtree.CJumpStmt;
import kanga.syntaxtree.CallStmt;
import kanga.syntaxtree.ErrorStmt;
import kanga.syntaxtree.Exp;
import kanga.syntaxtree.Goal;
import kanga.syntaxtree.HAllocate;
import kanga.syntaxtree.HLoadStmt;
import kanga.syntaxtree.HStoreStmt;
import kanga.syntaxtree.IntegerLiteral;
import kanga.syntaxtree.JumpStmt;
import kanga.syntaxtree.Label;
import kanga.syntaxtree.MoveStmt;
import kanga.syntaxtree.NoOpStmt;
import kanga.syntaxtree.NodeToken;
import kanga.syntaxtree.PassArgStmt;
import kanga.syntaxtree.PrintStmt;
import kanga.syntaxtree.Procedure;
import kanga.syntaxtree.Reg;
import kanga.syntaxtree.SimpleExp;
import kanga.syntaxtree.SpilledArg;
import kanga.syntaxtree.Stmt;
import kanga.syntaxtree.StmtList;



public class MipsVisitor extends GJNoArguDepthFirst<kgExprBase> {
	
	// used for stack translation from kanga to mips
	// it is the overflowed arg number of the sub routine
	private int OverFlowArgNumber = 0;
	
	private boolean isListEle = false;
	/**
	    * f0 -> "MAIN"
	    * f1 -> "["
	    * f2 -> IntegerLiteral()
	    * f3 -> "]"
	    * f4 -> "["
	    * f5 -> IntegerLiteral()
	    * f6 -> "]"
	    * f7 -> "["
	    * f8 -> IntegerLiteral()
	    * f9 -> "]"
	    * f10 -> StmtList()
	    * f11 -> "END"
	    * f12 -> ( Procedure() )*
	    * f13 -> <EOF>
	    */
	   public kgExprBase visit(Goal n) {
		   kgLabel ProcedureName = new kgLabel("main");
		   
		   kgInteger ArgNum = (kgInteger) n.f2.accept(this);
		   
		   kgInteger StackNum = (kgInteger) n.f5.accept(this);
		   
		   kgInteger MaxSubArgNum = (kgInteger) n.f8.accept(this);
		   
		   int OFSubArgNum = (MaxSubArgNum.GetValue() < 4) ? 0 : (MaxSubArgNum.GetValue() - 4);
		   
		   this.OverFlowArgNumber = (ArgNum.GetValue() < 4) ? 0 : ArgNum.GetValue() - 4;
		   
		   int stackspace = (StackNum.GetValue() - this.OverFlowArgNumber + OFSubArgNum + 2) * 4;
		   
		   System.out.printf("\t\t.text\n\t\t.globl\t%s\n", ProcedureName.toString());
		   System.out.printf("%s:\n", ProcedureName.toString());
		   System.out.printf("\t\tsw $fp, -8($sp)\n");
		   System.out.printf("\t\tsw $ra, -4($sp)\n");
		   System.out.printf("\t\tadd $fp, $zero, $sp\n");
		   System.out.printf("\t\taddi $sp, $sp, %d\n", -1 * stackspace);
		   
		   n.f10.accept(this);
		   
		   System.out.printf("\t\taddi $sp, $sp, %d\n", stackspace);
		   System.out.printf("\t\tlw $ra, -4($sp)\n");
		   System.out.printf("\t\tlw $fp, -8($sp)\n");
		   System.out.printf("\t\tjr $ra\n");
		   
		   n.f12.accept(this);
		   
		   String HelperFunctionCode = "\n\n\t\t.text\n" 
		   + "\t\t.globl\t__halloc\n"
		   + "__halloc:\n"
		   + "\t\tli $v0, 9\n"
		   + "\t\tsyscall\n"
		   + "\t\tj $ra\n"
		   + "\n"
		   + "\t\t.text\n"
		   + "\t\t.globl\t__print\n"
		   + "__print:\n"
		   + "\t\tli $v0, 1\n"
		   + "\t\tsyscall\n"
		   + "\t\tla $a0, newl\n"
		   + "\t\tli $v0, 4\n"
		   + "\t\tsyscall\n"
		   + "\t\tj $ra\n"
		   + "\n"
		   + "\t\t.text\n"
		   + "\t\t.globl\t__error\n"
		   + "__error:\n"
		   + "\t\tla $a0, str_er\n"
		   + "\t\tli $v0, 4\n"
		   + "\t\tsyscall\n"
		   + "\t\tj $ra\n"
		   + "\n"
		   + "\t\t.data\n"
		   + "\t\t.align\t0\n"
		   + "newl:\t.asciiz \"\\n\"\n"
		   + "\t\t.data\n"
		   + "\t\t.align\t0\n"
		   + "str_er:\t.asciiz \" ERROR: abnormal termination\\n\"\n";
		   System.out.println(HelperFunctionCode);
		   return null;
	   }

	   /**
	    * f0 -> ( ( Label() )? Stmt() )*
	    */
	   public kgExprBase visit(StmtList n) {
		   this.isListEle = true;
		   n.f0.accept(this);
		   this.isListEle = false;
		   return null;
	   }

	   /**
	    * f0 -> Label()
	    * f1 -> "["
	    * f2 -> IntegerLiteral()
	    * f3 -> "]"
	    * f4 -> "["
	    * f5 -> IntegerLiteral()
	    * f6 -> "]"
	    * f7 -> "["
	    * f8 -> IntegerLiteral()
	    * f9 -> "]"
	    * f10 -> StmtList()
	    * f11 -> "END"
	    */
	   public kgExprBase visit(Procedure n) {
		   kgLabel ProcedureName = (kgLabel) n.f0.accept(this);
		   
		   kgInteger ArgNum = (kgInteger) n.f2.accept(this);
		   
		   kgInteger StackNum = (kgInteger) n.f5.accept(this);
		   
		   kgInteger MaxSubArgNum = (kgInteger) n.f8.accept(this);
		   
		   int OFSubArgNum = (MaxSubArgNum.GetValue() < 4) ? 0 : (MaxSubArgNum.GetValue() - 4);
		   
		   this.OverFlowArgNumber = (ArgNum.GetValue() < 4) ? 0 : ArgNum.GetValue() - 4;
		   
		   int stackspace = (StackNum.GetValue() - this.OverFlowArgNumber + OFSubArgNum + 2) * 4;
		   
		   System.out.printf("\n\n\t\t.text\n\t\t.globl\t%s\n", ProcedureName.toString());
		   System.out.printf("%s:\n", ProcedureName.toString());
		   System.out.printf("\t\taddi $sp, $sp, %d\n", -1 * stackspace);
		   System.out.printf("\t\tsw $fp, %d($sp)\n", stackspace - 8);
		   System.out.printf("\t\tsw $ra, %d($sp)\n", stackspace - 4);
		   System.out.printf("\t\taddi $fp, $sp, %d\n", stackspace);
		   
		   
		   n.f10.accept(this);
		   
		   System.out.printf("\t\taddi $sp, $sp, %d\n", stackspace);
		   System.out.printf("\t\tlw $ra, -4($sp)\n");
		   System.out.printf("\t\tlw $fp, -8($sp)\n");
		   System.out.printf("\t\tjr $ra\n");
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
	    *       | ALoadStmt()
	    *       | AStoreStmt()
	    *       | PassArgStmt()
	    *       | CallStmt()
	    */
	   public kgExprBase visit(Stmt n) {
		   this.isListEle = false;
		   n.f0.accept(this);
		   this.isListEle = true;
		   return null;
	   }

	   /**
	    * f0 -> "NOOP"
	    */
	   public kgExprBase visit(NoOpStmt n) {
		   System.out.printf("\t\tnop\n");
		   return null;
	   }

	   /**
	    * f0 -> "ERROR"
	    */
	   public kgExprBase visit(ErrorStmt n) {
		   System.out.printf("\t\tjal __error\n");
		   return null;
	   }

	   /**
	    * f0 -> "CJUMP"
	    * f1 -> Reg()
	    * f2 -> Label()
	    */
	   public kgExprBase visit(CJumpStmt n) {
		   kgReg src = (kgReg) n.f1.accept(this);
		   
		   kgLabel target = (kgLabel) n.f2.accept(this);
		   
		   System.out.printf("\t\tbeq %s, $zero, %s\n", src.GetRegName(), target.toString());
		   return null;
	   }

	   /**
	    * f0 -> "JUMP"
	    * f1 -> Label()
	    */
	   public kgExprBase visit(JumpStmt n) {
		   kgLabel target = (kgLabel) n.f1.accept(this);
		   
		   System.out.printf("\t\tj %s\n", target.toString());
		   
		   return null;
	   }

	   /**
	    * f0 -> "HSTORE"
	    * f1 -> Reg()
	    * f2 -> IntegerLiteral()
	    * f3 -> Reg()
	    */
	   public kgExprBase visit(HStoreStmt n) {
		   kgReg src1 = (kgReg) n.f1.accept(this);
		   
		   kgReg src2 = (kgReg) n.f3.accept(this);
		   
		   kgInteger offset = (kgInteger) n.f2.accept(this);
		   
		   System.out.printf("\t\tsw %s, %d(%s)\n", src2.GetRegName(), offset.GetValue(), src1.GetRegName());
		   return null;
	   }

	   /**
	    * f0 -> "HLOAD"
	    * f1 -> Reg()
	    * f2 -> Reg()
	    * f3 -> IntegerLiteral()
	    */
	   public kgExprBase visit(HLoadStmt n) {
		   kgReg dest = (kgReg) n.f1.accept(this);
		   
		   kgReg src = (kgReg) n.f2.accept(this);
		   
		   kgInteger offset = (kgInteger) n.f3.accept(this);
		   
		   System.out.printf("\t\tlw %s %d(%s)\n", dest.GetRegName(), offset.GetValue(), src.GetRegName());
		   return null;
	   }

	   /**
	    * f0 -> "MOVE"
	    * f1 -> Reg()
	    * f2 -> Exp()
	    */
	   public kgExprBase visit(MoveStmt n) {
		   kgReg dest = (kgReg) n.f1.accept(this);
		   
		   kgExp src = (kgExp) n.f2.accept(this);
		   
		   if (src instanceof kgHAllocate){
			   kgHAllocate srch = (kgHAllocate) src;
			   if (srch.GetAllocateExp() instanceof kgInteger){
				   // use li pesudoinstruction
				   System.out.printf("\t\tli $a0, %d\n", ((kgInteger)srch.GetAllocateExp()).GetValue());
			   } else {
				   System.out.printf("\t\tadd $a0, $zero, %s\n", ((kgReg)srch.GetAllocateExp()).GetRegName());
			   }
			   System.out.printf("\t\tjal __halloc\n");
			   System.out.printf("\t\tadd %s, $zero, $v0\n", dest.GetRegName());
		   } else if (src instanceof kgBinOp){
			   kgBinOp srcb = (kgBinOp) src;
			   // special cases : big integers
			   if (srcb.GetSrc2() instanceof kgInteger && ((kgInteger)srcb.GetSrc2()).GetValue() > (1 << 15) - 1){
				   System.out.printf("\t\tli $a1, %d\n", ((kgInteger)srcb.GetSrc2()).GetValue());
				   srcb = new kgBinOp(
						   srcb.GetOp(),
						   srcb.GetSrc1(),
						   new kgReg("a1")
						   );
			   }
			   switch (srcb.GetOp()){
			   case OP_LT:
				   if (srcb.GetSrc2() instanceof kgInteger){
					   System.out.printf("\t\tslti %s, %s, %d\n", dest.GetRegName(), srcb.GetSrc1().GetRegName(), ((kgInteger)srcb.GetSrc2()).GetValue());
				   } else {
					   System.out.printf("\t\tslt %s, %s, %s\n", dest.GetRegName(), srcb.GetSrc1().GetRegName(), ((kgReg)srcb.GetSrc2()).GetRegName());
				   }
				   break;
			   case OP_PLUS:
				   if (srcb.GetSrc2() instanceof kgInteger){
					   System.out.printf("\t\taddi %s, %s, %d\n", dest.GetRegName(), srcb.GetSrc1().GetRegName(), ((kgInteger)srcb.GetSrc2()).GetValue());
				   } else {
					   System.out.printf("\t\tadd %s, %s, %s\n", dest.GetRegName(), srcb.GetSrc1().GetRegName(), ((kgReg)srcb.GetSrc2()).GetRegName());
				   }
				   break;
			   case OP_MINUS:
				   if (srcb.GetSrc2() instanceof kgInteger){
					   System.out.printf("\t\taddi %s, %s, %d\n", dest.GetRegName(), srcb.GetSrc1().GetRegName(), -1 * ((kgInteger)srcb.GetSrc2()).GetValue());
				   } else {
					   System.out.printf("\t\tsub %s, %s, %s\n", dest.GetRegName(), srcb.GetSrc1().GetRegName(), ((kgReg)srcb.GetSrc2()).GetRegName());
				   }
				   break;
			   case OP_TIMES:
				   if (srcb.GetSrc2() instanceof kgInteger){
					   System.out.printf("\t\tmul %s, %s, %d\n", dest.GetRegName(), srcb.GetSrc1().GetRegName(), ((kgInteger)srcb.GetSrc2()).GetValue());
				   } else {
					   System.out.printf("\t\tmul %s, %s, %s\n", dest.GetRegName(), srcb.GetSrc1().GetRegName(), ((kgReg)srcb.GetSrc2()).GetRegName());
				   }
				   break;
			   }
		   } else if (src instanceof kgSimpleExp){
			   if (src instanceof kgInteger){
				   // use li pesudoinstruction
				   System.out.printf("\t\tli %s, %d\n", dest.GetRegName(), ((kgInteger)src).GetValue());
			   } else if (src instanceof kgReg){
				   System.out.printf("\t\tadd %s, $zero, %s\n", dest.GetRegName(), ((kgReg)src).GetRegName());
			   } else if (src instanceof kgLabel){
				   System.out.printf("\t\tla %s, %s\n", dest.GetRegName(), ((kgLabel)src).toString());
			   }
		   }
		   return null;
	   }

	   /**
	    * f0 -> "PRINT"
	    * f1 -> SimpleExp()
	    */
	   public kgExprBase visit(PrintStmt n) {
		   kgSimpleExp expr = (kgSimpleExp) n.f1.accept(this);
		   
		   if (expr instanceof kgInteger){
			   // use li pesudoinstruction
			   System.out.printf("\t\tli $a0, %d\n", ((kgInteger)expr).GetValue());
		   } else if (expr instanceof kgReg){
			   System.out.printf("\t\tadd $a0, %s, $zero\n", ((kgReg)expr).GetRegName());
		   }
		   System.out.printf("\t\tjal __print\n");
		   return null;
	   }

	   /**
	    * f0 -> "ALOAD"
	    * f1 -> Reg()
	    * f2 -> SpilledArg()
	    */
	   public kgExprBase visit(ALoadStmt n) {
		   kgReg dest = (kgReg) n.f1.accept(this);
		   
		   kgSpilledArg spilled = (kgSpilledArg) n.f2.accept(this);
		   
		   int offset;
		   
		   if (spilled.GetArgPosition() < this.OverFlowArgNumber) {
			   // overflowed argument
			   offset = spilled.GetArgPosition() * 4;
		   } else {
			   // others
			   offset = (spilled.GetArgPosition() - this.OverFlowArgNumber + 3) *(-4);
		   }
		   System.out.printf("\t\tlw %s, %d($fp)\n", dest.GetRegName(), offset);
		   return null;
	   }

	   /**
	    * f0 -> "ASTORE"
	    * f1 -> SpilledArg()
	    * f2 -> Reg()
	    */
	   public kgExprBase visit(AStoreStmt n) {
		   kgSpilledArg spilled = (kgSpilledArg) n.f1.accept(this);
		   kgReg src = (kgReg) n.f2.accept(this);
		   
		   int offset = (spilled.GetArgPosition() - this.OverFlowArgNumber + 3) * (-4);
		   System.out.printf("\t\tsw %s, %d($fp)\n", src.GetRegName(), offset);
		   return null;
	   }

	   /**
	    * f0 -> "PASSARG"
	    * f1 -> IntegerLiteral()
	    * f2 -> Reg()
	    */
	   public kgExprBase visit(PassArgStmt n) {
		   kgInteger ArgPos = (kgInteger) n.f1.accept(this);
		   kgReg dest = (kgReg) n.f2.accept(this);
	      
		   System.out.printf("\t\tsw %s, %d($sp)\n", dest.GetRegName(), (ArgPos.GetValue() - 1) * 4);
		   return null;
	   }

	   /**
	    * f0 -> "CALL"
	    * f1 -> SimpleExp()
	    */
	   public kgExprBase visit(CallStmt n) {
		   kgSimpleExp CallExp = (kgSimpleExp) n.f1.accept(this);
		   
		   if (CallExp instanceof kgLabel){
			   System.out.printf("\t\tjal %s\n", ((kgLabel)CallExp).toString());
		   } else if (CallExp instanceof kgReg){
			   System.out.printf("\t\tjalr %s\n", ((kgReg)CallExp).GetRegName());
		   }
		   return null;
	   }

	   /**
	    * f0 -> HAllocate()
	    *       | BinOp()
	    *       | SimpleExp()
	    */
	   public kgExprBase visit(Exp n) {
		   return n.f0.accept(this);
	   }

	   /**
	    * f0 -> "HALLOCATE"
	    * f1 -> SimpleExp()
	    */
	   public kgExprBase visit(HAllocate n) {
		   kgSimpleExp AllocateExp = (kgSimpleExp) n.f1.accept(this);
		   return new kgHAllocate(AllocateExp);
	   }

	   /**
	    * f0 -> Operator()
	    * f1 -> Reg()
	    * f2 -> SimpleExp()
	    */
	   public kgExprBase visit(BinOp n) {
		   OperatorEnum op = null;
		   
		   String opstr = ((NodeToken)n.f0.f0.choice).tokenImage;
		   
		   if (opstr == "LT")
			   op = OperatorEnum.OP_LT;
		   else if (opstr == "MINUS")
			   op = OperatorEnum.OP_MINUS;
		   else if (opstr == "PLUS")
			   op = OperatorEnum.OP_PLUS;
		   else if (opstr == "TIMES")
			   op = OperatorEnum.OP_TIMES;
		   
		   kgReg src1 = (kgReg) n.f1.accept(this);
		   kgSimpleExp src2 = (kgSimpleExp) n.f2.accept(this);
		   
		   return new kgBinOp(op, src1, src2);
	   }

	   

	   /**
	    * f0 -> "SPILLEDARG"
	    * f1 -> IntegerLiteral()
	    */
	   public kgExprBase visit(SpilledArg n) {
		   kgInteger ArgPos = (kgInteger) n.f1.accept(this);
		   return new kgSpilledArg(ArgPos.GetValue());
	   }

	   /**
	    * f0 -> Reg()
	    *       | IntegerLiteral()
	    *       | Label()
	    */
	   public kgExprBase visit(SimpleExp n) {
		   return n.f0.accept(this);
	   }

	   /**
	    * f0 -> "a0"
	    *       | "a1"
	    *       | "a2"
	    *       | "a3"
	    *       | "t0"
	    *       | "t1"
	    *       | "t2"
	    *       | "t3"
	    *       | "t4"
	    *       | "t5"
	    *       | "t6"
	    *       | "t7"
	    *       | "s0"
	    *       | "s1"
	    *       | "s2"
	    *       | "s3"
	    *       | "s4"
	    *       | "s5"
	    *       | "s6"
	    *       | "s7"
	    *       | "t8"
	    *       | "t9"
	    *       | "v0"
	    *       | "v1"
	    */
	   public kgExprBase visit(Reg n) {
		   return new kgReg(((NodeToken)n.f0.choice).tokenImage);
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public kgExprBase visit(IntegerLiteral n) {
		   return new kgInteger(new Integer(n.f0.tokenImage));
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public kgExprBase visit(Label n) {
		   if (this.isListEle == true){
			   System.out.println(n.f0.tokenImage + ":");
			   this.isListEle = false;
			   return null;
		   }
		   return new kgLabel(n.f0.tokenImage);
	   }
	   
}
