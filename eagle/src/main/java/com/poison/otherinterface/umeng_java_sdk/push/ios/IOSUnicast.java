package com.poison.otherinterface.umeng_java_sdk.push.ios;

import com.poison.otherinterface.umeng_java_sdk.push.IOSNotification;


public class IOSUnicast extends IOSNotification {
	public IOSUnicast() {
		try {
			this.setPredefinedKeyValue("type", "unicast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
