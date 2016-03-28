package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class BigLevelValue extends BaseDO{

	/**
	 * BigLevelValue序列号
	 */
	private static final long serialVersionUID = -8454173022920125952L;
	private int id;		//主键ID
	private int level;	//等级
	private String value;//每级总需分值
	private int flag;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
