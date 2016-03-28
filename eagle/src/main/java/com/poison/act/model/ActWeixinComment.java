package com.poison.act.model;

import com.keel.common.lang.BaseDO;
/**
 * 好友对微信用户的评价
 * @Description:	
 * @author Weizhensong
 *
 */
public class ActWeixinComment extends BaseDO{

	/**
	 * ActWeixinComment序列号
	 */
	private static final long serialVersionUID = 3686207267895351883L;
	private long id;				//主键
	private String openId;			//评论者的微信用户openid
	private String sopenId;			//被评论者的微信用户openid
	private String commentContext;	//评价内容
	private long saveTime;				//初次评价时间
	private long updateTime;		//修改时间（最后评价时间）
	
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
	public String getCommentContext() {
		return commentContext;
	}
	public void setCommentContext(String commentContext) {
		this.commentContext = commentContext;
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
	public String getSopenId() {
		return sopenId;
	}
	public void setSopenId(String sopenId) {
		this.sopenId = sopenId;
	}
}
