package minijava.minijava2piglet;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;

import minijava.MiniJavaParser;
import minijava.ParseException;
import minijava.TokenMgrError;
import minijava.syntaxtree.Node;
import minijava.visitor.GJDepthFirst;
import minijava.pgtree.*;
import minijava.symboltable.*;
import minijava.visitor.STBuildVisitor;

public class Main { 
 
    public static void main(String[] args) {
    	
    	try {
    		FileInputStream fls = new FileInputStream("tests\\Factorial.java");
    		PrintStream pgOut = new PrintStream(new BufferedOutputStream(new FileOutputStream("test.pg")));
    		PrintStream SymbolOut = new PrintStream(new BufferedOutputStream(new FileOutputStream("Symbols.txt")));
    		
    		System.setIn(fls);
    		
    		Node root = new MiniJavaParser(System.in).Goal();
    		//Traverse the Abstract Grammar Tree
    		root.accept(new STBuildVisitor() ,null);
    		
    		MType.RootBind();
    		
    		//System.setOut(SymbolOut);
    		
    		MType.RootSymbolTableDump();
    		
    		//System.setOut(pgOut);
    		System.out.print("\n\n\t\t\tpiglet code\n\n");
    		
    		
    		pgStmtList _list = new pgStmtList();
    		
    		pgGoal ProgramGoal = new pgGoal(_list);	
    		
    		pgProcedure Global_Init = MType.Get_Global_Init();
    		
    		ProgramGoal.AddProcedure(Global_Init);
    		
    		_list.f0.add(new pgMoveStmt(
    				MType.GlobalTableTemp,
    				new pgCall(
    						Global_Init.f0
    						)
    				));
    		
    		for (Map.Entry<String, MType> entry : MType.RootSymbolTable.entrySet())
    			if (entry.getValue() instanceof MClass)
    				ProgramGoal.AddProcedure(((MClass) entry.getValue()).GenConstructorCode());
    		
    		ProgramGoal.PrintInstruction(0);
    		
    		
    	}
    	catch(TokenMgrError e){
    		//Handle Lexical Errors
    		e.printStackTrace();
    	}
    	catch (ParseException e){
    		//Handle Grammar Errors
    		e.printStackTrace();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
}