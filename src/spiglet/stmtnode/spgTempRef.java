package spiglet.stmtnode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import spiglet.flowgraph.InterfereGraphNode;
import spiglet.flowgraph.RegisterRef;


public class spgTempRef implements spgSimpleExp {
	public final int TempNum;
	
	private int UseCount = 0;
	
	public RegisterRef register = null;
	
	public int StackPos = -1;
	
	public spgTempRef(int _number){
		this.TempNum = _number;
	}
	
	private static final Map<Integer, spgTempRef> TempPool = new HashMap<Integer, spgTempRef>();
	
	public static spgTempRef GetTempByNum(int _number){
		spgTempRef _ret = spgTempRef.TempPool.get(_number);
		if (_ret == null){
			_ret = new spgTempRef(_number);
			TempPool.put(_number, _ret);
		}
		_ret.UseCount++;
		return _ret;
	}
	
	public int GetUseCount(){
		return this.UseCount;
	}
	
	public static void ClearCount(){
		for (Map.Entry<Integer, spgTempRef> entry : spgTempRef.TempPool.entrySet())
			entry.getValue().UseCount = 0;
	}
	
	public static void ClearRegister(){
		for (Map.Entry<Integer, spgTempRef> entry : spgTempRef.TempPool.entrySet())
			entry.getValue().register = null;
	}
	
	// interfere graph part
	public InterfereGraphNode attachedNode = null;
	
	
}
