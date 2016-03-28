package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class Tag extends BaseDO implements Comparable<Tag> {

	/**
	 * Tag序列号
	 */
	private static final long serialVersionUID = 453023599339331517L;
	private long id;					//主键ID
	private String tagName;		//标签名字
	private String tagGroup;		//标签分组
	private String type;				//标签类型
	private int isDelete;				//是否删除
	private int isTop;					//是否置顶
	private String resourceLinkType;		//对应type类型
	private long resid;//资源id
	private String restype;//资源类型
	private String cover;//封面地址
	private String introduce;//说明
	private int flag;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getResourceLinkType() {
		return resourceLinkType;
	}
	public void setResourceLinkType(String resourceLinkType) {
		this.resourceLinkType = resourceLinkType;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
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
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagGroup() {
		return tagGroup;
	}
	public void setTagGroup(String tagGroup) {
		this.tagGroup = tagGroup;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getResid() {
		return resid;
	}
	public void setResid(long resid) {
		this.resid = resid;
	}
	public String getRestype() {
		return restype;
	}
	public void setRestype(String restype) {
		this.restype = restype;
	}
	@Override
	public int compareTo(Tag o) {
		if(o.tagGroup.getBytes().length>=this.tagGroup.getBytes().length){
			return 1;
		}
		return -1;
	}
	
	
}
