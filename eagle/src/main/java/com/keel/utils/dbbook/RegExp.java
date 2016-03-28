package com.keel.utils.dbbook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {

	/**
	 * 判断字符串是否是数字
	 * @param str 
	 * @return boolean
	 */
	public boolean isNumeric(String str)
    {
          Pattern pattern = Pattern.compile("[0-9]*");
          Matcher isNum = pattern.matcher(str);
          if( !isNum.matches() )
          {
                return false;
          }
          return true;
    }
	/**
	 * 保留数字
	 * @param temp
	 * @return String
	 */
	public String reNumeric(String temp){
		//String temp = "你好ABC-abc_ 123";
	      StringBuffer Stringtest = new StringBuffer();
	      for(char c:temp.toCharArray())
	         if(c>='0' && c<='9')
	        	 Stringtest.append(c);
	      String temps = Stringtest.toString();
	      return temps;
	}
}
