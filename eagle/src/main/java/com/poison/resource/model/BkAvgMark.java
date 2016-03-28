package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class BkAvgMark extends BaseDO{

	/**
	 * BkAvgMark序列号
	 */
	private static final long serialVersionUID = -7888712390944542542L;
	private long id;							//主键
	private int bkId;							//书的id
	private String resType;				//资源类型，区分图书和网络小说
	private float bkAvgMark;			//书的平均评分
	private int bkTotalNum;				//对书评论的总人数
	private int isDelete;						//是否删除 0为未删除，1为已删除
	private long latestRevisionDate;	//最后修改时间
	private int flag;							//标示位
	private float expertsAvgMark;		//专家平均分
	private int expertsTotalNum;		//专家总人数
	
	
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getBkId() {
		return bkId;
	}
	public void setBkId(int bkId) {
		this.bkId = bkId;
	}
	
	public float getBkAvgMark() {
		return bkAvgMark;
	}
	public void setBkAvgMark(float bkAvgMark) {
		this.bkAvgMark = bkAvgMark;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public int getBkTotalNum() {
		return bkTotalNum;
	}
	public void setBkTotalNum(int bkTotalNum) {
		this.bkTotalNum = bkTotalNum;
	}
	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}
	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
}
