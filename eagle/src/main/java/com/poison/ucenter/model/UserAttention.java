package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class UserAttention extends BaseDO{

	/**
	 * UserAttention序列号
	 */
	private static final long serialVersionUID = 9202857901301987296L;
	private long attentionId;				//关注主键
	private long userId;						//用户ID
	private long userAttentionId;		//用户关注ID
	private int isAttention;					//是否被关注 0未关注 1已关注
	private String type;						//关注类型
	private long attentionDate;			//关注日期
	private long latestRevisionDate;	//最后修改时间
	private int flag;								//判断标示
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getAttentionId() {
		return attentionId;
	}
	public void setAttentionId(long attentionId) {
		this.attentionId = attentionId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getUserAttentionId() {
		return userAttentionId;
	}
	public void setUserAttentionId(long userAttentionId) {
		this.userAttentionId = userAttentionId;
	}
	public int getIsAttention() {
		return isAttention;
	}
	public void setIsAttention(int isAttention) {
		this.isAttention = isAttention;
	}
	public long getAttentionDate() {
		return attentionDate;
	}
	public void setAttentionDate(long attentionDate) {
		this.attentionDate = attentionDate;
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
