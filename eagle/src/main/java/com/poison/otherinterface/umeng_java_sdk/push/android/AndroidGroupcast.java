package com.poison.otherinterface.umeng_java_sdk.push.android;

import com.poison.otherinterface.umeng_java_sdk.push.AndroidNotification;


public class AndroidGroupcast extends AndroidNotification {
	public AndroidGroupcast() {
		try {
			this.setPredefinedKeyValue("type", "groupcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
