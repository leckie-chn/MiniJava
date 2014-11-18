package spiglet.stmtnode;

import java.util.HashMap;
import java.util.Map;


public class spgTempRef implements spgSimpleExp {
	public final int TempNum;
	
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
		return _ret;
	}
	
}
