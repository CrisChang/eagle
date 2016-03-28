package com.poison.eagle.utils;

public class AsciiUTils {
	public static String stringToAscii(String value)  
	 {  
	     StringBuffer sbu = new StringBuffer();  
	     char[] chars = value.toCharArray();   
	     for (int i = 0; i < chars.length; i++) {  
	         if(i != chars.length - 1)  
	         {  
	             sbu.append((int)chars[i]).append("-");  
	         }  
	         else {  
	             sbu.append((int)chars[i]);  
	         }  
	     }  
	     return sbu.toString();  
	 }  
}
