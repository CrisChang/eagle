package com.poison.eagle.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final String dateformat = "yyyy-MM-dd";

	public static final String timeformat = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 
	 * <p>
	 * Title: format
	 * </p>
	 * <p>
	 * Description: 转换日期
	 * </p>
	 * 
	 * @author :changjiang date 2014-11-26 上午11:23:19
	 * @param timeMillis
	 * @param format
	 * @return
	 */
	public static String format(Long timeMillis, String format) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(timeMillis);
	}

	/**
	 * 将日期转换为long型
	 * 
	 * @return
	 */
	public static long formatLong(String recDateStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long formatlong = 0;
		try {
			Date date = sdf.parse(recDateStr);
			formatlong = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatlong;
	}
	/**
	 * 获取今天的年月日日期，例如:2015-09-22
	 * @param timeMillis
	 * @param format
	 * @return
	 */
	public static String getDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
		return sdf.format(time);
	}

	/**
	 * 获取当前月的开始时间毫秒值，比如：2015年3月1日0时0分0秒的时间毫秒值
	 */
	public static long getTheMonthStartTime() {
		// 获取当前月第一天：
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		long time = c.getTime().getTime() / 1000 * 1000;
		return time;
	}

	/**
	 * 获取当前月的结束时间毫秒值，比如：2015年3月31日23时59分59秒的时间毫秒值
	 */
	public static long getTheMonthEndTime() {
		// 获取当前月最后一天
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		long time = ca.getTime().getTime() / 1000 * 1000;
		return time;
	}

	/**
	 * 获取当天开始时间的毫秒值,比如：2015年5月15日0时0分0秒
	 * 
	 * @Title: getTodayStartTime
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-15 上午11:09:55
	 * @param @return
	 * @return long
	 * @throws
	 */
	public static long getTodayStartTime() {
		try {
			Calendar ca = Calendar.getInstance();
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			long time = ca.getTime().getTime() / 1000 * 1000;
			return time;
		} catch (Exception e) {
			e.printStackTrace();
			return System.currentTimeMillis();
		}
	}

	// 获取今天是几号
	public static int getTodayDay(String datestr) {
		String date = "";
		if (datestr != null && datestr.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
			date = datestr;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
			date = sdf.format(System.currentTimeMillis());
		}
		// 2015-07-14
		String day = date.substring(8, 10);
		return Integer.parseInt(day);
	}
	
	/**
	 * 获取指定月的开始时间毫秒值，比如：2015年3月1日0时0分0秒的时间毫秒值
	 */
	public static long getTheMonthStartTime(int year,int month){
		//获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        //c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        long time = c.getTime().getTime()/1000*1000;
        return time;
	}
	/**
	 * 获取指定月的结束时间毫秒值，比如：2015年3月31日23时59分59秒的时间毫秒值
	 */
	public static long getTheMonthEndTime(int year,int month){
		//获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, year);
        ca.set(Calendar.MONTH, month-1);
        ca.set(Calendar.DATE, 1);  
        //ca.roll(Calendar.DATE, -1);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        long time = ca.getTime().getTime()/1000*1000;
        return time;
	}
	 /** 
     * 根据年 月 获取对应的月份 天数 
     * */  
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);  
        a.roll(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    }

	/**
	 * 获取 今天是周几
	 * 
	 * @author zhangqi
	 * @param date
	 * @return
	 */
	public static String getWeekDay(String dateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
			Date date = sdf.parse(dateStr);
			String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0)
				w = 0;
			return weekDays[w];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 比较时间是否大于今天
	 * 
	 * @author zhangqi
	 * @param splitDate
	 *            sdf:yyyy-mm-dd
	 * @return 大于今天 true else false
	 */
	public static boolean compareNow(String splitDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
			Date date = sdf.parse(splitDate);
			Date today = new Date();
			int compareTime = date.compareTo(today);
			if (compareTime == -1 || compareTime == 0) {
				return false;
			} else if (compareTime == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 *
	 * @param inputTime
	 * @param systime
	 * @return
	 */
	public static boolean compareDateNow(long inputTime,long systime){
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
		Date inputDate = new Date(inputTime);
		Date sysDate = new Date(systime);
		String inputStr = sdf.format(inputDate);
		String sysStr = sdf.format(sysDate);
		boolean flag = inputStr.equals(sysStr);
		return flag;
	}
	// public static void main(String[] args) {
	// String date2 = "2016";
	// Double releaseDateDouble = CheckParams.getRealeaseDate(date2);
	// String splitDate =
	// CheckParams.getSplitRealeaseDate(String.valueOf(releaseDateDouble.intValue()));
	// System.out.println(compareNow(splitDate));
	// }
}
