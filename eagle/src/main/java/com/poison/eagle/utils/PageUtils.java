package com.poison.eagle.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 读取配置文件获得每页显示数的工具类
 * @author 陈呈泰
 */
public class PageUtils {

	private static String pageSizeStr ;
	
	/**
	 * 读取配置文件，获取pageSize
	 * @return pageSize
	 */
	public static int getPageSize(String keys) {
		
		InputStream ins = null;
		try {
			ins = PageUtils.class.getResourceAsStream("/com/poison/eagle/utils/page.properties"); 
			Properties pro = new Properties();
			pro.load(ins);
			pageSizeStr = pro.getProperty(keys);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(pageSizeStr== null || pageSizeStr == "0"){
			int pageSize = Integer.parseInt(pageSizeStr);
			if (pageSize<1) {
				//如果是最终为空，则给默认分页值
				pageSizeStr = CommentUtils.DEFAULT_PAGESIZE;
			}
		}
		return Integer.parseInt(pageSizeStr);
	}
	
	/**
	 * 根据pagesize和pageno计算出开始记录数
	 */
	public static long getRecordStart(int pagesize,long pageno){
		long start = (pageno-1)*pagesize;
		return start;
	}
}
