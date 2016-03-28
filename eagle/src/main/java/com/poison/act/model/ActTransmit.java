package com.poison.act.model;

import com.keel.common.lang.BaseDO;

public class ActTransmit extends BaseDO{

	/**
	 * ActTransmit序列号
	 */
	private static final long serialVersionUID = -4707449960467810152L;
	private long id;				//转发ID主键
	private long userId;						//用户ID
	private long resourceId;				//资源ID
	private int isDelete;						//是否被删除
	private String transmitContext;		//转发内容
	private long transmitDate;			//转发日期
	private long latestRevisionDate;	//最后修改日期
	private int flag;							//状态标示
	private String type;						//转发类型
	
	
	
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
	public String getTransmitContext() {
		return transmitContext;
	}
	public void setTransmitContext(String transmitContext) {
		this.transmitContext = transmitContext;
	}
	public long getTransmitDate() {
		return transmitDate;
	}
	public void setTransmitDate(long transmitDate) {
		this.transmitDate = transmitDate;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
