package minijava.visitor;

import minijava.symboltable.MClass;
import minijava.symboltable.MIdentifier;
import minijava.symboltable.MMethod;
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

public class STBuildVisitor extends GJDepthFirst<MType, MType> {
	   /**
	    * f0 -> MainClass()
	    * f1 -> ( TypeDeclaration() )*
	    * f2 -> <EOF>
	    */
	   public MType visit(Goal n, MType argu) {
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
	    */
	   public MType visit(MainClass n, MType argu) {
		   MIdentifier ClassID = n.f1.accept(this, null).GetID();
		   MIdentifier MethodID = new MIdentifier("main", n.f5.beginLine);
		   MClass MainClass = new MClass(ClassID);
		   MType.InsertClass(MainClass);
		   MType.MainMethod = new MMethod(MethodID, MainClass);
		   n.f14.accept(this, MType.MainMethod);
		   MainClass.InsertMethod(MType.MainMethod);
		   return null;
	   }

	   /**
	    * f0 -> ClassDeclaration();
	    *       | ClassExtendsDeclaration()
	    */
	   public MType visit(TypeDeclaration n, MType argu) {
	      return n.f0.accept(this, argu);
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
		   MClass SelfRef = new MClass(ClassID);
		   n.f3.accept(this, SelfRef);
		   n.f4.accept(this, SelfRef);
		   MType.InsertClass(SelfRef);
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
		   MIdentifier ParentID = n.f3.accept(this, argu).GetID();
		   MClass SelfRef = new MClass(ClassID);
		   SelfRef.SetParent(ParentID);
		   n.f5.accept(this, SelfRef);
		   n.f6.accept(this, SelfRef);
		   MType.InsertClass(SelfRef);
		   return null;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    * f2 -> ";"
	    * @param context : may be class or method
	    * @return null
	    */
	   public MType visit(VarDeclaration n, MType context) {
		   MType VarType = n.f0.accept(this, context);
		   MIdentifier VarID = n.f1.accept(this, context).GetID();
		   MVar NewVar = new MVar(VarID);
		   NewVar.SetVarType(VarType.GetID());
		   if (context.GetType() == TypeEnum.M_CLASS){
			   ((MClass)context).InsertVar(NewVar);
		   }
		   else if (context.GetType() == TypeEnum.M_METHOD){
			   ((MMethod)context).InsertVar(NewVar);
		   }
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
	    * @param context : master class
	    */
	   public MType visit(MethodDeclaration n, MType _context) {
		   MClass context = (MClass)_context;
		   MType RetType = n.f1.accept(this, context);
		   MIdentifier MethodID = n.f2.accept(this, context).GetID();
		   MMethod NewMethod = new MMethod(MethodID, context);
		   NewMethod.SetRetType(RetType.GetID());
		   context.InsertMethod(NewMethod);
		   n.f4.accept(this, NewMethod);
		   n.f7.accept(this, NewMethod);
		   return null;
	   }

	   /**
	    * f0 -> FormalParameter()
	    * f1 -> ( FormalParameterRest() )*
	    * @param context : master Method
	    * @return null
	    */
	   public MType visit(FormalParameterList n, MType context) {
		   n.f0.accept(this, context);
		   n.f1.accept(this, context);
		   return null;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    * @param MType _context, method
	    * @return null
	    */
	   public MType visit(FormalParameter n, MType _context) {
		   MMethod context = (MMethod)_context;
		   MType ParaType = n.f0.accept(this, context);
		   MIdentifier ParaID = n.f1.accept(this, null).GetID();
		   MVar NewPara = new MVar(ParaID);
		   NewPara.SetVarType(ParaType.GetID());
		   context.AddParaItem(NewPara);
		   return null;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> FormalParameter()
	    */
	   public MType visit(FormalParameterRest n, MType _context) {
	      n.f1.accept(this, _context);
	      return null;
	   }

	   /**
	    * f0 -> ArrayType()
	    *       | BooleanType()
	    *       | IntegerType()
	    *       | Identifier()
	    * @return MType .ID denotes the actual type
	    */
	   public MType visit(Type n, MType argu) {
		   MType _ret = n.f0.accept(this, argu);
		   if (_ret.GetType() == TypeEnum.M_BASIC)
			   return new MType(TypeEnum.M_CLASS, _ret.GetID());
		   else
			   return _ret;
	   }

	   /**
	    * f0 -> "int"
	    * f1 -> "["
	    * f2 -> "]"
	    * @see visit(Type)
	    */
	   public MType visit(ArrayType n, MType argu) {
		   return new MType(TypeEnum.M_ARRAY, new MIdentifier("int[]", n.f0.beginLine));
	   }

	   /**
	    * f0 -> "boolean"
	    * @see visit(Type)
	    */
	   public MType visit(BooleanType n, MType argu) {
		   return new MType(TypeEnum.M_BOOLEAN, new MIdentifier(n.f0.tokenImage, n.f0.beginLine));
	   }

	   /**
	    * f0 -> "int"
	    * @see visit(Type)
	    */
	   public MType visit(IntegerType n, MType argu) {
		   return new MType(TypeEnum.M_INT, new MIdentifier(n.f0.tokenImage, n.f0.beginLine));
	   }
	   

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public MType visit(Identifier n, MType argu) {
		   return new MType(TypeEnum.M_BASIC, new MIdentifier(n.f0.tokenImage, n.f0.beginLine));
	   }


}
