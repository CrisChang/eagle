package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class UserSummary  extends BaseDO{
	/**
	 * UserSummary序列号
	 */
	private static final long serialVersionUID = -8525605596968576157L;
	private long userId;								//用户ID 主键
	private String sign;			    				//用户个性签名
	private String interest;				    		//用户兴趣
	private String introduction;					//用户个人说明
	private long latestRevisionDate;   			//用户修改日期
	private int flag;									//返回标示
	private String identification;					//用户认证
	
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}
	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}
