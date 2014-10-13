package minijava.typecheck;

import java.io.FileInputStream;

import minijava.MiniJavaParser;
import minijava.ParseException;
import minijava.TokenMgrError;
import minijava.symboltable.MType;
import minijava.syntaxtree.Node;
import minijava.visitor.STBuildVisitor;


public class Main { 
 
    public static void main(String[] args) {
    	try {
    		FileInputStream fls = new FileInputStream("tests\\test01.java");
    		System.setIn(fls);
    		Node root = new MiniJavaParser(System.in).Goal();

    		//Traverse the Abstract Grammar Tree
    		root.accept(new STBuildVisitor(), null);
    		MType.RootBind();
    		MType.RootSymbolTableDump();
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