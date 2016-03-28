package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class MovieList extends BaseDO implements Comparable<MovieList> {
	/**
	 * MovieList序列号
	 */
	private static final long serialVersionUID = 744952736411606495L;
	private long id;// 主键id
	private long uid;// 用户名
	private String filmListName;// 电影名
	private long createDate;//创建时间
	private String reason;// 推荐理由
	private int isDel;// 0表示该数据没被删除，1表示数据删除
	private int type;// 0表示无单，1表示有单
	private String tag;			//影单标签 
	private int isPublishing;//是否发布
	private int flag = 0;//标示
	private String cover;//封面
	private long latestRevisionDate;//更新时间
	
	
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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

	public String getFilmListName() {
		return filmListName;
	}

	public void setFilmListName(String filmListName) {
		this.filmListName = filmListName;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getIsPublishing() {
		return isPublishing;
	}

	public void setIsPublishing(int isPublishing) {
		this.isPublishing = isPublishing;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}

	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}

	@Override
	public int compareTo(MovieList o) {
		if (o.id == this.id) {
			return 1;
		} else {
			return -1;
		}
	}

}
