package com.poison.act.model;

import java.io.Serializable;

import com.keel.common.lang.BaseDO;
/**
 * 微信用户信息
 * @Description:
 * @author Weizhensong
 *
 */
public class ActWeixinUser extends BaseDO implements Comparable<ActWeixinUser>,Serializable{

	/**
	 * ActWeixinUser序列号
	 */
	private static final long serialVersionUID = 3685207217895351883L;
	private long id;				//主键
	private String openId;						//微信用户openid
	private String nickName;	//微信用户昵称
	private int sex;			//性别
	private String province;						//省份
	private String city;
	private String country;				//国家
	private String headimgUrl;			//头像
	private String unionId;				//微信用户unionid
	private long saveTime;				//保存时间
	private long updateTime;			//更新时间
	private int score;					//答题得分
	private int flag;//
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
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
	public long getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(long saveTime) {
		this.saveTime = saveTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	@Override
	public int compareTo(ActWeixinUser o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
}
