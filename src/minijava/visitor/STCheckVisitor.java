package minijava.visitor;

import minijava.symboltable.MClass;
import minijava.symboltable.MIdentifier;
import minijava.symboltable.MMethod;
import minijava.symboltable.MParameter;
import minijava.symboltable.MType;
import minijava.symboltable.MVar;
import minijava.symboltable.TypeEnum;
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
import minijava.typecheck.CompileError;

public class STCheckVisitor extends GJDepthFirst<MType, MType> {
	 /**
	    * f0 -> MainClass()
	    * f1 -> ( TypeDeclaration() )*
	    * f2 -> <EOF>
	    */
	   public MType visit(Goal n, MType argu) {
		   MType _ret=null;
		   n.f0.accept(this, argu);
		   n.f1.accept(this, argu);
		   return _ret;
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
	    */
	   public MType visit(MainClass n, MType argu) {
		   n.f14.accept(this, MType.MainMethod);
		   return null;
	   }

	   /**
	    * f0 -> ClassDeclaration()
	    *       | ClassExtendsDeclaration()
	    */
	   public MType visit(TypeDeclaration n, MType argu) {
		   n.f0.accept(this, argu);
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
	   public MType visit(ClassDeclaration n, MType argu) {
		   MIdentifier ClassID = n.f1.accept(this, argu).GetID();
		   MClass context = (MClass) MType.GetTypeByID(ClassID);
		   n.f4.accept(this, context);
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
	   public MType visit(ClassExtendsDeclaration n, MType argu) {
		   MIdentifier ClassID = n.f1.accept(this, argu).GetID();
		   MClass context = (MClass) MType.GetTypeByID(ClassID);
		   n.f6.accept(this, context);
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
	    */
	   public MType visit(MethodDeclaration n, MType argu) {
		   MClass context = (MClass) argu;
		   MIdentifier MethodID = n.f2.accept(this, argu).GetID();
		   MMethod method = context.GetMethod(MethodID);
		   n.f8.accept(this, method);
		   MType RetExpr = n.f10.accept(this, method);
		   if (!RetExpr.isInstanceOf(method.GetRetType())){
			   CompileError.ExprMisMatchError(RetExpr, method.GetRetType().toString());
		   }
		   return null;
	   }


	   /**
	    * f0 -> Block()
	    *       | AssignmentStatement()
	    *       | ArrayAssignmentStatement()
	    *       | IfStatement()
	    *       | WhileStatement()
	    *       | PrintStatement()
	    */
	   public MType visit(Statement n, MType argu) {
		   n.f0.accept(this, argu);
		   return null;
	   }

	   /**
	    * f0 -> "{"
	    * f1 -> ( Statement() )*
	    * f2 -> "}"
	    */
	   public MType visit(Block n, MType argu) {
		   n.f1.accept(this, argu);
		   return null;
	   }

	   /**
	    * f0 -> Identifier()
	    * f1 -> "="
	    * f2 -> Expression()
	    * f3 -> ";"
	    */
	   public MType visit(AssignmentStatement n, MType argu) {
		   MMethod context = (MMethod) argu;
		   MIdentifier VarID = n.f0.accept(this, argu).GetID();
		   MVar VarRef = context.GetVar(VarID);
		   if (VarRef == null)	return null;
		   MType ValueExpr = n.f2.accept(this, argu);
		   if (ValueExpr != null && !ValueExpr.isInstanceOf(VarRef.GetVarType())){
			   CompileError.ExprMisMatchError(ValueExpr, VarRef.GetVarType().toString());
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
	   public MType visit(ArrayAssignmentStatement n, MType argu) {
		   MMethod context = (MMethod) argu;
		   MIdentifier ArrayID = n.f0.accept(this, argu).GetID();
		   MVar ArrayVar = context.GetVar(ArrayID);
		   this.ExprTypeCheck(new MType(ArrayVar.GetVarType().GetType(), new MIdentifier(ArrayVar.GetVarType().GetID().GetID(), n.f1.beginLine)), TypeEnum.M_ARRAY);
		   MType IndexExpr = n.f2.accept(this, argu);
		   MType ValueExpr = n.f5.accept(this, argu);
		   this.ExprTypeCheck(IndexExpr, TypeEnum.M_INT);
		   this.ExprTypeCheck(ValueExpr, TypeEnum.M_INT);
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
	   public MType visit(IfStatement n, MType argu) {
		   MType CondExpr = n.f2.accept(this, argu);
		   this.ExprTypeCheck(CondExpr, TypeEnum.M_BOOLEAN);
		   n.f4.accept(this, argu);
		   n.f6.accept(this, argu);
		   return null;
	   }

	   /**
	    * f0 -> "while"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> Statement()
	    */
	   public MType visit(WhileStatement n, MType argu) {
		   MType CondExpr = n.f2.accept(this, argu);
		   this.ExprTypeCheck(CondExpr, TypeEnum.M_BOOLEAN);
		   n.f4.accept(this, argu);
		   return null;
	   }

	   /**
	    * f0 -> "System.out.println"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> ";"
	    */
	   public MType visit(PrintStatement n, MType argu) {
		   MType SubExpr = n.f2.accept(this, argu);
		   this.ExprTypeCheck(SubExpr, TypeEnum.M_INT);
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
	    */
	   public MType visit(Expression n, MType argu) {
		   return n.f0.accept(this, argu);
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "&&"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(AndExpression n, MType argu) {
		   MType LExpr = n.f0.accept(this, argu);
		   MType RExpr = n.f2.accept(this, argu);
		   this.ExprTypeCheck(LExpr, TypeEnum.M_BOOLEAN);
		   this.ExprTypeCheck(RExpr, TypeEnum.M_BOOLEAN);
		   return new MType(TypeEnum.M_BOOLEAN, new MIdentifier("&&", n.f1.beginLine));
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "<"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(CompareExpression n, MType argu) {
		   MType LExpr = n.f0.accept(this, argu);
		   MType RExpr = n.f2.accept(this, argu);
		   this.ExprTypeCheck(LExpr, TypeEnum.M_INT);
		   this.ExprTypeCheck(RExpr, TypeEnum.M_INT);
		   return new MType(TypeEnum.M_BOOLEAN, new MIdentifier("<", n.f1.beginLine));
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "+"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(PlusExpression n, MType argu) {
		   MType LExpr = n.f0.accept(this, argu);
		   MType RExpr = n.f2.accept(this, argu);
		   this.ExprTypeCheck(LExpr, TypeEnum.M_INT);
		   this.ExprTypeCheck(RExpr, TypeEnum.M_INT);
		   return new MType(TypeEnum.M_INT, new MIdentifier("+", n.f1.beginLine));
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "-"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(MinusExpression n, MType argu) {
		   MType LExpr = n.f0.accept(this, argu);
		   MType RExpr = n.f2.accept(this, argu);
		   this.ExprTypeCheck(LExpr, TypeEnum.M_INT);
		   this.ExprTypeCheck(RExpr, TypeEnum.M_INT);
		   return new MType(TypeEnum.M_INT, new MIdentifier("-", n.f1.beginLine));
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "*"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(TimesExpression n, MType argu) {
		   MType LExpr = n.f0.accept(this, argu);
		   MType RExpr = n.f2.accept(this, argu);
		   this.ExprTypeCheck(LExpr, TypeEnum.M_INT);
		   this.ExprTypeCheck(RExpr, TypeEnum.M_INT);
		   return new MType(TypeEnum.M_INT, new MIdentifier("*", n.f1.beginLine));
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "["
	    * f2 -> PrimaryExpression()
	    * f3 -> "]"
	    */
	   public MType visit(ArrayLookup n, MType argu) {
		   MType ArrayExpr = n.f0.accept(this, argu);
		   MType IndexExpr = n.f2.accept(this, argu);
		   this.ExprTypeCheck(ArrayExpr, TypeEnum.M_ARRAY);
		   this.ExprTypeCheck(IndexExpr, TypeEnum.M_INT);
		   return new MType(TypeEnum.M_INT, new MIdentifier("[]", n.f1.beginLine));
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> "length"
	    */
	   public MType visit(ArrayLength n, MType argu) {
		   MType ArrayExpr = n.f0.accept(this, argu);
		   this.ExprTypeCheck(ArrayExpr, TypeEnum.M_ARRAY);
		   return new MType(TypeEnum.M_INT, new MIdentifier(".length", n.f2.beginLine));
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( ExpressionList() )?
	    * f5 -> ")"
	    */
	   public MType visit(MessageSend n, MType _context) {
		   MType Caller = n.f0.accept(this, _context);
		   if (Caller == null) 	return null;
		   if (Caller.GetType() != TypeEnum.M_CLASS){
			   CompileError.CommonError(new MIdentifier(
					   "caller should not be in basic variable type",
					   Caller.GetID().GetLineNo()));
			   return null;
		   }
		   MClass CallerClass = (MClass) MType.GetTypeByID(Caller.GetID());
		   if (CallerClass == null) 	return null;
		   MType MethodID = n.f2.accept(this, _context);
		   MMethod Callee = CallerClass.GetMethod(MethodID.GetID());
		   if (Callee == null) 	return null;
		   MParameter container = new MParameter();
		   container._context = (MMethod) _context;
		   n.f4.accept(this, container);
		   
		   // check for parameter match
		   if (container.ParaList.size() != Callee.ParaTypeList.size()){
			   CompileError.CommonError(new MIdentifier(
					   "Method Call parameter number mismatch, Should be " + Callee.ParaTypeList.size(),
					   n.f3.beginLine));
		   }
		   for (int i = 0; i < container.ParaList.size() && i < Callee.ParaTypeList.size(); i++)
			   if (!container.ParaList.elementAt(i).isInstanceOf(Callee.ParaTypeList.elementAt(i).GetVarType())){
				   CompileError.ExprMisMatchError(container.ParaList.elementAt(i), Callee.ParaTypeList.elementAt(i).GetVarType().GetType().toString());
			   }
		   
		   //return Callee.GetRetType();
		   return new MType(Callee.GetRetType().GetType(), new MIdentifier(Callee.GetRetType().GetID().GetID(), n.f1.beginLine));
	   }

	   /**
	    * f0 -> Expression()
	    * f1 -> ( ExpressionRest() )*
	    */
	   public MType visit(ExpressionList n, MType _container) {
		   MParameter container = (MParameter) _container;
		   container.ParaList.add(n.f0.accept(this, container._context));
		   n.f1.accept(this, _container);
		   return null;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> Expression()
	    */
	   public MType visit(ExpressionRest n, MType _container) {
		   MParameter container = (MParameter) _container;
		   container.ParaList.add(n.f1.accept(this, container._context));
		   return null;
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
	   public MType visit(PrimaryExpression n, MType _context) {
		   MType SubExpr = n.f0.accept(this, _context);
		   // identifier : variable name here
		   if (SubExpr == null) 	return null;
		   if (SubExpr.GetType() == TypeEnum.M_BASIC){
			   MMethod context = (MMethod) _context;
			   MVar VarRef = context.GetVar(SubExpr.GetID());
			   if (VarRef == null)	return null;
			   return new MType(VarRef.GetVarType().GetType(), new MIdentifier(VarRef.GetVarType().GetID().GetID(), SubExpr.GetID().GetLineNo()));
		   }
		   return SubExpr;
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public MType visit(IntegerLiteral n, MType argu) {
		   return new MType(TypeEnum.M_INT, new MIdentifier(n.f0.tokenImage, n.f0.beginLine));
	   }

	   /**
	    * f0 -> "true"
	    */
	   public MType visit(TrueLiteral n, MType argu) {
		   return new MType(TypeEnum.M_BOOLEAN, new MIdentifier(n.f0.tokenImage, n.f0.beginLine));
	   }

	   /**
	    * f0 -> "false"
	    */
	   public MType visit(FalseLiteral n, MType argu) {
		   return new MType(TypeEnum.M_BOOLEAN, new MIdentifier(n.f0.tokenImage, n.f0.beginLine));
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public MType visit(Identifier n, MType argu) {
		   return new MType(TypeEnum.M_BASIC, new MIdentifier(n.f0.tokenImage, n.f0.beginLine));
	   }

	   /**
	    * f0 -> "this"
	    * @param MMethod _context
	    * @return MType, denotes the type
	    */
	   public MType visit(ThisExpression n, MType _context) {
		   MMethod context = (MMethod) _context;
		   return new MType(TypeEnum.M_CLASS, new MIdentifier(context.MasterClass.GetID().GetID(), n.f0.beginLine));
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> "int"
	    * f2 -> "["
	    * f3 -> Expression()
	    * f4 -> "]"
	    */
	   public MType visit(ArrayAllocationExpression n, MType argu) {
		   MType IndexExpr = n.f3.accept(this, argu);
		   if (!this.ExprTypeCheck(IndexExpr, TypeEnum.M_INT))
			   return null;
		   return new MType(TypeEnum.M_ARRAY);
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> Identifier()
	    * f2 -> "("
	    * f3 -> ")"
	    * @return MType
	    */
	   public MType visit(AllocationExpression n, MType argu) {
		   MIdentifier ClassID = n.f1.accept(this, argu).GetID();
		   if (MType.GetTypeByID(ClassID) == null)
			   return null;
		   return new MType(TypeEnum.M_CLASS, ClassID);
	   }

	   /**
	    * f0 -> "!"
	    * f1 -> Expression()
	    */
	   public MType visit(NotExpression n, MType argu) {
		   MType SubExpr = n.f1.accept(this, argu);
		   if (!this.ExprTypeCheck(SubExpr, TypeEnum.M_BOOLEAN))
			   return null;
		   return SubExpr;
	   }

	   /**
	    * f0 -> "("
	    * f1 -> Expression()
	    * f2 -> ")"
	    * @return MType
	    */
	   public MType visit(BracketExpression n, MType argu) {
		   return n.f1.accept(this, argu);
	   }
	   
	   private boolean ExprTypeCheck(MType expr, TypeEnum idealType){
		   if (expr == null) return false;
		   if (expr.GetType() != idealType){
			   CompileError.ExprMisMatchError(expr, idealType.toString());
			   return false;
		   }
		   return true;
	   }

}
