package com.keel.utils.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.keel.framework.runtime.ProductContextHolder;

/**
 * 
 * NOTE: 依赖于ProductContext
 * */
public final class HttpHeaderUtils {
    private final static String HTTP_REQUEST_COOKIE_THREAD_CACHE = "_http_request_cookie_thread_cache_";
    
    /**
     * 获得真实IP地址
     */
    public static String getClientIP(HttpServletRequest request) {

    	String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
            ip = request.getHeader("Proxy-Client-IP");   
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
            ip = request.getHeader("WL-Proxy-Client-IP");   
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
            ip = request.getRemoteAddr();   
        }
        
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){ //"***.***.***.***".length() = 15
            if( ip.indexOf(",")>0){
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        return ip;   
    }

    /**
     * 获得代理IP地址
     */
    public static String getProxyIP(HttpServletRequest request) {

    	String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
            ip = request.getHeader("Proxy-Client-IP");   
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
            ip = request.getHeader("WL-Proxy-Client-IP");   
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
            return null;
        } else {
        	ip = request.getRemoteAddr();
        	return ip;
        }
    }
    
    /**
     * 获得referer
     */
    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }
    
    /**
     * 获取header
     * */
    public static String getRequestHeader(HttpServletRequest request, String name){
    	return request.getHeader(name);
    }
    
    /**
     * 设置header
     * */
    public static void setResponseHeader(HttpServletResponse response, String name, String value) {
    	response.setHeader(name, value);
    	return;
    }

    /**
     * 获得所有的cookie
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Cookie> getAllCookie(HttpServletRequest request) {
        // NOTICE: 增加cache，是为了避免多次遍历cookie
        Map<String, Cookie> cookieMap = (Map<String, Cookie>) ProductContextHolder
            .getProductContext().find(HttpHeaderUtils.HTTP_REQUEST_COOKIE_THREAD_CACHE);
        if (null == cookieMap) {
            cookieMap = new HashMap<String, Cookie>();
            Cookie[] cookies = request.getCookies();
            if (null != cookies) {
                int length = cookies.length;
                for (int i = 0; i < length; i++) {
                    cookieMap.put(cookies[i].getName(), cookies[i]);
                }
            }
            ProductContextHolder.getProductContext()
                .put(HttpHeaderUtils.HTTP_REQUEST_COOKIE_THREAD_CACHE, cookieMap);
        }
        return cookieMap;
    }
    
    /**
     * 取得某个cookie的值
     */
    public static String getCookieValue(String cookieName,HttpServletRequest request){
    	Cookie cookie=getAllCookie(request).get(cookieName);
    	if(cookie!=null){
    		return cookie.getValue();
    	}else{
    		return "";
    	}
    }
    
	/**
	 * 设置cookie
	 */
	 public static void saveCookie(HttpServletResponse response,String name,String value, String domain, int maxAge) {
		 Cookie userCookie = new Cookie(name,value);
		 userCookie.setPath("/");
		 userCookie.setDomain(domain);
		 userCookie.setMaxAge(maxAge);
		 response.addCookie(userCookie);
	 } 
	 
	/**
	 * 设置cookie
	 */
	public static void saveCookie(HttpServletResponse response, String name,
			String value, int maxAge, String domain, boolean httponly) {
		StringBuilder cookie = new StringBuilder(name);
		cookie.append("=");
		cookie.append(value);
		cookie.append("; path=/; ");
		cookie.append("domain=");
		cookie.append(domain);

		if (maxAge != -1) {
			Date date = new Date();
			long time = 1000l * maxAge + date.getTime();
			String toGMTString = toGMT(time);
			cookie.append("; expires=");
			cookie.append(toGMTString);
		}
		if (httponly)
			cookie.append("; HttpOnly");
		
		response.addHeader("Set-Cookie", cookie.toString());
	}
	 
	public static String toGMT(long time) {
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(time);
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE, d-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timeStr = sdf.format(cd.getTime());
		return timeStr;
	}
	
    /**
     * 删除cookie
     */
	 private  void removeCookie(HttpServletResponse response, String name, String domain) {
		 Cookie userCookie = new Cookie(name,"");
		 userCookie.setPath("/");
		 userCookie.setDomain(domain);
		 userCookie.setMaxAge(0);
		 response.addCookie(userCookie);
	 }
    
    /**
     * 获得URL，同时附加所有参数
     */
    @SuppressWarnings("unchecked")
    public static String getRequestURLWithParameter(HttpServletRequest request) {
        StringBuffer buffer = request.getRequestURL();
        Map parameter = request.getParameterMap();
        if (null != parameter && !parameter.isEmpty()) {
            buffer.append("?");
            Iterator keys = parameter.keySet().iterator();
            String key = null;
            String[] value = null;
            while (keys.hasNext()) {
                key = (String) keys.next();
                value = request.getParameterValues(key);
                if ((null == value) || (value.length == 0)) {
                    buffer.append(key).append("=").append("");
                } else if (value.length > 0) {
                    buffer.append(key).append("=").append(value[0]);
                }
                if (keys.hasNext()) {
                    buffer.append("&");
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 获得URL，不附加任何输入参数
     */
    public static String getRequestURL(HttpServletRequest request) {
        StringBuffer buffer = request.getRequestURL();
        return buffer.toString();
    }

    /**
     * 获得 Http://xxxx:yyy/context
     */
    public static String getHttpRootAddress(HttpServletRequest request) {
        String protocol = request.getProtocol();
        if (StringUtils.isNotBlank(protocol)) {
            int p = protocol.indexOf("/");
            if (p > -1) {
                protocol = protocol.substring(0, p);
            }
        }
        StringBuffer buffer = new StringBuffer(protocol);
        buffer.append("://");
        buffer.append(request.getServerName());
        buffer.append(":");
        buffer.append(request.getServerPort());
        String contextPath = request.getContextPath();
        if (StringUtils.isNotBlank(contextPath)) {
            buffer.append("/").append(contextPath);
        }
        return buffer.toString();
    }
}
