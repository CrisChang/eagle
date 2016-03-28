package com.poison.eagle.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BussinessLogUtils {

	/**
	 * 业务日志的各种日志打印
	 */
	private static final  Log BUSINESS_LOG = LogFactory.getLog("COMMON.BUSINESS");
	
	/**
	 * 
	 * <p>Title: searchBookLog</p> 
	 * <p>Description: 热搜书的日志打印</p> 
	 * @author :changjiang
	 * date 2015-5-21 下午5:40:37
	 * @param bookId
	 * @param bookName
	 * @param userId
	 */
	public static void searchBookLog(long bookId,String bookName,String bookType,long userId){
		/*Map<String, Object> bussinessMap = new HashMap<String, Object>();
		bussinessMap.put("method", "search_book");
		bussinessMap.put("bookId", bookId);
		bussinessMap.put("bookName", bookName);
		bussinessMap.put("bookType", bookType);
		bussinessMap.put("userId", userId);*/
		 //StringUtils.putMapToJson(bussinessMap);
		StringBuffer sb = new StringBuffer("{\"method\":\"search_book\",");
		sb.append("\"bookId\":\""+bookId+"\",");
		sb.append("\"bookType\":\""+bookType+"\",");
		sb.append("\"bookName\":\""+bookName+"\",");
		sb.append("\"userId\":\""+userId+"\"");
		sb.append("}");
		String resultStr = sb.toString();
		//System.out.println(resultStr);
		BUSINESS_LOG.info(resultStr);
	}
	
	/**
	 * 
	 * <p>Title: searchMovieLog</p> 
	 * <p>Description: 热搜电影的日志</p> 
	 * @author :changjiang
	 * date 2015-5-21 下午6:05:46
	 * @param movieId
	 * @param movieName
	 * @param movieType
	 * @param userId
	 */
	public static void searchMovieLog(long movieId,String movieName,String movieType,long userId){
		/*Map<String, Object> bussinessMap = new HashMap<String, Object>();
		bussinessMap.put("method", "search_movie");
		bussinessMap.put("movieId", movieId);
		bussinessMap.put("movieName", movieName);
		bussinessMap.put("movieType", movieType);
		bussinessMap.put("userId", userId);*/
		StringBuffer sb = new StringBuffer("{\"method\":\"search_movie\",");
		sb.append("\"movieId\":\""+movieId+"\",");
		sb.append("\"movieType\":\""+movieType+"\",");
		sb.append("\"movieName\":\""+movieName+"\",");
		sb.append("\"userId\":\""+userId+"\"");
		sb.append("}");
		String resultStr = sb.toString();
		//System.out.println(resultStr);
		BUSINESS_LOG.info(resultStr);
	}

	/**
	 * 查看书评的日志
	 */
	public static void viewBkCommentLog(long bkCommentId,int bookId,long userId,String bookType,String resourceType){
		StringBuffer sb = new StringBuffer("{\"method\":\"view_bkcomment\",");
		sb.append("\"bkCommentId\":\""+bkCommentId+"\",");
		sb.append("\"bookId\":\""+bookId+"\",");
		sb.append("\"bookType\":\""+bookType+"\",");
		sb.append("\"resourceType\":\""+resourceType+"\",");
		sb.append("\"userId\":\""+userId+"\"");
		sb.append("}");
		String resultStr = sb.toString();
		//System.out.println(resultStr);
		BUSINESS_LOG.info(resultStr);
	}

	/**
	 * 查看影评的日志
	 * @param mvCommentId
	 * @param movieId
	 * @param userId
	 * @param resourceType
	 */
	public static void viewMvCommentLog(long mvCommentId,long movieId,long userId,String resourceType){
		StringBuffer sb = new StringBuffer("{\"method\":\"view_mvcomment\",");
		sb.append("\"mvCommentId\":\""+mvCommentId+"\",");
		sb.append("\"movieId\":\""+movieId+"\",");
		sb.append("\"resourceType\":\""+resourceType+"\",");
		sb.append("\"userId\":\""+userId+"\"");
		sb.append("}");
		String resultStr = sb.toString();
		//System.out.println(resultStr);
		BUSINESS_LOG.info(resultStr);
	}

	/**
	 * 查看
	 * @param diaryId
	 * @param userId
	 * @param resourceType
	 */
	public static void viewDiaryLog(long diaryId,long userId,String resourceType){
		StringBuffer sb = new StringBuffer("{\"method\":\"view_diary\",");
		sb.append("\"diaryId\":\""+diaryId+"\",");
		sb.append("\"resourceType\":\""+resourceType+"\",");
		sb.append("\"userId\":\""+userId+"\"");
		sb.append("}");
		String resultStr = sb.toString();
		//System.out.println(resultStr);
		BUSINESS_LOG.info(resultStr);
	}

}
