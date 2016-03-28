package com.poison.act.model;

import com.keel.common.lang.BaseDO;

public class ActComment extends BaseDO{

	/**
	 * ActComment序列号
	 */
	private static final long serialVersionUID = 3685207217495351883L;
	private long id;				//评论主键
	private long userId;						//用户ID
	private long resourceId;				//资源ID
	private int isDelete;						//是否被删除0为未删除1为已删除
	private String commentContext;	//评论详情
	private long commentDate;			//评论日期
	private long latestRevisionDate;	//最后
	private int flag;							//标示位
	private String type;						//资源类型
	private long commentUserId;				//被评论人的id
	private long commentId;				//评论的id
	private long resUserId;				//作者的id
	
	public long getResUserId() {
		return resUserId;
	}
	public void setResUserId(long resUserId) {
		this.resUserId = resUserId;
	}
	public long getCommentUserId() {
		return commentUserId;
	}
	public void setCommentUserId(long commentUserId) {
		this.commentUserId = commentUserId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public String getCommentContext() {
		return commentContext;
	}
	public void setCommentContext(String commentContext) {
		if(null==commentContext){
			commentContext = "";
		}
		this.commentContext = commentContext;
	}
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public long getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(long commentDate) {
		this.commentDate = commentDate;
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
