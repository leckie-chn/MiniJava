package minijava.visitor;

import java.util.Map;

import minijava.pgtree.*;
import minijava.symboltable.*;
import minijava.minijava2piglet.*;
import minijava.syntaxtree.AllocationExpression;
import minijava.syntaxtree.AndExpression;
import minijava.syntaxtree.ArrayAllocationExpression;
import minijava.syntaxtree.ArrayAssignmentStatement;
import minijava.syntaxtree.ArrayLength;
import minijava.syntaxtree.ArrayLookup;
import minijava.syntaxtree.ArrayType;
import minijava.syntaxtree.AssignmentStatement;
import minijava.syntaxtree.Block;
import minijava.syntaxtree.BooleanType;
import minijava.syntaxtree.BracketExpression;
import minijava.syntaxtree.ClassDeclaration;
import minijava.syntaxtree.ClassExtendsDeclaration;
import minijava.syntaxtree.CompareExpression;
import minijava.syntaxtree.Expression;
import minijava.syntaxtree.ExpressionList;
import minijava.syntaxtree.ExpressionRest;
import minijava.syntaxtree.FalseLiteral;
import minijava.syntaxtree.FormalParameter;
import minijava.syntaxtree.FormalParameterList;
import minijava.syntaxtree.FormalParameterRest;
import minijava.syntaxtree.Goal;
import minijava.syntaxtree.Identifier;
import minijava.syntaxtree.IfStatement;
import minijava.syntaxtree.IntegerLiteral;
import minijava.syntaxtree.IntegerType;
import minijava.syntaxtree.MainClass;
import minijava.syntaxtree.MessageSend;
import minijava.syntaxtree.MethodDeclaration;
import minijava.syntaxtree.MinusExpression;
import minijava.syntaxtree.NotExpression;
import minijava.syntaxtree.PlusExpression;
import minijava.syntaxtree.PrimaryExpression;
import minijava.syntaxtree.PrintStatement;
import minijava.syntaxtree.Statement;
import minijava.syntaxtree.ThisExpression;
import minijava.syntaxtree.TimesExpression;
import minijava.syntaxtree.TrueLiteral;
import minijava.syntaxtree.Type;
import minijava.syntaxtree.TypeDeclaration;
import minijava.syntaxtree.VarDeclaration;
import minijava.syntaxtree.WhileStatement;

public class pgVisitor extends GJDepthFirst<pgNode, MType> {
	/**
	    * f0 -> MainClass()
	    * f1 -> ( TypeDeclaration() )*
	    * f2 -> <EOF>
	    */
	   public pgNode visit(Goal n, MType argu) {
		   Main.ProgramGoal = new pgGoal();
		   
		   // global class table init
		   MType.GlobalTableTemp = new pgTemp();
		   pgProcedure Global_Init = MType.Get_Global_Init();
		   Main.ProgramGoal.AddProcedure(Global_Init);
		   Main.ProgramGoal.f0.f0.add(new pgMoveStmt(
				   MType.GlobalTableTemp,
				   new pgCall(Global_Init.f0)
				   ));
		   // local class constructor init
		   for (Map.Entry<String, MType> entry : MType.RootSymbolTable.entrySet()){
			   if (entry.getValue() instanceof MClass && entry.getValue() != MType.MainClass)
				   Main.ProgramGoal.AddProcedure(((MClass) entry.getValue()).GenConstructorCode());
		   }
		   
		   n.f0.accept(this, argu);
		   n.f1.accept(this, argu);
		   
		   return null;
	   }

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "{"
	    * f3 -> "public"
	    * f4 -> "static"
	    * f5 -> "void"
	    * f6 -> "main"
	    * f7 -> "("
	    * f8 -> "String"
	    * f9 -> "["
	    * f10 -> "]"
	    * f11 -> Identifier()
	    * f12 -> ")"
	    * f13 -> "{"
	    * f14 -> PrintStatement()
	    * f15 -> "}"
	    * f16 -> "}"
	    * @return null
	    */
	   public pgNode visit(MainClass n, MType argu) {
		   MType.MainMethod._list = Main.ProgramGoal.f0;
		   n.f14.accept(this, MType.MainMethod);
		   return null;
	   }

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "{"
	    * f3 -> ( VarDeclaration() )*
	    * f4 -> ( MethodDeclaration() )*
	    * f5 -> "}"
	    */
	   public pgNode visit(ClassDeclaration n, MType argu) {
		   pgLabel ClassID = (pgLabel) n.f1.accept(this, argu);
		   MClass MasterClass = (MClass) MType.GetTypeByID(new MIdentifier(ClassID.f0, -1));
		   n.f4.accept(this, MasterClass);
		   return null;
	   }

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "extends"
	    * f3 -> Identifier()
	    * f4 -> "{"
	    * f5 -> ( VarDeclaration() )*
	    * f6 -> ( MethodDeclaration() )*
	    * f7 -> "}"
	    */
	   public pgNode visit(ClassExtendsDeclaration n, MType argu) {
		   pgLabel ClassID = (pgLabel) n.f1.accept(this, argu);
		   MClass MasterClass = (MClass) MType.GetTypeByID(new MIdentifier(ClassID.f0, -1));
		   n.f6.accept(this, MasterClass);
		   return null;
	   }
	   
