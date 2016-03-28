package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

/**
 * 
 * 类的作用:此类的作用是封装数据库字段 
 * 作者:闫前刚 
 * 创建时间:2014-7-27下午4:43:12 
 * email :1486488968@qq.com
 * version: 1.0
 */
public class Chapter extends BaseDO implements Comparable<Chapter>{
	/**
	 * Chapter序列号
	 */
	private static final long serialVersionUID = 7611138431033304735L;
	private long id;// 主键id
	private long beginDate;// 创建时间
	private long updateDate;// 更新时间
	private String content;// 章节内容
	private String name;//创建章节名称
	private long uid;// 用户名
	private int flag = 0;// 标识符
	private long isDel=0;//0表示被删除1表示没删除
	private long parentId;//外键id
	public long getIsDel() {
		return isDel;
	}

	public void setIsDel(long isDel) {
		this.isDel = isDel;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(long beginDate) {
		this.beginDate = beginDate;
	}

	public long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public int compareTo(Chapter o) {
		if(o.id==this.id){
			return 1;
		}else{
			return -1;
		}
	}

	
}
