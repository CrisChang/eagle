package com.poison.otherinterface.umeng_java_sdk.push.android;

import com.poison.otherinterface.umeng_java_sdk.push.AndroidNotification;


public class AndroidUnicast extends AndroidNotification {
	public AndroidUnicast() {
		try {
			this.setPredefinedKeyValue("type", "unicast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}