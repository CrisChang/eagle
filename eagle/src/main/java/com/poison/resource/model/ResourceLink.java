package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class ResourceLink extends BaseDO{

	/**
	 * ResourceLink序列号
	 */
	private static final long serialVersionUID = -660991499101249629L;
	private long id;			//主键
	private long resId;		//资源id
	private long resLinkId;//资源链接id
	private String resType;//资源类型
	private String linkType;//资源链接类型
	private String type;		//主要类型
	private String description;//描述
	private int isDel;			//是否删除
	private int flag;			//标识符
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	public long getResLinkId() {
		return resLinkId;
	}
	public void setResLinkId(long resLinkId) {
		this.resLinkId = resLinkId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getResId() {
		return resId;
	}
	public void setResId(long resId) {
		this.resId = resId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
