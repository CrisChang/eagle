package com.poison.otherinterface.umeng_java_sdk.push.android;

import com.poison.otherinterface.umeng_java_sdk.push.AndroidNotification;


public class AndroidBroadcast extends AndroidNotification {
	public AndroidBroadcast() {
		try {
			this.setPredefinedKeyValue("type", "broadcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
