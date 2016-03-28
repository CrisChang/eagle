package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class GraphicFilm extends BaseDO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5281301380168073436L;

	private long id;						//主键id
	private long uid;						//用户id
	private String title;					//标题
	private String content;				//内容
	private String type;					//类型
	private int isDel;					//是否删除
	private String description;			//描述
	private String cover;					//封面
	private long createDate;			//创建日期
	private long latestRevisionDate;		//最后修改时间
	private int flag;						//标识符
	
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
