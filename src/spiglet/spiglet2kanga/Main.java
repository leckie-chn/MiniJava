package spiglet.spiglet2kanga;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Vector;

import spiglet.ParseException;
import spiglet.SpigletParser;
import spiglet.TokenMgrError;
import spiglet.flowgraph.FlowGraph;
import spiglet.flowgraph.RegisterRef;
import spiglet.kgtree.kgGoal;
import spiglet.stmtnode.spgTempRef;
import spiglet.syntaxtree.Node;
import spiglet.visitor.SPGVisitor;




public class Main { 
 
    public static void main(String[] args) {
    	try {
    		
    		FileInputStream fls = new FileInputStream("SRegTest.spg");
    		
    		System.setIn(fls);
    		RegisterRef.init();
    		
    		Node root = new SpigletParser(System.in).Goal();
    		
    		//Traverse the Abstract Grammar Tree
    		root.accept(new SPGVisitor() ,null);
    		
    		//Vector<FlowGraph> global_table = FlowGraph.GlobalFlowGraphVec;
    		Map<Integer, spgTempRef> pool = spgTempRef.TempPool;
    		kgGoal GlobalRoot = new kgGoal();
    		for (FlowGraph graph : FlowGraph.GlobalFlowGraphVec){
    			spgTempRef.ClearCount();
    			spgTempRef.ClearRegister();
    			graph.Init();
    			graph.LiveAnalysis();
    			graph.DoRegAllocation();
    			GlobalRoot.f1.add(graph.DoTranslation());
    		}
    		
    		
    		GlobalRoot.PrintInstruction();
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