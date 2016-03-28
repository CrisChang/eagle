package com.poison.otherinterface.umeng_java_sdk.push.ios;

import com.poison.otherinterface.umeng_java_sdk.push.IOSNotification;


public class IOSGroupcast extends IOSNotification {
	public IOSGroupcast() {
		try {
			this.setPredefinedKeyValue("type", "groupcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
