package com.poison.resource.model;

import com.keel.common.lang.BaseDO;
/**
 * 为了实现资源根据评论数量排行而创建，并不存在对应的数据库表
 * @Description:	
 * @author weizhensong
 *
 */
public class ResourceRanking extends BaseDO{

	/**
	 * ResourceRanking序列号
	 */
	private static final long serialVersionUID = -668991699181267629L;
	private long resId;		//资源id
	private String resType;//资源类型
	private int commentAmount;//评论数量
	private int flag;			//标识符
	public long getResId() {
		return resId;
	}
	public void setResId(long resId) {
		this.resId = resId;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public int getCommentAmount() {
		return commentAmount;
	}
	public void setCommentAmount(int commentAmount) {
		this.commentAmount = commentAmount;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
