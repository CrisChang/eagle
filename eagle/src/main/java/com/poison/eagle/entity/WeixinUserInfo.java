package com.poison.eagle.entity;

import java.io.Serializable;

import com.keel.common.lang.BaseDO;

/**
 * 微信用户信息
 * 
 * @Description:
 * @author zhangqi
 * 
 */
public class WeixinUserInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 3685207217895351883L;
	private String openId; // 微信用户openid
	private String nickName; // 微信用户昵称
	private String sex; // 性别
	private String province; // 省份
	private String city;
	private String country; // 国家
	private String headimgUrl; // 头像
	private String unionId; // 微信用户unionid

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgUrl() {
		return headimgUrl;
	}

	public void setHeadimgUrl(String headimgUrl) {
		this.headimgUrl = headimgUrl;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

}
