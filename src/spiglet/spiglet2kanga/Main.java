package spiglet.spiglet2kanga;

import java.io.FileInputStream;
import java.util.Vector;

import spiglet.ParseException;
import spiglet.SpigletParser;
import spiglet.TokenMgrError;
import spiglet.flowgraph.FlowGraph;
import spiglet.flowgraph.RegisterRef;
import spiglet.syntaxtree.Node;
import spiglet.visitor.SPGVisitor;




public class Main { 
 
    public static void main(String[] args) {
    	try {
    		FileInputStream fls = new FileInputStream("ucla\\spiglet\\LinearSearch.spg");
    		
    		System.setIn(fls);
    		
    		RegisterRef.init();
    		
    		Node root = new SpigletParser(System.in).Goal();
    		
    		//Traverse the Abstract Grammar Tree
    		root.accept(new SPGVisitor() ,null);
    		
    		Vector<FlowGraph> global_table = FlowGraph.GlobalFlowGraphVec;
    		
    		for (FlowGraph graph : FlowGraph.GlobalFlowGraphVec){
    			graph.Init();
    			graph.LiveAnalysis();
    		}
    		
    		
    		System.out.print("Success!");
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