	   /**
	    * f0 -> "public"
	    * f1 -> Type()
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( FormalParameterList() )?
	    * f5 -> ")"
	    * f6 -> "{"
	    * f7 -> ( VarDeclaration() )*
	    * f8 -> ( Statement() )*
	    * f9 -> "return"
	    * f10 -> Expression()
	    * f11 -> ";"
	    * f12 -> "}"
	    * @param MType MasterClass, the context class
	    * @return null
	    */
	   public pgNode visit(MethodDeclaration n, MType argu) {
		   MClass MasterClass = (MClass) argu;
		   pgLabel MethodID = (pgLabel) n.f2.accept(this, argu);
		   MMethod _method = MasterClass.GetMethod(new MIdentifier(MethodID.f0, -1));
		   _method._list = new pgStmtList();
		   
		   // move this from special temp 0 to normal temp
		   _method.MasterClassTemp = new pgTemp();
		   _method._list.f0.add(new pgMoveStmt(
				   _method.MasterClassTemp,
				   Main.ProgramGoal.SpecialTemp[0]
				   ));
		   // move para from special temp to normal temp
		   for (MVar paraVar : _method.ParaTypeList){
			   paraVar.VarTemp = new pgTemp();
			   paraVar.VarTemp.TempType = paraVar.GetVarType();
			   if (_method.ParaTypeList.size() > 19 && _method.ParaTypeList.indexOf(paraVar) > 17){
				   _method._list.f0.add(new pgHLoadStmt(
						   paraVar.VarTemp,
						   Main.ProgramGoal.SpecialTemp[19],
						   new pgIntegerLiteral(_method.ParaTypeList.indexOf(paraVar) * 4 - 72)
						   ));
			   } else
				   _method._list.f0.add(new pgMoveStmt(
						   paraVar.VarTemp,
						   Main.ProgramGoal.SpecialTemp[_method.ParaTypeList.indexOf(paraVar) + 1]
						   ));
		   }
			   
		   n.f8.accept(this, _method);
		   pgExp RetExp = (pgExp) n.f10.accept(this, _method);
		   Main.ProgramGoal.AddProcedure(new pgProcedure(
				   new pgLabel(_method.PgName),
				   new pgIntegerLiteral(_method.ParaTypeList.size() + 1),
				   new pgStmtExp(_method._list, RetExp)
				   ));
		   return null;
	   }
	   
	   
	   /**
	    * f0 -> Identifier()
	    * f1 -> "="
	    * f2 -> Expression()
	    * f3 -> ";"
	    */
	   public pgNode visit(AssignmentStatement n, MType argu) {
		   pgLabel VarID = (pgLabel) n.f0.accept(this, argu);
		   MMethod context = (MMethod) argu;
		   MVar _variable = context.GetVar(new MIdentifier(VarID.f0, -1));
		   
		   if (_variable.isClassMember){
			   context._list.f0.add(new pgHStoreStmt(
					   context.MasterClassTemp,
					   new pgIntegerLiteral(_variable.VarSerialNo * 4),
					   (pgExp) n.f2.accept(this, argu)
					   ));
		   } else {
			   if (_variable.VarTemp == null){
				   _variable.VarTemp = new pgTemp();
				   _variable.VarTemp.TempType = _variable.GetVarType();
			   }
			   context._list.f0.add(new pgMoveStmt(
					   _variable.VarTemp,
					   (pgExp) n.f2.accept(this, argu)
					   ));
		   }
		   return null;
	   }

