package com.poison.eagle.utils;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.poison.eagle.manager.BookManager;

public class StringUtils {

	private static final  Log LOG = LogFactory.getLog(StringUtils.class);
	
	/**
	 * 
	 * <p>Title: removeHTMLLable</p> 
	 * <p>Description: 去除html标签</p> 
	 * @author :changjiang
	 * date 2014-8-7 下午4:16:17
	 * @param str
	 * @return
	 */
	public static String removeHTMLLable(String str) {
		str = stringReplace(str, "\\s", "");// 去掉页面上看不到的字符
		str = stringReplace(str, "<br ?/?>", "\n");// 去<br><br />
		str = stringReplace(str, "<([^<>]+)>", "");// 去掉<>内的字符
		str = stringReplace(str, "&nbsp;", " ");// 替换空格
		str = stringReplace(str, "&(\\S)(\\S?)(\\S?)(\\S?);", "");// 
		return str;
	}
	
	public static String stringReplace(String str, String sr, String sd) {
		String regEx = sr;
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		str = m.replaceAll(sd);
		return str;
	}
	
	/**
	 * 
	 * <p>Title: getFirstAuthorName</p> 
	 * <p>Description: 当出现多个作者名时匹配第一个作者名称</p> 
	 * @author :changjiang
	 * date 2014-8-17 上午11:54:26
	 * @param authorName
	 * @return
	 */
	public static String getFirstAuthorName(String authorName){
		if(!authorName.contains(",")&&!authorName.contains("／")){
			return authorName;
		}
		String str = "";
		try{
			Pattern p = Pattern.compile("([\\S]+)(,|／)");
			Matcher m = p.matcher(authorName);
			if(m.find()){
				str = m.group(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
			str = authorName;
		}
		return str;
	}
	
	/**
	 * 
	 * <p>Title: isType</p> 
	 * <p>Description: 判断是否为该类型</p> 
	 * @author :changjiang
	 * date 2014-10-31 下午2:17:06
	 * @param content
	 * @param type
	 * @return
	 */
	public static boolean isType(String content,String type){
		if(content.contains(type)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符窜是否是正整数类型
	 * @Title: isInteger 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-3-28 下午3:22:58
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isInteger(String str){
		if(str!=null && str.matches("^[0-9]+$")){
			return true;
		}
		return false;
	}
	/**
	 * 判断字符窜是否是手机号码
	 * @Title: isMobile 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-20 下午3:53:53
	 * @param @param str
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isMobile(String str){
		if(str==null || str.length()!=11){
			return false;
		}
		String mobileRegex = "^1[0-9]{10}$";
		if(str.matches(mobileRegex)){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * <p>Title: putMapToJson</p> 
	 * <p>Description: 将map类型转换为json</p> 
	 * @author :changjiang
	 * date 2015-5-21 下午6:00:45
	 * @param map
	 * @return
	 */
	public static String putMapToJson(Map<String, Object> map) {
		if(null==map||map.isEmpty()){
			return "";
		}	
		
		String resultStr = "";
		ObjectMapper objectMapper  = new ObjectMapper();
			
		objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false) ;
//			objectMapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
//			objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
		try {
			resultStr = objectMapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (JsonMappingException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (IOException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return resultStr;
	}
	
	/**
	 * 
	 * <p>Title: putObjectToJson</p> 
	 * <p>Description: 将对象转为json</p> 
	 * @author :changjiang
	 * date 2015-5-28 下午6:10:25
	 * @param map
	 * @return
	 */
	public static String putObjectToJson(Object map) {
		if(null==map){
			return "";
		}	
		
		String resultStr = "";
		ObjectMapper objectMapper  = new ObjectMapper();
			
		objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false) ;
//			objectMapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
//			objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
		try {
			resultStr = objectMapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (JsonMappingException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (IOException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return resultStr;
	}
	

	/**
	 * 
	 * @param praiseScore
	 *            正方分数
	 * @param lowScore
	 *            反方分数
	 * @author zhangqi
	 * @return String[0]=正方百分比，String[1]=反方百分比
	 * 
	 */
	public static String[] getPercent(int praiseScore, int lowScore) {
		String percent[] = new String[2];
		try {
			float score = lowScore + praiseScore;
			NumberFormat nt = NumberFormat.getPercentInstance();
			// 设置百分数精确度2即保留两位小数
			nt.setMinimumFractionDigits(0);
			float lowP = lowScore / score;
			String lowStr = nt.format(lowP);

			float praiseP = praiseScore / score;
			String praiseStr = nt.format(praiseP);
			percent[0] = praiseStr;
			percent[1] = lowStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percent;
	}
}
