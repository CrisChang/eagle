package com.poison.store.ext.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BookRelationUtil {

	private static final  Log LOG = LogFactory.getLog(BookRelationUtil.class);
	/**
	 * 
	 * <p>Title: getBookTime</p> 
	 * <p>Description: 得到书的发布时间</p> 
	 * @author :changjiang
	 * date 2015-1-30 下午6:39:31
	 * @param bookTime
	 * @return
	 */
	public static long getBookTime(String bookTime){
		long finalForamatDate =31507200000L;
		if(null==bookTime||"".equals(bookTime)){
			return finalForamatDate;
		}
		SimpleDateFormat dateForm = null;
		if(bookTime.matches("[A-z]{3,}, [0-9]{4}")){//September, 1996
			dateForm = new SimpleDateFormat("MMM, yyyy",Locale.US);
		}else if(bookTime.matches("[A-z]{3,} [0-9]{1,2}, ?[0-9]{4}")){//September 8, 1995
			dateForm = new SimpleDateFormat("MMM dd, yyyy",Locale.US);
		}else if(bookTime.matches("[A-z]{3,} [0-9]{4}")){//October 2001
			dateForm = new SimpleDateFormat("MMM yyyy",Locale.US);
		}else if(bookTime.matches("[0-9]{2}/[0-9]{1,2}/[0-9]{1,2}")){//94/07/04
			dateForm = new SimpleDateFormat("yy/MM/dd",Locale.US);
		}else if(bookTime.matches("[0-9]{1,2} [A-z]{3,}, [0-9]{4}")){//24 April, 2001
			dateForm = new SimpleDateFormat("dd MMM, yyyy",Locale.US);
		}else if(bookTime.matches("[0-9]{1,2}-[A-z]{3,}-[0-9]{1,2}")){//24-Mar-03
			dateForm = new SimpleDateFormat("dd-MMM-yy",Locale.US);
		}else if(bookTime.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}")){//25/2/2002
			dateForm = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
		}else if(bookTime.matches("[0-9]{4}-[0-9]{1,2}")){//2017-7
			dateForm = new SimpleDateFormat("yyyy-MM",Locale.US);
		}else if(bookTime.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}")){//2015-6-18
			dateForm = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		}else if(bookTime.matches("[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日")){//2007年11月5日
			dateForm = new SimpleDateFormat("yyyy年MM月dd",Locale.US);
		}else if(bookTime.matches("[0-9]{7,8}")){//20071206
			dateForm = new SimpleDateFormat("yyyyMMdd",Locale.US);
		}else if(bookTime.matches("[0-9]{4}/[0-9]{1,2}")){//2007/9
			dateForm = new SimpleDateFormat("yyyy/MM",Locale.US);
		}else if(bookTime.matches("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}")){//2007/07/01
			dateForm = new SimpleDateFormat("yyyy/MM/dd",Locale.US);
		}else if(bookTime.matches("[0-9]{4}\\.[0-9]{1,2}")){//2007.7
			dateForm = new SimpleDateFormat("yyyy.MM",Locale.US);
		}else if(bookTime.matches("[0-9]{4}年[0-9]{1,2}月.*")){//2005年4月,2005年7月第二版，2005年5月第1版第1次印刷
			bookTime = bookTime.substring(0,bookTime.indexOf("月")+1);
			dateForm = new SimpleDateFormat("yyyy年MM月",Locale.US);
		}else if(bookTime.matches("[0-9]{4}年")){//2005年
			dateForm = new SimpleDateFormat("yyyy年",Locale.US);
		}
		Date date = null;
		try {
			if(dateForm != null){
				date = dateForm.parse(bookTime);
				finalForamatDate = date.getTime();
			}else{
				finalForamatDate = 31507200000L;
			}
		} catch (ParseException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			finalForamatDate = 31507200000L;
		}
		return finalForamatDate;
	}
}
