package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class TopicLink extends BaseDO implements Comparable<TopicLink>{
	/**
	 * TopicLink序列号
	 */
	private static final long serialVersionUID = -1011684277869123967L;
	
	public static final int ISOPERATION_ESSENCE=1;//精华帖
	public static final int ISOPERATION_NORMAL=0;//普通的
	public static final int ISOPERATION_TOP=2;//置顶的
	
	private long id;//主鍵id
	private long userid;//用户id
	private long topicid;//话题id
	private String type;//话题类型
	private long resid;//资源id
	private String restype;//资源类型
	private long praisecount;//点赞数量
	private long commentcount;//评论数量
	private int isDel;//0表示删除，1表示未删除
	private long createDate;//创建时间
	private long latestRevisionDate;//更新时间
	private int isOperation;//干预标志，区分精华、置顶
	private int flag;//标示
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public long getTopicid() {
		return topicid;
	}
	public void setTopicid(long topicid) {
		this.topicid = topicid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getResid() {
		return resid;
	}
	public void setResid(long resid) {
		this.resid = resid;
	}
	public String getRestype() {
		return restype;
	}
	public void setRestype(String restype) {
		this.restype = restype;
	}
	public long getPraisecount() {
		return praisecount;
	}
	public void setPraisecount(long praisecount) {
		this.praisecount = praisecount;
	}
	public long getCommentcount() {
		return commentcount;
	}
	public void setCommentcount(long commentcount) {
		this.commentcount = commentcount;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}
	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}
	public int getIsOperation() {
		return isOperation;
	}
	public void setIsOperation(int isOperation) {
		this.isOperation = isOperation;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public TopicLink() {
		super();
	}
	@Override
	public int compareTo(TopicLink o) {
		if(o.id==this.id){
			return 1;
		}
		return -1;
	}
}
