package com.poison.store.model;

import com.keel.common.lang.BaseDO;

public class OnlineRead extends BaseDO{

	/**
	 * OnlineRead的序列号
	 */
	private static final long serialVersionUID = 2559873144016767261L;
	private long id;							//主键id
	private long bkId;						//书的id
	private String onlineRead;				//在线阅读部分
	private String other;						//其他
	private int isDelete;						//是否删除
	private long createDate;				//创建时间
	private long latestRevisionDate;	//最后修改时间
	private String twoDimensionCode;//二维码
	private int flag;							//状态标示
	private String resType;					//资源类型
	
	
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getTwoDimensionCode() {
		return twoDimensionCode;
	}
	public void setTwoDimensionCode(String twoDimensionCode) {
		this.twoDimensionCode = twoDimensionCode;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
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
	public long getBkId() {
		return bkId;
	}
	public void setBkId(long bkId) {
		this.bkId = bkId;
	}
	public String getOnlineRead() {
		return onlineRead;
	}
	public void setOnlineRead(String onlineRead) {
		this.onlineRead = onlineRead;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
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
	
	
}
