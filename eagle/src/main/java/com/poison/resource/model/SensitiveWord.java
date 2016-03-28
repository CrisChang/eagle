package com.poison.resource.model;

import com.keel.common.lang.BaseDO;


public class SensitiveWord extends BaseDO {
	
	/**
	 * SensitiveWord序列号
	 */
	private static final long serialVersionUID = -4415049648122212835L;
	private long id;
	private String sensitiveName;
	private int sensitiveLevel;
	private int isDelete;
	private int flag;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSensitiveName() {
		return sensitiveName;
	}
	public void setSensitiveName(String sensitiveName) {
		this.sensitiveName = sensitiveName;
	}
	public int getSensitiveLevel() {
		return sensitiveLevel;
	}
	public void setSensitiveLevel(int sensitiveLevel) {
		this.sensitiveLevel = sensitiveLevel;
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
}
