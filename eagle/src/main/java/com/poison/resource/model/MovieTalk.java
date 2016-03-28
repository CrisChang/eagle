package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class MovieTalk extends BaseDO implements Comparable<MovieTalk>{
	/**
	 * MovieTalk序列号
	 */
	private static final long serialVersionUID = 8299602394640704438L;
	private long id;
	private int movieId;
	private long uid;//用户id
	private String content;
	private int isDel;
	private int type;
	private long createTime;
	private int flag;
	public MovieTalk() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	@Override
	public int compareTo(MovieTalk o) {
		if(o.id==this.id){
			return 1;
		}
		return -1;
	}
}
