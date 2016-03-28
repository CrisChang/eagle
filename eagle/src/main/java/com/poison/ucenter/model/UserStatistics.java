package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class UserStatistics extends BaseDO{

	/**
	 * UserStatistics序列号
	 */
	private static final long serialVersionUID = -1893891608346486060L;
	private long id;							//主键id
	private long userId;						//用户id
	private long bkcommentCount;	//书评总数
	private long mvcommentCount;	//影评总数
	private long bklistCount;				//发布书单总数
	private long mvlistCount;				//发布影单总数
	private long transmitCount;			//转发总数
	private long diaryCount;				//日记总数
	private long postCount;				//帖子总数
	private long articleCount;			//新的长文章总数
	private long serializeCount;			//连载总数
	private long totalCount;				//总数
	private long latestRevisionDate;	//最后修改日期
	private int flag;							//标识符
	private int commentSwitch;			//评论的提醒开关
	private int giveSwitch;					//打赏的提醒开关
	private int atSwitch;						//@的提醒开关
	
	
	public int getCommentSwitch() {
		return commentSwitch;
	}
	public void setCommentSwitch(int commentSwitch) {
		this.commentSwitch = commentSwitch;
	}
	public int getGiveSwitch() {
		return giveSwitch;
	}
	public void setGiveSwitch(int giveSwitch) {
		this.giveSwitch = giveSwitch;
	}
	public int getAtSwitch() {
		return atSwitch;
	}
	public void setAtSwitch(int atSwitch) {
		this.atSwitch = atSwitch;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
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
	public long getBkcommentCount() {
		return bkcommentCount;
	}
	public void setBkcommentCount(long bkcommentCount) {
		this.bkcommentCount = bkcommentCount;
	}
	public long getMvcommentCount() {
		return mvcommentCount;
	}
	public void setMvcommentCount(long mvcommentCount) {
		this.mvcommentCount = mvcommentCount;
	}
	public long getBklistCount() {
		return bklistCount;
	}
	public void setBklistCount(long bklistCount) {
		this.bklistCount = bklistCount;
	}
	public long getMvlistCount() {
		return mvlistCount;
	}
	public void setMvlistCount(long mvlistCount) {
		this.mvlistCount = mvlistCount;
	}
	public long getTransmitCount() {
		return transmitCount;
	}
	public void setTransmitCount(long transmitCount) {
		this.transmitCount = transmitCount;
	}
	public long getDiaryCount() {
		return diaryCount;
	}
	public void setDiaryCount(long diaryCount) {
		this.diaryCount = diaryCount;
	}
	public long getPostCount() {
		return postCount;
	}
	public void setPostCount(long postCount) {
		this.postCount = postCount;
	}
	public long getSerializeCount() {
		return serializeCount;
	}
	public void setSerializeCount(long serializeCount) {
		this.serializeCount = serializeCount;
	}
	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}
	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}
	public long getArticleCount() {
		return articleCount;
	}
	public void setArticleCount(long articleCount) {
		this.articleCount = articleCount;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
