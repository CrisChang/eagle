package com.poison.store.model;

import com.keel.common.lang.BaseDO;

public class MovieStills extends BaseDO{

	/**
	 * MovieStills序列号
	 */
	private static final long serialVersionUID = 8954288223351035892L;
	private long id;								//剧照主键id
	private long mvId;							//电影id
	private String movieStills;					//电影剧照片花
	private String other;							//其他信息
	private int isDelete;						//是否删除0为未删除1为已删除
	private long createDate;					//创建日期
	private long latestRevisionDate;		//最后修改时间
	private int flag;								//判断标示
	private String twoDimensionCode;//二维码
	
	public String getTwoDimensionCode() {
		return twoDimensionCode;
	}
	public void setTwoDimensionCode(String twoDimensionCode) {
		this.twoDimensionCode = twoDimensionCode;
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
	public String getMovieStills() {
		return movieStills;
	}
	public void setMovieStills(String movieStills) {
		this.movieStills = movieStills;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
