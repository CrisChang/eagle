package com.poison.eagle.action.weixin;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.utils.MD5Utils;

@Controller  
@RequestMapping("/weixin")
public class WeiXinController {
	
	private String weixinappid = "wx1441086740e20837";
	
	private String weixinappsecret = "e6c65b57845846986637f7c341b0eefd";
	
	 public static String token = null;
	 public static String time = null;
	 public static String jsapi_ticket = null;
	    

	@RequestMapping(value = "/callback", method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String loginByWeiXin(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		System.out.println("weixin------test");
		//微信加密签名
		String signature = request.getParameter("signature");
		//时间戳
		String timestamp = request.getParameter("timestamp");
		//随机数
		String nonce = request.getParameter("nonce");
		//随机字符串
		String echostr = request.getParameter("echostr");
		System.out.println(signature+";"+timestamp+";"+echostr);
		return echostr;
	}
	
	
	@RequestMapping(value = "/sign.do", method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getWeiXinSign(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		
		StringBuffer queryUrl = request.getRequestURL();
		String queryString = request.getQueryString();
		String resultUrl = (String) request.getParameter("url");
		
		String callback = (String) request.getParameter("callback");
		
		Map<String, String> tokenAndTicketMap = new HashMap<String, String>();
		if(token == null){
			tokenAndTicketMap = getAccessTokenAndTicket(weixinappid, weixinappsecret);
			token = tokenAndTicketMap.get("token");
			jsapi_ticket = tokenAndTicketMap.get("jsapi_ticket");
			time = getTime();
		}else{
			if(!time.substring(0, 13).equals(getTime().substring(0, 13))){ //每小时刷新一次){
				token = null;
				tokenAndTicketMap = getAccessTokenAndTicket(weixinappid, weixinappsecret);
				token = tokenAndTicketMap.get("token");
				jsapi_ticket = tokenAndTicketMap.get("jsapi_ticket");
				time = getTime();
			}
		}
		System.out.println("生成的token为"+token);
		System.out.println("生成的ticket为"+jsapi_ticket);
		String signStr = "";
		
		 String signature = "";
		 
		String nonce_str = MD5Utils.md5(create_nonce_str());
		signStr = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + time +
                "&url=" + resultUrl;
		
		
		MessageDigest crypt;
		try {
			crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(signStr.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        //System.out.println(signature);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("timestamp", time);
        resultMap.put("nonceStr", nonce_str);
        resultMap.put("signature", signature);
        JSONObject json = new JSONObject(resultMap);
        signature = json.toString();
        //System.out.println("生成的签名为："+signature);
		//凭证获取(GET)
        /*String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    String requestUrl = token_url.replace("APPID", weixinappid).replace("APPSECRET", weixinappsecret);
    HttpClient httpClient = null;
    GetMethod httpget = null;
    InputStream is = null;
    
    httpClient = new HttpClient();
    httpget = new GetMethod(requestUrl);
    String returnStr = "";
    httpClient.getParams().setSoTimeout(5000);
    int statusCode;
    String ticketStr = "";
    String signStr = "";
    String nonce_str = MD5Utils.md5(create_nonce_str());
    String timestamp = create_timestamp();
    String signature = "";
	try {
		statusCode = httpClient.executeMethod(httpget);
		if(statusCode == HttpStatus.SC_OK){
			//System. out.println("返回状态为" +statusCode);
			is = httpget.getResponseBodyAsStream();
			returnStr = IOUtils. toString(is,"utf8");
			JSONObject json = new JSONObject(returnStr);
			//System.out.println(returnStr);
			token = json.getString("access_token");
			//System.out.println(token);
		}
		
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
		String ticketUrl = url.replace("ACCESS_TOKEN", token);
		httpget = new GetMethod(ticketUrl);
		statusCode = httpClient.executeMethod(httpget);
		if(statusCode == HttpStatus.SC_OK){
			//System. out.println("返回状态为" +statusCode);
			is = httpget.getResponseBodyAsStream();
			returnStr = IOUtils. toString(is,"utf8");
			JSONObject json = new JSONObject(returnStr);
			//System.out.println(returnStr);
			ticketStr = json.getString("ticket");
			token = json.getString("access_token");
			System.out.println(token);
		}
		
		signStr = "jsapi_ticket=" + ticketStr +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + resultUrl;
		//System.out.println(signStr);
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
        crypt.update(signStr.getBytes("UTF-8"));
        signature = byteToHex(crypt.digest());
        //System.out.println(signature);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("timestamp", timestamp);
        resultMap.put("nonceStr", nonce_str);
        resultMap.put("signature", signature);
        JSONObject json = new JSONObject(resultMap);
        signature = json.toString();
	} catch (HttpException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (JSONException e) {
		e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	} finally{
		if (is != null) {
            try {
                     is.close();
           } catch (IOException e) {
           }
		  }
		   if (httpget != null) {
		           httpget.releaseConnection();
		            //
		            httpClient.getHttpConnectionManager().closeIdleConnections(0);
		  }
	}*/
	if(null!=callback){
		signature = callback+"("+signature+")";
	}
	
		return signature;
	}
	
	
	public static Map<String, String> getAccessTokenAndTicket(String appid,String appsecret){
		String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	    String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
	    Map<String, String> resultMap = new HashMap<String, String>();
	    
	    HttpClient httpClient = null;
	    GetMethod httpget = null;
	    InputStream is = null;
	    
	    httpClient = new HttpClient();
	    httpget = new GetMethod(requestUrl);
	    String returnStr = "";
	    httpClient.getParams().setSoTimeout(5000);
	    int statusCode;
	    String timestamp = create_timestamp();
		try {
			statusCode = httpClient.executeMethod(httpget);
			if(statusCode == HttpStatus.SC_OK){
				//System. out.println("返回状态为" +statusCode);
				is = httpget.getResponseBodyAsStream();
				returnStr = IOUtils. toString(is,"utf8");
				JSONObject json = new JSONObject(returnStr);
				//System.out.println(returnStr);
				token = json.getString("access_token");
				resultMap.put("token", token);
				//System.out.println(token);
			}
			
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
			String ticketUrl = url.replace("ACCESS_TOKEN", token);
			httpget = new GetMethod(ticketUrl);
			statusCode = httpClient.executeMethod(httpget);
			if(statusCode == HttpStatus.SC_OK){
				//System. out.println("返回状态为" +statusCode);
				is = httpget.getResponseBodyAsStream();
				returnStr = IOUtils. toString(is,"utf8");
				JSONObject json = new JSONObject(returnStr);
				//System.out.println(returnStr);
				jsapi_ticket = json.getString("ticket");
				/*token = json.getString("access_token");
				System.out.println(token);*/
				resultMap.put("jsapi_ticket", jsapi_ticket);
			}
		}catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally{
			if (is != null) {
	            try {
	                     is.close();
	           } catch (IOException e) {
	           }
			  }
			   if (httpget != null) {
			           httpget.releaseConnection();
			            //
			            httpClient.getHttpConnectionManager().closeIdleConnections(0);
			  }
		}
		return resultMap;
	}
	
	 private static String byteToHex(final byte[] hash) {
	        Formatter formatter = new Formatter();
	        for (byte b : hash)
	        {
	            formatter.format("%02x", b);
	        }
	        String result = formatter.toString();
	        formatter.close();
	        return result;
	    }
	 
	//获取当前系统时间 用来判断access_token是否过期
	    public static String getTime(){
	        Date dt=new Date();
	        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        return sdf.format(dt);
	    }
	 
	private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
 
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
     
}
