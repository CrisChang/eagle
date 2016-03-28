package com.poison.eagle.utils;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;

import java.util.Map;

import javax.management.RuntimeErrorException;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.poison.eagle.entity.ResourceInfo;

public class BaseManager {
	private static ObjectMapper objectMapper;
	private Map<String, Object> res ;
	private Map<String, Object> json;
	private String jsonString;
	public final String RES_DATA_NOTRESULT = CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_DATARESULTERROR+CommentUtils.RES_ERROR_END;
	public final String RES_DATA_NOTGET = CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_DATANOTGET+CommentUtils.RES_ERROR_END;
	
	
	public final String RES_DATA_RIGHT_BEGIN = "{\"res\":{\"data\":{\"flag\":\"0\",";
	public final String RES_DATA_RIGHT_END = "}}}";
	public final String ISTRUE = "0";
	public final String ISFALSE = "1";
	/**
	 * TRUE = 0
	 */
	public int TRUE = 0;
	/**
	 * FALSE = 1
	 */
	public int FALSE = 1;
	public long UNID = 0;
	/**
	 * RESOURCE_PAGE_SIZE = 15
	 */
	public int RESOURCE_PAGE_SIZE = 15;
	

	public  ObjectMapper getObjectMapper() {
		if(objectMapper == null){
			objectMapper  = new ObjectMapper();
			
			objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false) ;
//			objectMapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
//			objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
			return objectMapper;
		}else{
			return objectMapper;
		}
	}
	
	
	/**
	 * 服务端返回json数据公共方法
	 * @param data
	 * @return
	 */
	public String getResponseData(Map<String, Object> data){
		json = new HashMap<String, Object>();
		res = new HashMap<String, Object>();
		res.put("data", data);
		json.put("res", res);
		
		try {
			jsonString = getObjectMapper().writeValueAsString(json);
		} catch (Exception e) {
			e.printStackTrace();
			jsonString= RES_DATA_NOTRESULT;
		}
		if(jsonString == null){
			jsonString = RES_DATA_NOTRESULT;
		}
		return jsonString;
	}
	
	public int changeIntForTrueFalse(int isTrue){
		int tmp = 0;
		if(isTrue == 0){
			tmp =  1;
		}else if(isTrue == 1){
			tmp =  0;
		}
		
		return tmp;
	}
	
	public void clearObject(Object... obj){
		for (Object o : obj) {
			if(o != null){
				o = null;
			}
		}
	}

	/**
	 * 通用的解析请求参数方法
	 * @param requestMsg 请求体
	 * @param c 返回类型
	 * @return
	 */
	public <T> T readValue(String requestMsg,Class<T> c){
		try {
			return getObjectMapper().readValue(requestMsg, c);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public static void main(String[] args) {
		BaseManager baseManager = new BaseManager();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
				objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
				objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false) ;
				
				ResourceInfo resourceInfo = new ResourceInfo();
				String jsons = objectMapper.writeValueAsString(resourceInfo);
				System.out.println(jsons);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			JSONObject json = baseManager.getObjectMapper().readValue("{\"ss\":\"ss\"}",  new TypeReference<JSONObject>(){});
//			System.out.println(json.toString());
			ResourceInfo resourceInfo = new ResourceInfo();
			String jsons = baseManager.getObjectMapper().writeValueAsString(resourceInfo);
			System.out.println(jsons);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
