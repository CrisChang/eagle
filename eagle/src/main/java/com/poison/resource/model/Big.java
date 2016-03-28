package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class Big extends BaseDO{

	/**
	 * Big序列号
	 */
	private static final long serialVersionUID = 10800407226232683L;
	private int id;							//主键ID
	private String attribute;				//属性
	private float attributeValue;		//属性值
	private String branch;				//分支
	private String branchLevel;		//分支等级
	private String branchDetail;		//分支详情
	private float branchValue;		//分支值
	private int isDelete;					//是否删除
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public float getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(float attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getBranchLevel() {
		return branchLevel;
	}
	public void setBranchLevel(String branchLevel) {
		this.branchLevel = branchLevel;
	}
	public String getBranchDetail() {
		return branchDetail;
	}
	public void setBranchDetail(String branchDetail) {
		this.branchDetail = branchDetail;
	}
	public float getBranchValue() {
		return branchValue;
	}
	public void setBranchValue(float branchValue) {
		this.branchValue = branchValue;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
