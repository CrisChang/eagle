package com.poison.eagle.utils.story;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 小说工具类
 * @author songdan
 * @date 2016年3月3日
 * @version V1.0
 */
public final class StoryUtils {
	/**
	 * 内容类型,提供相关类型的数据转换和还原。 可作为状态机使用
	 * @author songdan
	 * @date 2016年3月3日
	 * @version V1.0
	 */
	enum Type{
		/**纯文本*/
		TEXT("0"){
			public String convert(String content){
				return super.convert(content);
			}
			@Override
			public String reverse(String content) {
				return super.reverse(content);
			}
		};
		
		private String tag;
		
		private Type(String tag) {
			this.tag = tag;
		}
		
		
		public String getTag() {
			return tag;
		}


		public void setTag(String tag) {
			this.tag = tag;
		}


		/**类型转换*/
		public String convert(String content) {
			return content;
		}
		/**数据还原*/
		public String reverse(String content){
			return content;
		}
		
		public static  Type switchType(String tag){
			switch (tag) {
			case "0":
				return TEXT;

			default:
				return Type.TEXT;
			}
		}
	}
	
	private StoryUtils() {
	}
	
	private static final String TYPE_KEY="type";
	private static final String VALUE_KEY="content";
	
	/**
	 * 把List<Map<String,Object>>中的内容转换为字符串
	 * @param data
	 * @return String
	 */
	public static String putData2Str(List<Map<String, Object>> data){
		StringBuilder content = new StringBuilder(); 
		for (Map<String, Object> map : data) {
			Type type = Type.switchType((String)map.get(TYPE_KEY));
			content.append(type.convert((String)map.get(VALUE_KEY)));
		}
		return content.toString();
	}
	
	/**
	 * 把字符串内容转换为指定的格式
	 * @param content
	 * @return List<Map<String, Object>>
	 */
	public static List<Map<String, Object>> str2Data(String content){
		List<Map<String, Object>> list = new ArrayList<>();
		if(StringUtils.isEmpty(content)){
			return list;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TYPE_KEY,Type.TEXT.getTag());
		map.put(VALUE_KEY, Type.TEXT.reverse(content));
		list.add(map);
		return list;
	}
	/**
	 * 把字符串内容转换为指定的格式，没有类型参数，尽量不要使用
	 * @param content
	 * @return
	 */
	public static List<Map> str2DataNoInfer(String content){
		List<Map> list = new ArrayList<Map>();
		if(StringUtils.isEmpty(content)){
			return list;
		}
		Map map = new HashMap();
		map.put(TYPE_KEY,Type.TEXT.getTag());
		map.put(VALUE_KEY, Type.TEXT.reverse(content));
		list.add(map);
		return list;
	}
}
