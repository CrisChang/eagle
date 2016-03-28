package com.poison.otherinterface.umeng_java_sdk.push;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.poison.otherinterface.umeng_java_sdk.push.android.AndroidBroadcast;
import com.poison.otherinterface.umeng_java_sdk.push.android.AndroidCustomizedcast;
import com.poison.otherinterface.umeng_java_sdk.push.android.AndroidFilecast;
import com.poison.otherinterface.umeng_java_sdk.push.android.AndroidGroupcast;
import com.poison.otherinterface.umeng_java_sdk.push.android.AndroidUnicast;
import com.poison.otherinterface.umeng_java_sdk.push.ios.IOSBroadcast;
import com.poison.otherinterface.umeng_java_sdk.push.ios.IOSCustomizedcast;
import com.poison.otherinterface.umeng_java_sdk.push.ios.IOSFilecast;
import com.poison.otherinterface.umeng_java_sdk.push.ios.IOSGroupcast;
import com.poison.otherinterface.umeng_java_sdk.push.ios.IOSUnicast;

public class Demo {
	private String appkey = null;
	private String appMasterSecret = null;
	private String timestamp = null;
	private String name = null;
	private String tag=null;
	private String type = null;
	
	public Demo(String key, String secret,String orgname,String userId,String restype) {
		try {
			appkey = key;
			appMasterSecret = secret;
			timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
			name = orgname;
			tag = userId;
			type = restype;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void sendAndroidBroadcast() throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast();
		broadcast.setAppMasterSecret(appMasterSecret);
		broadcast.setPredefinedKeyValue("appkey", this.appkey);
		broadcast.setPredefinedKeyValue("timestamp", this.timestamp);
		broadcast.setPredefinedKeyValue("ticker", "Android broadcast ticker");
		broadcast.setPredefinedKeyValue("title",  "中文的title");
		broadcast.setPredefinedKeyValue("text",   "Android broadcast text");
		broadcast.setPredefinedKeyValue("after_open", "go_app");
		broadcast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		broadcast.setPredefinedKeyValue("production_mode", "true");
		// Set customized fields
		broadcast.setExtraField("test", "helloworld");
		broadcast.send();
	}
	
	public void sendAndroidUnicast() throws Exception {
		AndroidUnicast unicast = new AndroidUnicast();
		unicast.setAppMasterSecret(appMasterSecret);
		unicast.setPredefinedKeyValue("appkey", this.appkey);
		unicast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO Set your device token
		unicast.setPredefinedKeyValue("device_tokens", "xxxx");
		unicast.setPredefinedKeyValue("ticker", "Android unicast ticker");
		unicast.setPredefinedKeyValue("title",  "中文的title");
		unicast.setPredefinedKeyValue("text",   "Android unicast text");
		unicast.setPredefinedKeyValue("after_open", "go_app");
		unicast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		unicast.setPredefinedKeyValue("production_mode", "true");
		// Set customized fields
		unicast.setExtraField("test", "helloworld");
		unicast.send();
	}
	
	public void sendAndroidGroupcast() throws Exception {
		AndroidGroupcast groupcast = new AndroidGroupcast();
		groupcast.setAppMasterSecret(appMasterSecret);
		groupcast.setPredefinedKeyValue("appkey", this.appkey);
		groupcast.setPredefinedKeyValue("timestamp", this.timestamp);
		/*  TODO
		 *  Construct the filter condition:
		 *  "where": 
		 *	{
    	 *		"and": 
    	 *		[
      	 *			{"tag":"test"},
      	 *			{"tag":"Test"}
    	 *		]
		 *	}
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		JSONObject TestTag = new JSONObject();
		testTag.put("tag", "test");
		TestTag.put("tag", "Test");
		tagArray.put(testTag);
		tagArray.put(TestTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());
		
		groupcast.setPredefinedKeyValue("filter", filterJson);
		groupcast.setPredefinedKeyValue("ticker", "Android groupcast ticker");
		groupcast.setPredefinedKeyValue("title",  "中文的title");
		groupcast.setPredefinedKeyValue("text",   "Android groupcast text");
		groupcast.setPredefinedKeyValue("after_open", "go_app");
		groupcast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		groupcast.setPredefinedKeyValue("production_mode", "true");
		groupcast.send();
	}
	
	public void sendAndroidCustomizedcast() throws Exception {
		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast();
		customizedcast.setAppMasterSecret(appMasterSecret);
		customizedcast.setPredefinedKeyValue("appkey", this.appkey);
		customizedcast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		customizedcast.setPredefinedKeyValue("alias", "xx");
		// TODO Set your alias_type here
		customizedcast.setPredefinedKeyValue("alias_type", "xx");
		customizedcast.setPredefinedKeyValue("ticker", "Android customizedcast ticker");
		customizedcast.setPredefinedKeyValue("title",  "中文的title");
		customizedcast.setPredefinedKeyValue("text",   "Android customizedcast text");
		customizedcast.setPredefinedKeyValue("after_open", "go_app");
		customizedcast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		customizedcast.setPredefinedKeyValue("production_mode", "true");
		customizedcast.send();
	}
	
	public void sendAndroidFilecast() throws Exception {
		AndroidFilecast filecast = new AndroidFilecast();
		filecast.setAppMasterSecret(appMasterSecret);
		filecast.setPredefinedKeyValue("appkey", this.appkey);
		filecast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens 
		filecast.uploadContents("aa"+"\n"+"bb");
		filecast.setPredefinedKeyValue("ticker", "Android filecast ticker");
		filecast.setPredefinedKeyValue("title",  "中文的title");
		filecast.setPredefinedKeyValue("text",   "Android filecast text");
		filecast.setPredefinedKeyValue("after_open", "go_app");
		filecast.setPredefinedKeyValue("display_type", "notification");
		filecast.send();
	}
	
	public void sendIOSBroadcast() throws Exception {
		IOSBroadcast broadcast = new IOSBroadcast();
		broadcast.setAppMasterSecret(appMasterSecret);
		broadcast.setPredefinedKeyValue("appkey", this.appkey);
		broadcast.setPredefinedKeyValue("timestamp", this.timestamp);

		broadcast.setPredefinedKeyValue("alert", "尹楠黑眼圈");
		broadcast.setPredefinedKeyValue("badge", 1);
		broadcast.setPredefinedKeyValue("sound", "chime");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		broadcast.setPredefinedKeyValue("production_mode", "false");
		// Set customized fields
		broadcast.setCustomizedField("test", "高口碑低票房的国产电影");
		broadcast.setCustomizedField("id", "152980525752127488");
		broadcast.setCustomizedField("type", "1");
		broadcast.send();
	}
	
	public void sendIOSUnicast() throws Exception {
		IOSUnicast unicast = new IOSUnicast();
		unicast.setAppMasterSecret(appMasterSecret);
		unicast.setPredefinedKeyValue("appkey", this.appkey);
		unicast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO Set your device token
		//unicast.setPredefinedKeyValue("device_tokens", "9c4fbe495ef3baedb018a3e2efaa71ad51c3f20f56a1acd45ba71e76e489f626");
		unicast.setPredefinedKeyValue("alert", "IOS 单播测试");
		unicast.setPredefinedKeyValue("badge", 342);
		unicast.setPredefinedKeyValue("sound", "chime");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		unicast.setPredefinedKeyValue("production_mode", "false");
		// Set customized fields
		unicast.setCustomizedField("test", "helloworld");
		unicast.setCustomizedField("id", "142797933568458752");
		unicast.setCustomizedField("type", "1");
		unicast.send();
	}
	
	public void sendIOSGroupcast() throws Exception {
		IOSGroupcast groupcast = new IOSGroupcast();
		groupcast.setAppMasterSecret(appMasterSecret);
		groupcast.setPredefinedKeyValue("appkey", this.appkey);
		groupcast.setPredefinedKeyValue("timestamp", this.timestamp);
		/*  TODO
		 *  Construct the filter condition:
		 *  "where": 
		 *	{
    	 *		"and": 
    	 *		[
      	 *			{"tag":"iostest"}
    	 *		]
		 *	}
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		testTag.put("tag", tag);
		tagArray.put(testTag);
		whereJson.put("or", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());
		
		// Set filter condition into rootJson
		groupcast.setPredefinedKeyValue("filter", filterJson);
		groupcast.setPredefinedKeyValue("alert", name);
		groupcast.setPredefinedKeyValue("badge", 1);
		groupcast.setPredefinedKeyValue("sound", "chime");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		groupcast.setPredefinedKeyValue("production_mode", "false");
		
		groupcast.setCustomizedField("test", "helloworld");
		//groupcast.setCustomizedField("id", "142797933568458752");
		groupcast.setCustomizedField("type", type);
		groupcast.send();
	}
	
	public void sendIOSCustomizedcast() throws Exception {
		IOSCustomizedcast customizedcast = new IOSCustomizedcast();
		customizedcast.setAppMasterSecret(appMasterSecret);
		customizedcast.setPredefinedKeyValue("appkey", this.appkey);
		customizedcast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		customizedcast.setPredefinedKeyValue("alias", "342");
		// TODO Set your alias_type here
		customizedcast.setPredefinedKeyValue("alias_type", "50");
		customizedcast.setPredefinedKeyValue("alert", "IOS 个性化测试");
		customizedcast.setPredefinedKeyValue("badge", 0);
		customizedcast.setPredefinedKeyValue("sound", "chime");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		customizedcast.setPredefinedKeyValue("production_mode", "false");
		customizedcast.send();
	}
	
	public void sendIOSFilecast() throws Exception {
		IOSFilecast filecast = new IOSFilecast();
		filecast.setAppMasterSecret(appMasterSecret);
		filecast.setPredefinedKeyValue("appkey", this.appkey);
		filecast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens 
		filecast.uploadContents("aa"+"\n"+"bb");
		filecast.setPredefinedKeyValue("alert", "IOS 文件播测试");
		filecast.setPredefinedKeyValue("badge", 0);
		filecast.setPredefinedKeyValue("sound", "chime");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		filecast.setPredefinedKeyValue("production_mode", "false");
		filecast.send();
	}
	
	public static void main(String[] args) {
		// TODO set your appkey and master secret here
		//Demo demo = new Demo("548671c7fd98c5590200033c", "7lbz2nffeuf8f1gueu06v3zu9qxsgpdb");
		try {
			//demo.sendIOSUnicast();
			//demo.sendIOSGroupcast();
			//sendIOSCustomizedcast();
			//sendIOSBroadcast();
			//sendAndroidUnicast();
			/* TODO these methods are all available, just fill in some fields and do the test
			 * demo.sendAndroidBroadcast();
			 * demo.sendAndroidGroupcast();
			 * demo.sendAndroidCustomizedcast();
			 * demo.sendAndroidFilecast();
			 * 
			 * demo.sendIOSBroadcast();
			 * demo.sendIOSUnicast();
			 * demo.sendIOSGroupcast();
			 * demo.sendIOSCustomizedcast();
			 * demo.sendIOSFilecast();
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

}
