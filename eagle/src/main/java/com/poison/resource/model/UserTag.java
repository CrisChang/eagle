package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class UserTag extends BaseDO{

	/**
	 * UserTag序列号
	 */
	private static final long serialVersionUID = 6334972339997593874L;
	private long id;				//标签表主键
	private long userId;			//用户id
	private long tagId;			//标签ID
	private String tagName;	//标签名字
	private long createDate;	//创建时间
	private long latestRevisionDate;	//最后修改时间
	private int isDelete;			//是否删除
	private int selectCount;	//选择的次数
	private String type;			//type类型
	private int flag;				//标识符
	
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
	public long getTagId() {
		return tagId;
	}
	public void setTagId(long tagId) {
		this.tagId = tagId;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
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
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public int getSelectCount() {
		return selectCount;
	}
	public void setSelectCount(int selectCount) {
		this.selectCount = selectCount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}
