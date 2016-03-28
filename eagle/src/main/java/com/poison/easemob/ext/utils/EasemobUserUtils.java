package com.poison.easemob.ext.utils;

import com.poison.eagle.utils.MD5Utils;

public class EasemobUserUtils {
	/**
	 * 根据规则生成环信的用户id
	 * @Title: getEasemobUserId 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-7 下午3:00:04
	 * @param @param userId
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getEasemobUserId(long userId){
		String easemobUserId = MD5Utils.md5(userId+"");
		return easemobUserId;
	}
	/**
	 * 根据规则生成环信的密码
	 * @Title: getEasemobPassword 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-7 下午3:02:06
	 * @param @param userId
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getEasemobPassword(long userId){
		String password = MD5Utils.md5(userId+"");
		password = MD5Utils.md5(password.substring(8,24));
		return password;
	}
}
