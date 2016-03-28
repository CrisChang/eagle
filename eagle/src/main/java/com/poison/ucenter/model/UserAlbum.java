package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class UserAlbum extends BaseDO{

	/**
	 * UserAlbum序列号
	 */
	private static final long serialVersionUID = 1975530627040081311L;
	private long id;						//主键ID
	private long userId;					//用户的主键ID
	private String title;					//相册名字
	private String type;					//相册的类型
	private String content;				//相册内容
	private int isDelete;					//是否删除
	private long createDate;			//创建日期
	private long latestRevisionDate;//最后修改日期
	private int flag;						//标示位
	
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
