package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class ResStatistic extends BaseDO{

	/**
	 * ResStatistic序列号
	 */
	private static final long serialVersionUID = -343696962718049763L;
	private long id;						//主键id
	private long resId;					//资源id
	private String type;					//类型
	private long falseVisit;				//假的访问量
	private long visitNumber;			//实际访问量
	private int isDelete;					//是否被删除
	private long latestRevisionDate;	//最后修改时间
	private int flag;						//标识符
	private long praiseNumber;              //点赞数
	private long commentNumber;             //评论数
	private long rewardNumber;              //打赏数
	private long readNumber;                //阅读数
	private long readRandomNumber;          //随机阅读数
	private long usefulNumber;              //有用数
	private long nousefulNumber;            //无用数
	private long resLinkId;
	private String resLinkType;
	
	private long heatNumber;
	private long searchNumber;
	private long totalSearchNumber;         //总的搜索数而非单日搜索数
	private long activityStageId;           //影评大赛活动ID
	
	
	public long getResId() {
		return resId;
	}
	public void setResId(long resId) {
		this.resId = resId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getFalseVisit() {
		return falseVisit;
	}
	public void setFalseVisit(long falseVisit) {
		this.falseVisit = falseVisit;
	}
	public long getVisitNumber() {
		return visitNumber;
	}
	public void setVisitNumber(long visitNumber) {
		this.visitNumber = visitNumber;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
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
	public long getPraiseNumber() {
		return praiseNumber;
	}
	public void setPraiseNumber(long praiseNumber) {
		this.praiseNumber = praiseNumber;
	}
	public long getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(long commentNumber) {
		this.commentNumber = commentNumber;
	}
	public long getRewardNumber() {
		return rewardNumber;
	}
	public void setRewardNumber(long rewardNumber) {
		this.rewardNumber = rewardNumber;
	}
	public long getReadNumber() {
		return readNumber;
	}
	public void setReadNumber(long readNumber) {
		this.readNumber = readNumber;
	}
	public long getReadRandomNumber() {
		return readRandomNumber;
	}
	public void setReadRandomNumber(long readRandomNumber) {
		this.readRandomNumber = readRandomNumber;
	}
	public long getUsefulNumber() {
		return usefulNumber;
	}
	public void setUsefulNumber(long usefulNumber) {
		this.usefulNumber = usefulNumber;
	}
	public long getNousefulNumber() {
		return nousefulNumber;
	}
	public void setNousefulNumber(long nousefulNumber) {
		this.nousefulNumber = nousefulNumber;
	}
	public long getResLinkId() {
		return resLinkId;
	}
	public void setResLinkId(long resLinkId) {
		this.resLinkId = resLinkId;
	}
	public String getResLinkType() {
		return resLinkType;
	}
	public void setResLinkType(String resLinkType) {
		this.resLinkType = resLinkType;
	}
	public long getHeatNumber() {
		return heatNumber;
	}
	public void setHeatNumber(long heatNumber) {
		this.heatNumber = heatNumber;
	}
	public long getSearchNumber() {
		return searchNumber;
	}
	public void setSearchNumber(long searchNumber) {
		this.searchNumber = searchNumber;
	}
	public long getTotalSearchNumber() {
		return totalSearchNumber;
	}
	public void setTotalSearchNumber(long totalSearchNumber) {
		this.totalSearchNumber = totalSearchNumber;
	}
	public long getActivityStageId() {
		return activityStageId;
	}
	public void setActivityStageId(long activityStageId) {
		this.activityStageId = activityStageId;
	}
	
}