	   /**
	    * f0 -> Identifier()
	    * f1 -> "["
	    * f2 -> Expression()
	    * f3 -> "]"
	    * f4 -> "="
	    * f5 -> Expression()
	    * f6 -> ";"
	    */
	   public pgNode visit(ArrayAssignmentStatement n, MType argu) {
		   pgLabel ArrayID = (pgLabel) n.f0.accept(this, argu);
		   MMethod context = (MMethod) argu;
		   MVar ArrayVar = context.GetVar(new MIdentifier(ArrayID.f0, -1));
		   
		   // get the array base and store it into a temp
		   pgTemp ArrayTemp = null;
		   if (ArrayVar.isClassMember){
			   ArrayTemp = new pgTemp();
			   context._list.f0.add(new pgHLoadStmt(
					   ArrayTemp,
					   context.MasterClassTemp,
					   new pgIntegerLiteral(ArrayVar.VarSerialNo * 4)
					   ));
		   } else {
			   if (ArrayVar.VarTemp == null)
				   ArrayVar.VarTemp = new pgTemp();
			   ArrayTemp = ArrayVar.VarTemp;
		   }
		   
		   pgExp ArrayIndex = (pgExp) n.f2.accept(this, argu);
		   
		   // add the assignment statement
		   if (ArrayIndex instanceof pgIntegerLiteral){
			   context._list.f0.add(new pgHStoreStmt(
					   ArrayTemp,
					   new pgIntegerLiteral(((pgIntegerLiteral) ArrayIndex).f0 * 4 + 4),
					   (pgExp) n.f5.accept(this, argu)
					   ));
		   } else {
			   context._list.f0.add(new pgHStoreStmt(
					   	new pgBinOp(
							   OperatorEnum.OP_PLUS, 
							   ArrayTemp, 
							   new pgBinOp(
									   OperatorEnum.OP_TIMES,
									   ArrayIndex,
									   new pgIntegerLiteral(4)
									   )
							   ),
						new pgIntegerLiteral(4),
						(pgExp) n.f5.accept(this, argu)
					   ));
		   }
		   return null;
	   }

	   /**
	    * f0 -> "if"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> Statement()
	    * f5 -> "else"
	    * f6 -> Statement()
	    */
	   public pgNode visit(IfStatement n, MType argu) {
		   MMethod context = (MMethod) argu;
		   pgLabel eLabel = new pgLabel();
		   pgLabel OutletLabel = new pgLabel();
		   context._list.f0.add(new pgCJumpStmt(
				   (pgExp) n.f2.accept(this, argu),
				   eLabel
				   ));
		   n.f4.accept(this, argu);
		   context._list.f0.add(new pgJumpStmt(
				   OutletLabel
				   ));
		   context._list.f0.add(eLabel);
		   n.f6.accept(this, argu);
		   context._list.f0.add(OutletLabel);
		   return null;
	   }

