package com.keel.utils.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeUtils {
	
	/*时间戳转化为Sting*/
	public static String timestampToString(final long ts){
		SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String t = format.format(ts);
		
		return t;
	}
	
	/*String转化为时间戳*/
	public static long StringToTimestamp(final String t) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		Date date = format.parse(t);

		return date.getTime();
	}
	
	/**时间戳转化为Date*/
	public static Date timestamp2Date(final long ts){
		return new Date(ts);
	}
	
	public static String Date2String(final Date date){
		return timestampToString(date.getTime());
	}
}
