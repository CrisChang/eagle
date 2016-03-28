package com.poison.otherinterface.umeng_java_sdk.push.ios;

import com.poison.otherinterface.umeng_java_sdk.push.IOSNotification;


public class IOSBroadcast extends IOSNotification {
	public IOSBroadcast() {
		try {
			this.setPredefinedKeyValue("type", "broadcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