	   /**
	    * f0 -> "while"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> Statement()
	    */
	   public pgNode visit(WhileStatement n, MType argu) {
		   MMethod context = (MMethod) argu;
		   pgLabel LoopLabel = new pgLabel();
		   pgLabel OutletLabel = new pgLabel();
		   context._list.f0.add(LoopLabel);
		   context._list.f0.add(new pgCJumpStmt(
				   (pgExp) n.f2.accept(this, argu),
				   OutletLabel
				   ));
		   n.f4.accept(this, argu);
		   context._list.f0.add(new pgJumpStmt(
				   LoopLabel
				   ));
		   context._list.f0.add(OutletLabel);
		   return null;
	   }

	   /**
	    * f0 -> "System.out.println"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> ";"
	    */
	   public pgNode visit(PrintStatement n, MType argu) {
		   MMethod context = (MMethod) argu;
		   context._list.f0.add(new pgPrintStmt(
				   (pgExp) n.f2.accept(this, argu)
				   ));
		   return null;
	   }

	   /**
	    * f0 -> AndExpression()
	    *       | CompareExpression()
	    *       | PlusExpression()
	    *       | MinusExpression()
	    *       | TimesExpression()
	    *       | ArrayLookup()
	    *       | ArrayLength()
	    *       | MessageSend()
	    *       | PrimaryExpression()
	    * 
	    * @return pgExp
	    */
	   public pgNode visit(Expression n, MType argu) {
		   if (argu instanceof MParameter){
			   // subnode of Message Call
			   MParameter container = (MParameter) argu;
			   pgExp paraExp = (pgExp) n.f0.accept(this, container._context);
			   if (container.ParaArray != null && container.CallNode.f1.size() > 18){
				   container._context._list.f0.add(new pgHStoreStmt(
						   container.ParaArray,
						   new pgIntegerLiteral(container.ParaOverFlowCnt++ * 4),
						   paraExp
						   ));
			   } else
				   container.CallNode.f1.add(paraExp);
			   return null;
		   }
		   return n.f0.accept(this, argu);
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "&&"
	    * f2 -> PrimaryExpression()
	    * @return pgExp
	    */
	   public pgNode visit(AndExpression n, MType argu) {
		   return new pgBinOp(
				   OperatorEnum.OP_TIMES,
				   (pgExp) n.f0.accept(this, argu),
				   (pgExp) n.f2.accept(this, argu)
				   );
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "<"
	    * f2 -> PrimaryExpression()
	    */
	   public pgNode visit(CompareExpression n, MType argu) {
		   return new pgBinOp(
				   OperatorEnum.OP_LT,
				   (pgExp) n.f0.accept(this, argu),
				   (pgExp) n.f2.accept(this, argu)
				   );
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "+"
	    * f2 -> PrimaryExpression()
	    */
	   public pgNode visit(PlusExpression n, MType argu) {
		   return new pgBinOp(
				   OperatorEnum.OP_PLUS,
				   (pgExp) n.f0.accept(this, argu),
				   (pgExp) n.f2.accept(this, argu)
				   );
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "-"
	    * f2 -> PrimaryExpression()
	    */
	   public pgNode visit(MinusExpression n, MType argu) {
		   return new pgBinOp(
				   OperatorEnum.OP_MINUS,
				   (pgExp) n.f0.accept(this, argu),
				   (pgExp) n.f2.accept(this, argu)
				   );
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "*"
	    * f2 -> PrimaryExpression()
	    */
	   public pgNode visit(TimesExpression n, MType argu) {
		   return new pgBinOp(
				   OperatorEnum.OP_TIMES,
				   (pgExp) n.f0.accept(this, argu),
				   (pgExp) n.f2.accept(this, argu)
				   );
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "["
	    * f2 -> PrimaryExpression()
	    * f3 -> "]"
	    */
	   public pgNode visit(ArrayLookup n, MType argu) {
		   MMethod context = (MMethod) argu;
		   pgExp ArrayBase = (pgExp) n.f0.accept(this, argu);
		   pgTemp EleTemp = new pgTemp();
		   pgExp ArrayIndex = (pgExp) n.f2.accept(this, argu);
		   
		   if (ArrayIndex instanceof pgIntegerLiteral){
			   context._list.f0.add(new pgHLoadStmt(
					   EleTemp,
					   ArrayBase,
					   new pgIntegerLiteral(((pgIntegerLiteral) ArrayIndex).f0 * 4 + 4)
					   ));
		   } else {
			   context._list.f0.add(new pgHLoadStmt(
					   EleTemp,
					   new pgBinOp(
							   OperatorEnum.OP_PLUS,
							   ArrayBase,
							   new pgBinOp(
									   OperatorEnum.OP_TIMES,
									   ArrayIndex,
									   new pgIntegerLiteral(4)
									   )
							   ),
					   new pgIntegerLiteral(4)
					   ));
		   }
		   
		   return EleTemp;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> "length"
	    */
	   public pgNode visit(ArrayLength n, MType argu) {
		   pgTemp LenTemp = new pgTemp();
		   MMethod context = (MMethod) argu;
		   context._list.f0.add(new pgHLoadStmt(
				   LenTemp,
				   (pgExp) n.f0.accept(this, argu),
				   new pgIntegerLiteral(0)
				   ));
		   return LenTemp;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( ExpressionList() )?
	    * f5 -> ")"
	    */
	   public pgNode visit(MessageSend n, MType argu) {
		   pgCall CallNode = new pgCall();
		   pgTemp CallerTemp = (pgTemp) n.f0.accept(this, argu);
		   pgLabel MethodID = (pgLabel) n.f2.accept(this, argu);
		   MMethod context = (MMethod) argu;
		   MMethod _method = ((MClass) CallerTemp.TempType).GetMethod(new MIdentifier(MethodID.f0, -1));
		   MParameter container = new MParameter();
		   container._context = context;
		   container.CallNode = CallNode;
		   container.CallNode.f1.add(CallerTemp);
		   if (_method.ParaTypeList.size() > 18){
			   container.ParaArray = new pgTemp();
		   }
		   n.f4.accept(this, container);
		   if (container.ParaArray != null){
			   container.CallNode.f1.add(container.ParaArray);
		   }
		   pgTemp DtableTemp = new pgTemp();
		   pgTemp MAddrTemp = new pgTemp();
		   context._list.f0.add(new pgHLoadStmt(
				   DtableTemp,
				   CallerTemp,
				   new pgIntegerLiteral(0)
				   ));
		   context._list.f0.add(new pgHLoadStmt(
				   MAddrTemp,
				   DtableTemp,
				   new pgIntegerLiteral(_method.MethodSerialNo * 4)
				   ));
		   CallNode.f0 = MAddrTemp;
		   CallNode.RetType = _method.GetRetType();
		   return CallNode;
	   }


	   /**
	    * f0 -> IntegerLiteral()
	    *       | TrueLiteral()
	    *       | FalseLiteral()
	    *       | Identifier()
	    *       | ThisExpression()
	    *       | ArrayAllocationExpression()
	    *       | AllocationExpression()
	    *       | NotExpression()
	    *       | BracketExpression()
	    */
	   public pgNode visit(PrimaryExpression n, MType argu) {
		   pgExp _ret = (pgExp) n.f0.accept(this, argu);
		   if (_ret instanceof pgLabel){
			   // sub node is Identifier, denotes a variable
			   MIdentifier VarID = new MIdentifier(((pgLabel)_ret).f0, -1);
			   MMethod context = (MMethod) argu;
			   MVar _variable = context.GetVar(VarID);
			   if (_variable.isClassMember){
				   pgTemp VarTemp = new pgTemp();
				   context._list.f0.add(new pgHLoadStmt(
						   VarTemp,
						   context.MasterClassTemp,
						   new pgIntegerLiteral(_variable.VarSerialNo * 4)
						   ));
				   VarTemp.TempType = _variable.GetVarType();
				   return VarTemp;
			   } else {
				   if (_variable.VarTemp == null){
					   _variable.VarTemp = new pgTemp();
					   _variable.VarTemp.TempType = _variable.GetVarType();
				   }
				   return _variable.VarTemp;
			   }
		   } else 
			   return _ret;
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public pgNode visit(IntegerLiteral n, MType argu) {
		   return new pgIntegerLiteral(new Integer(n.f0.tokenImage));
	   }

	   /**
	    * f0 -> "true"
	    */
	   public pgNode visit(TrueLiteral n, MType argu) {
		   return new pgIntegerLiteral(1);
	   }

	   /**
	    * f0 -> "false"
	    */
	   public pgNode visit(FalseLiteral n, MType argu) {
		   return new pgIntegerLiteral(0);
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    * @return pgLabel
	    */
	   public pgNode visit(Identifier n, MType argu) {
		   return new pgLabel(n.f0.tokenImage);
	   }

	   /**
	    * f0 -> "this"
	    */
	   public pgNode visit(ThisExpression n, MType argu) {
		   MMethod context = (MMethod) argu;
		   context.MasterClassTemp.TempType = context.MasterClass;
		   return context.MasterClassTemp;
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> "int"
	    * f2 -> "["
	    * f3 -> Expression()
	    * f4 -> "]"
	    */
	   public pgNode visit(ArrayAllocationExpression n, MType argu) {
		   MMethod context = (MMethod) argu;
		   pgTemp ArrayTemp = new pgTemp();
		   pgTemp LenTemp = new pgTemp();
		   
		   context._list.f0.add(new pgMoveStmt(
				   LenTemp,
				   (pgExp) n.f3.accept(this, argu)
				   ));
		   
		   context._list.f0.add(new pgMoveStmt(
				   ArrayTemp,
				   new pgHAllocate(LenTemp)
				   ));
		   
		   context._list.f0.add(new pgHStoreStmt(
				   ArrayTemp,
				   new pgIntegerLiteral(0),
				   LenTemp
				   ));
		   return ArrayTemp;
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> Identifier()
	    * f2 -> "("
	    * f3 -> ")"
	    */
	   public pgNode visit(AllocationExpression n, MType argu) {
		   pgLabel ClassID = (pgLabel) n.f1.accept(this, argu);
		   MClass ClassRef = (MClass) MType.GetTypeByID(new MIdentifier(ClassID.f0, -1));
		   MMethod context = (MMethod) argu;
		   pgTemp _ret = new pgTemp();
		   context._list.f0.add(new pgMoveStmt(
				   _ret,
				   new pgCall(
						   ClassRef.ConstructorLabel
						   )
				   ));
		   _ret.TempType = ClassRef;
		   return _ret;
	   }

	   /**
	    * f0 -> "!"
	    * f1 -> Expression()
	    */
	   public pgNode visit(NotExpression n, MType argu) {
		   return new pgBinOp(
				   OperatorEnum.OP_MINUS,
				   new pgIntegerLiteral(1),
				   (pgExp) n.f1.accept(this, argu)
				   );
	   }

	   /**
	    * f0 -> "("
	    * f1 -> Expression()
	    * f2 -> ")"
	    */
	   public pgNode visit(BracketExpression n, MType argu) {
		   pgExp _ret = (pgExp) n.f1.accept(this, argu);
		   
		   if (_ret instanceof pgCall){
			   MMethod context = (MMethod) argu;
			   pgTemp RetTemp = new pgTemp();
			   RetTemp.TempType = ((pgCall) _ret).RetType;
			   context._list.f0.add(new pgMoveStmt(
					   RetTemp,
					   _ret
					   ));
			   return RetTemp;
		   }
		   
		   return _ret;
	   }
}
