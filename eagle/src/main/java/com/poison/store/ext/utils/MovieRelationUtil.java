package com.poison.store.ext.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.poison.store.client.impl.BkFacadeImpl;

public class MovieRelationUtil {

	private static final  Log LOG = LogFactory.getLog(MovieRelationUtil.class);
	
	/**
	 * 总票房
	 */
	private static final String TOTAL_TICKET = "totalTicket";
	
	/**
	 * 大陆票房
	 */
	private static final String MAINLAND_TICKET = "mainlandTicket";
	
	/**
	 * 香港票房
	 */
	private static final String HONGKONG_TICKET = "hongkongTicket";
	
	/**
	 * 美国票房
	 */
	private static final String US_TICKET = "usTicket";
	
	/**
	 * 
	 * <p>Title: addBoxOffice</p> 
	 * <p>Description: 添加票房信息</p> 
	 * @author :changjiang
	 * date 2015-3-7 下午5:00:39
	 * @param boxOffice
	 * @return
	 */
	public static String addBoxOffice(String boxOffice){
		String finalBoxOffice = boxOffice;
		if(null!=boxOffice&&boxOffice.contains("{")){
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				Map<String, String> map = objectMapper.readValue( finalBoxOffice,Map.class);
				Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String, String> entry = iterator.next();
					if(entry.getKey().equals(TOTAL_TICKET)){
						finalBoxOffice = entry.getValue();
						break;
					}else if(entry.getKey().equals(MAINLAND_TICKET)){//大陆票房
						finalBoxOffice = entry.getValue();
						break;
					}else if(entry.getKey().equals(HONGKONG_TICKET)){//香港票房
						finalBoxOffice = entry.getValue();
						break;
					}else if(entry.getKey().equals(US_TICKET)){//北美票房
						finalBoxOffice = entry.getValue();
						break;
					}else{
						finalBoxOffice = "";
					}
				}
			}catch (Exception e) {
				LOG.error(e.getMessage(),e.fillInStackTrace());
			}
		}
		return finalBoxOffice;
	}
	
	/**
	 * 
	 * <p>Title: addAlias</p> 
	 * <p>Description: 给电影添加别名（目前只有英文）</p> 
	 * @author :changjiang
	 * date 2015-1-29 下午5:14:06
	 * @param name
	 * @param alias
	 * @return
	 */
	public static String addAlias(String name,String alias){
		if(null==name||"".equals(name)){
			return "";
		}
		String nameStr = "";
		String aliasStr = "";
		//判断不为中文的时候并且不为运营添加的时候
		try{
			if(name.matches("[A-z,0-9,\\s,:,\\.,']*")||name.matches("[\uAC00-\uD7A3,!, ,-,0-9,']*")||name.matches("[\u0800-\u4e00,・]*")){//当为全部英文，韩文，日文的时候加入别名
				//System.out.println(name);
				if(alias.contains("/")){
					String[] aliasArray = alias.split("/");
					aliasStr = aliasArray[0];
				}else{
					aliasStr = alias;
				}
				//如果别名包含中文 加到name的前面
				Pattern chsPattern = Pattern.compile("[\u4E00-\u9FA5]*");
				Matcher chsMatcher = chsPattern.matcher(aliasStr);
				if(chsMatcher.find()){
					nameStr = aliasStr + " "+ name;
				}
			}else{//
				nameStr = name;
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			nameStr = name;
		}
		return nameStr;
	}
	
	
	/**
	 * 
	 * <p>Title: getMovieTimes</p> 
	 * <p>Description: 电影时间</p> 
	 * @author :changjiang
	 * date 2015-1-30 下午12:16:54
	 * @param movieTime
	 * @return
	 * “2004-12-23”
	 */
	public static long getMovieTimes(String movieTime ){
		long finalForamatDate = 31507200000L;
		movieTime = movieTime.trim();
		movieTime = movieTime.replaceAll("\\(.*|<.*|,.*|&.*|/.*", "");
		SimpleDateFormat dateForm = null;
		if(movieTime.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}")){//2004-12-23
			dateForm = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		}else if(movieTime.matches("[0-9]{4}")){//1983
			dateForm = new SimpleDateFormat("yyyy",Locale.US);
		}else if(movieTime.matches("[0-9]{4}-[0-9]{1,2}")){//2014-07
			dateForm = new SimpleDateFormat("yyyy-MM",Locale.US);
		}else if(movieTime.matches("[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日")){//2014年2月14日
			dateForm = new SimpleDateFormat("yyyy年MM月dd日",Locale.US);
		}else if(movieTime.matches("[0-9]{4}\\.[0-9]{1,2}\\.[0-9]{1,2}")){//2013.6.29
			dateForm = new SimpleDateFormat("yyyy.MM.dd",Locale.US);
		}
		Date date = null;
		try {
			if(dateForm != null){
				date = dateForm.parse(movieTime);
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
