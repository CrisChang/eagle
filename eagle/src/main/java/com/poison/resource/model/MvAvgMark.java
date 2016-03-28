package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class MvAvgMark extends BaseDO{

	/**
	 * MvAvgMark的序列号
	 */
	private static final long serialVersionUID = -7705163904071729116L;
	private long id;							//主键
	private long mvId;							//电影的id
	private float mvAvgMark;			//电影的平均评分
	private int mvTotalNum;				//对电影评论的总人数
	private float expertsAvgMark;		//专家平均分
	private int expertsTotalNum;		//专家总人数
	private int isDelete;						//是否删除 0为未删除，1为已删除
	private long latestRevisionDate;	//最后修改时间
	private int flag;							//标示位
	
	public float getExpertsAvgMark() {
		return expertsAvgMark;
	}
	public void setExpertsAvgMark(float expertsAvgMark) {
		this.expertsAvgMark = expertsAvgMark;
	}
	public int getExpertsTotalNum() {
		return expertsTotalNum;
	}
	public void setExpertsTotalNum(int expertsTotalNum) {
		this.expertsTotalNum = expertsTotalNum;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public long getMvId() {
		return mvId;
	}
	public void setMvId(long mvId) {
		this.mvId = mvId;
	}
	public float getMvAvgMark() {
		return mvAvgMark;
	}
	public void setMvAvgMark(float mvAvgMark) {
		this.mvAvgMark = mvAvgMark;
	}
	public int getMvTotalNum() {
		return mvTotalNum;
	}
	public void setMvTotalNum(int mvTotalNum) {
		this.mvTotalNum = mvTotalNum;
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
	
	
}
