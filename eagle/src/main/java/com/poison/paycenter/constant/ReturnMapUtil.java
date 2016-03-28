package com.poison.paycenter.constant;

import java.util.HashMap;
import java.util.Map;

import com.poison.eagle.utils.ResultUtils;

/**
 * @author Administrator
 *
 */
public class ReturnMapUtil {

	public static Map<String, Object> getSuccessMap(){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("flag", ResultUtils.SUCCESS);	
		return returnMap;		
	}
	public static Map<String, Object> getErrorMap(int errorCode) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("flag", errorCode);
		return returnMap;
	}
}
