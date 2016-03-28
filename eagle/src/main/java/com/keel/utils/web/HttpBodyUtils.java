package com.keel.utils.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpBodyUtils {
	private static final  Log LOG = LogFactory.getLog(HttpBodyUtils.class);
	
	public static String getBodyString(HttpServletRequest request) {

	
		String inputLine;
		String str = "";
		BufferedReader bufferedReader = null;
		try {
/*<<<<<<< .mine
			request.setCharacterEncoding("gbk");
			//str= request.getParameter("body");
			while ((inputLine = request.getReader().readLine()) != null) {
=======*/
			bufferedReader = request.getReader();
		} catch(IllegalStateException e){
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			} catch (IOException e1) {
				LOG.error(e1.getMessage(), e1.fillInStackTrace());
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		if (null == bufferedReader) {
			throw new IllegalStateException("HttpBodyUtils cann't obtain bufferedReader");
		}
		
		try {
			while ((inputLine = bufferedReader.readLine()) != null) {
				str += inputLine;
			}
			bufferedReader.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return str;
	}
}
