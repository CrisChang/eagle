package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.model.ResReport;

public class SensitiveManager extends BaseManager {	
	private static final int IS_DELETE = 0;
	private static final int SENSITIVE_LEVEL = 1;
	
	private MyBkFacade myBkFacade;
	
	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}
	public String checkSensitive(String str, long userId, long resourceId, String type) {				
		//将字符串中的大写字母全部转换为小写字母
//		String src = StringUtils.lowerCase(str);
		//查询所有敏感词汇并分组
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isDelete", IS_DELETE);
		map.put("sensitiveLevel", SENSITIVE_LEVEL);
		List<String> list = myBkFacade.selectSensitiveWord(map);		
		Map<Character,List<String>> wordMap = new HashMap<Character,List<String>>();
        for (String s:list){
            char c=s.charAt(0);
            List<String> strs=wordMap.get(c);
            if (strs==null){
                strs=new ArrayList<String>();
                wordMap.put(c,strs);
            }
            strs.add(s);
        }
        //匹配敏感词汇并用*号替代敏感词汇，另外记录敏感词汇
        StringBuilder strb=new StringBuilder();
        for (int i=0;i<str.length();i++){
            char c=str.charAt(i);
            String find=null;
            if (wordMap.containsKey(c)){
                List<String> words=wordMap.get(c);
                for (String s:words){
                    String temp=str.substring(i,(s.length()<=(str.length()-i))?i+s.length():i);
                    if (s.equals(temp)){
                        find=s;
                        break;
                    }
                }
            }
            if (find!=null){
				ResReport resReport = myBkFacade.insertResReport(userId, resourceId, type, find);	
				int flagint = resReport.getFlag();
				if (flagint != ResultUtils.SUCCESS) {
					return str;
				}
            	int length = find.length();
            	for(int j=1;j<=length;j++){
            		strb.append("*");
            	}
                i+=(find.length()-1);
            } else {
                strb.append(c);
            }
        }
        
		return strb.toString();
	}



    public String checkSensitive(String str) {
        //将字符串中的大写字母全部转换为小写字母
//		String src = StringUtils.lowerCase(str);
        //查询所有敏感词汇并分组
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isDelete", IS_DELETE);
        map.put("sensitiveLevel", SENSITIVE_LEVEL);
        List<String> list = myBkFacade.selectSensitiveWord(map);
        Map<Character,List<String>> wordMap = new HashMap<Character,List<String>>();
        for (String s:list){
            char c=s.charAt(0);
            List<String> strs=wordMap.get(c);
            if (strs==null){
                strs=new ArrayList<String>();
                wordMap.put(c,strs);
            }
            strs.add(s);
        }
        //匹配敏感词汇并用*号替代敏感词汇，另外记录敏感词汇
        StringBuilder strb=new StringBuilder();
        for (int i=0;i<str.length();i++){
            char c=str.charAt(i);
            String find=null;
            if (wordMap.containsKey(c)){
                List<String> words=wordMap.get(c);
                for (String s:words){
                    String temp=str.substring(i,(s.length()<=(str.length()-i))?i+s.length():i);
                    if (s.equals(temp)){
                        find=s;
                        break;
                    }
                }
            }
            if (find!=null){
//                ResReport resReport = myBkFacade.insertResReport(userId, resourceId, type, find);
//                int flagint = resReport.getFlag();
//                if (flagint != ResultUtils.SUCCESS) {
//                    return str;
//                }
                int length = find.length();
                for(int j=1;j<=length;j++){
                    strb.append("*");
                }
                i+=(find.length()-1);
            } else {
                strb.append(c);
            }
        }

        return strb.toString();
    }
}
