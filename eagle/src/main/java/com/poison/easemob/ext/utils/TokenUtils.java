package com.poison.easemob.ext.utils;

import com.poison.easemob.ext.comm.Constants;
import com.poison.easemob.ext.comm.Roles;
import com.poison.easemob.ext.vo.ClientSecretCredential;
import com.poison.easemob.ext.vo.Credential;

public class TokenUtils {
	// 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID,
            Constants.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);
    
    public static Credential getCredential(){
    	if(credential==null){
    		credential = new ClientSecretCredential(Constants.APP_CLIENT_ID,
    	            Constants.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);
    	}
    	return credential;
    }
}
