package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;

/**
 * 毒药用户信息 DO  此接口只使用于图解电影返回上传图解者信息 ，其他用户信息返回 UserEntity 实体，
 * 
 * @author :zhangqi
 * @time:2015-8-6下午6:57:35
 * @return
 */
public class DuyaoerInfo extends BaseDO {

	private static final long serialVersionUID = 1L;
	private String userId; // 用户ID,可不传送，默认为空字符串
	private String faceAddress; // 头像
	private String name; // 名字

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFaceAddress() {
		return faceAddress;
	}

	public void setFaceAddress(String faceAddress) {
		this.faceAddress = faceAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
