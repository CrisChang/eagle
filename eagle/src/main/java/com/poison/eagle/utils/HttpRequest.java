package com.poison.eagle.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
	// 通过连接地址获取返回信息
	public static String getUrl(String urladdress){
		try{
			URL url = new URL(urladdress);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String result = "";
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				result = result + inputLine;
			}
			inputLine = new String(result.getBytes(), "utf-8"); // 转码 防止获取到的信息乱码
			in.close();
			System.out.println(result);
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) throws IOException {
		getUrl("https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID");
	}
}
