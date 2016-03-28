package com.poison.eagle.utils;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;

public class MainSend {
	public  void  sendMsgToStore(String sendMsg,String deviceToken){
		try {
			  //String deviceToken = "9e1c28e44cfa0f2474adfb8ac76dff8b4c05f532cd115d5dd242012e7d47941f";
			  //String deviceToken = "6982eed7e3791102d14204aa753e07ee9b3373d716edcdc17f8e5916ba02ff35";
			  PayLoad payLoad = new PayLoad();
			  payLoad.addAlert(sendMsg);
			  payLoad.addBadge(4);
			  payLoad.addSound("default");
						
			  PushNotificationManager pushManager = PushNotificationManager.getInstance();
			  pushManager.addDevice("iPhone", deviceToken);
						
			  //Connect to APNs
			  String host= "gateway.sandbox.push.apple.com";
			  int port = 2195;
			  String certificatePath= "d:/jinyingduyao.p12";
			  String certificatePassword= "10170802";
			  pushManager.initializeConnection(host,port, certificatePath,certificatePassword, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
						
			  //Send Push
			  Device client = pushManager.getDevice("iPhone");
			  pushManager.sendNotification(client, payLoad);
			  pushManager.stopConnection();

			  pushManager.removeDevice("iPhone");
			 }
			 catch (Exception e) {
			  e.printStackTrace();
		}
	}
}
