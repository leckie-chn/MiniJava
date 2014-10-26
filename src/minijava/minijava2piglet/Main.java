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
import minijava.visitor.pgVisitor;
import minijava.pgtree.*;
import minijava.symboltable.*;
import minijava.visitor.STBuildVisitor;

public class Main { 
 
	public static pgGoal ProgramGoal;
	
    public static void main(String[] args) {
    	
    	try {
    		// FileInputStream fls = new FileInputStream("tests\\test00.java");
    		
    		// System.setIn(fls);
    		
    		Node root = new MiniJavaParser(System.in).Goal();
    		//Traverse the Abstract Grammar Tree
    		root.accept(new STBuildVisitor() ,null);
    		
    		// bind the symbol table identifier information with its reference
    		// check & bind 
    		MType.RootBind();
    		
    		root.accept(new pgVisitor(), null);
    		
    		Main.ProgramGoal.PrintInstruction(0);
    		
    		
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